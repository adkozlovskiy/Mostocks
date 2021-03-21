package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.google.gson.Gson;
import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.entities.SocketData;
import com.kozlovskiy.mostocks.entities.SocketResponse;
import com.kozlovskiy.mostocks.room.StocksDao;
import com.kozlovskiy.mostocks.services.websocket.ClientWebSocket;
import com.kozlovskiy.mostocks.services.websocket.StockCostSocketConnection;
import com.kozlovskiy.mostocks.utils.QuoteConverter;

import static android.os.Looper.getMainLooper;

public class ChartPresenter implements ClientWebSocket.MessageListener {

    private final ChartView chartView;
    private StockCostSocketConnection stockCostSocketConnection;
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
        stockCostSocketConnection = new StockCostSocketConnection(ticker);
        stockCostSocketConnection.setListener(this);
        stockCostSocketConnection.openConnection();
    }

    public void unsubscribe() {
        stockCostSocketConnection.closeConnection();
    }

    @Override
    public void onSocketMessage(String message) {
        Gson gson = new Gson();
        try {
            SocketResponse response = gson.fromJson(message, SocketResponse.class);
            if (response.getType().equals("trade")) {
                SocketData data = response.getData().get(0);

                if (data.getS().equals(ticker)) {
                    Handler mainHandler = new Handler(getMainLooper());

                    Runnable mainRunnable = () -> chartView.showUpdatedCost(
                            QuoteConverter.convertToCurrencyFormat(data.getP(), 2, 4));
                    mainHandler.post(mainRunnable);

                    previousCost = currentCost;
                    currentCost = data.getP();
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
