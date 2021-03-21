package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.google.gson.Gson;
import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.entities.SocketData;
import com.kozlovskiy.mostocks.entities.SocketResponse;
import com.kozlovskiy.mostocks.room.StocksDao;
import com.kozlovskiy.mostocks.services.websocket.WebSocketClient;
import com.kozlovskiy.mostocks.services.websocket.WebSocketConnection;
import com.kozlovskiy.mostocks.utils.QuoteConverter;

import static android.os.Looper.getMainLooper;

public class ChartPresenter implements WebSocketClient.MessageListener {

    public static final String TAG = ChartPresenter.class.getSimpleName();
    private ChartView chartView;
    private WebSocketConnection webSocketConnection;
    private final String ticker;
    private double previousCost;
    private double currentCost;
    private final Context context;
    private final StocksDao stocksDao;

    public ChartPresenter(ChartView chartView, Context context, String ticker) {
        this.chartView = chartView;
        this.context = context;
        this.ticker = ticker;

        stocksDao = ((AppDelegate) context.getApplicationContext())
                .getDatabase()
                .getDao();
    }

    public void subscribe(String ticker) {
        webSocketConnection = new WebSocketConnection(ticker);
        webSocketConnection.setListener(this);
        webSocketConnection.openConnection();
        Log.d(TAG, "subscribe: ");
    }

    public void unsubscribe() {
        webSocketConnection.closeConnection();
        Log.d(TAG, "unsubscribe: ");
        chartView = null;
    }

    @Override
    public void onSocketMessage(String message) {
        Gson gson = new Gson();
        try {
            SocketResponse response = gson.fromJson(message, SocketResponse.class);
            if (response.getType().equals("trade")) {
                SocketData data = response.getData().get(0);

                if (data.getSymbol().equals(ticker)) {
                    Handler mainHandler = new Handler(getMainLooper());

                    Runnable mainRunnable = () -> chartView.showUpdatedCost(
                            QuoteConverter.convertToCurrencyFormat(data.getQuote(), 2, 4));
                    mainHandler.post(mainRunnable);

                    previousCost = currentCost;
                    currentCost = data.getQuote();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void configureCandlesChart(CandleStickChart chart) {
        chart.setBackgroundColor(Color.WHITE);
        chart.getDescription().setEnabled(false);

        chartView.buildCandlesChart(chart);
    }
}
