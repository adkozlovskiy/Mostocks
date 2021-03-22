
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

import java.util.ArrayList;
import java.util.List;

import static android.os.Looper.getMainLooper;

public class ChartPresenter implements WebSocketClient.MessageListener {

    public static final String TAG = ChartPresenter.class.getSimpleName();
    private ChartView chartView;
    private WebSocketConnection webSocketConnection;
    private final String ticker;
    private double previousCost;
    private double currentCost;
    private final double pq;
    private final Context context;
    private final StocksDao stocksDao;

    public ChartPresenter(ChartView chartView, Context context, String ticker, double pq) {
        this.chartView = chartView;
        this.context = context;
        this.ticker = ticker;
        this.pq = pq;

        stocksDao = ((AppDelegate) context.getApplicationContext())
                .getDatabase()
                .getDao();
    }

    public void subscribe(String symbol) {
        List<String> symbols = new ArrayList<>();
        symbols.add(symbol);
        webSocketConnection = new WebSocketConnection(symbols);
        webSocketConnection.setListener(this);
        webSocketConnection.openConnection();
        Log.d(TAG, "subscribe: ");
    }

    public void unsubscribe() {
        webSocketConnection.closeConnection();
        Log.d(TAG, "unsubscribe: ");
    }

    public void calculateQuoteChange(double cq, double pq) {
        int color = context.getResources().getColor(R.color.textColor, context.getTheme());
        double difference = cq - pq;
        Drawable quoteDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_no_changes);
        String changeString = QuoteConverter.convertToCurrencyFormat(difference, 0, 2);
        String percentString = QuoteConverter.convertToDefaultFormat(difference / pq * 100, 0, 2);

        if (difference > 0) {
            color = context.getResources().getColor(R.color.positiveCost, context.getTheme());
            changeString = "+" + changeString;
            quoteDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_go_up);

        } else if (difference < 0) {
            color = context.getResources().getColor(R.color.negativeCost, context.getTheme());
            percentString = QuoteConverter.convertToDefaultFormat(difference / pq * -100, 0, 2);
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
                SocketData data = response.getData().get(response.getData().size() - 1);

                if (data.getSymbol().equals(ticker)) {
                    Handler mainHandler = new Handler(getMainLooper());

                    Runnable mainRunnable = () -> {
                        if (data.getQuote() == null) {
                            Log.d(TAG, "onSocketMessage: data null");
                        }

                        chartView.showUpdatedCost(
                                QuoteConverter.convertToCurrencyFormat(data.getQuote(), 0, 2));
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
