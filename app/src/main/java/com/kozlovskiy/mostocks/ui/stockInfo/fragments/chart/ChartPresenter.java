package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.google.gson.Gson;
import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.R;
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
    private double pq;
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

    public void calculateQuoteChange(double cq, double pq) {
        this.pq = pq;
        int color = context.getResources().getColor(R.color.textColor);
        double difference = cq - pq;
        Drawable quoteDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_no_changes);
        String changeString = QuoteConverter.convertToCurrencyFormat(difference, 2, 2);
        String percentString = QuoteConverter.convertToDefaultFormat(difference / pq * 100, 2, 2);

        if (difference > 0) {
            color = context.getResources().getColor(R.color.positiveCost);
            changeString = "+" + changeString;
            quoteDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_go_up);

        } else if (difference < 0) {
            color = context.getResources().getColor(R.color.negativeCost);
            percentString = QuoteConverter.convertToDefaultFormat(difference / previousCost * -100, 2, 2);
            quoteDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_go_down);

        }

        changeString += " (" + percentString + "%)";
        chartView.showQuoteChange(changeString, color, quoteDrawable);
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

                    Runnable mainRunnable = () -> {
                        chartView.showUpdatedCost(
                                QuoteConverter.convertToCurrencyFormat(data.getQuote(), 2, 4));
                        calculateQuoteChange(data.getQuote(), pq);
                    };
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
