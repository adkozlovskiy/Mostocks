
package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.Candles;
import com.kozlovskiy.mostocks.entities.CandlesMarker;
import com.kozlovskiy.mostocks.entities.SocketData;
import com.kozlovskiy.mostocks.entities.SocketResponse;
import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.services.websocket.WebSocketClient;
import com.kozlovskiy.mostocks.services.websocket.WebSocketConnection;
import com.kozlovskiy.mostocks.utils.QuoteConverter;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static android.os.Looper.getMainLooper;

public class ChartPresenter implements WebSocketClient.MessageListener {

    public static final String TAG = ChartPresenter.class.getSimpleName();
    private final StocksRepository stocksRepository;
    private final CandleStickChart chart;
    private final ChartView chartView;
    private final Context context;
    private final String symbol;
    private final double pq;

    private WebSocketConnection webSocketConnection;

    public ChartPresenter(ChartView chartView, CandleStickChart chart, Context context, String symbol, double pq) {
        this.stocksRepository = new StocksRepository(context);
        this.chartView = chartView;
        this.chart = chart;
        this.context = context;
        this.symbol = symbol;
        this.pq = pq;
    }

    public void subscribe() {
        webSocketConnection = new WebSocketConnection(new ArrayList<String>() {{
            add(symbol);
        }});
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
        String changeString = QuoteConverter.toCurrencyFormat(difference, 0, 2);
        String percentString = QuoteConverter.toDefaultFormat(difference / pq * 100, 0, 2);

        if (difference > 0) {
            color = context.getResources().getColor(R.color.positiveCost, context.getTheme());
            changeString = "+" + changeString;
            quoteDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_go_up);

        } else if (difference < 0) {
            color = context.getResources().getColor(R.color.negativeCost, context.getTheme());
            percentString = QuoteConverter.toDefaultFormat(difference / pq * -100, 0, 2);
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

                if (data.getSymbol().equals(symbol)) {
                    Handler mainHandler = new Handler(getMainLooper());

                    Runnable mainRunnable = () -> {
                        if (data.getQuote() == null) {
                            Log.d(TAG, "onSocketMessage: data null");
                        }

                        chartView.showUpdatedCost(
                                QuoteConverter.toCurrencyFormat(data.getQuote(), 0, 2));
                        calculateQuoteChange(data.getQuote(), pq);
                    };
                    mainHandler.post(mainRunnable);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeCandles(long from, long to, String resolution) {
        Log.d(TAG, "initializeCandles: init from " + from + " to " + to);
        stocksRepository.getSymbolCandles(symbol, resolution, from, to)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Candles>() {
                    @Override
                    public void onSuccess(@NonNull Candles candles) {
                        configureCandlesChart(candles);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public void configureCandlesChart(Candles candles) {
        chart.setBackgroundColor(Color.WHITE);
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(0);
        chart.setPinchZoom(true);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setScaleEnabled(true);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e.getData() != null & e.getData() instanceof Long) {
                    long timestamp = (Long) e.getData() * 1000;
                    chartView.showTimeStamp(timestamp);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        chart.getLegend().setEnabled(false);

        chart.resetTracking();
        ArrayList<CandleEntry> values = new ArrayList<>();
        for (int i = 0; i < candles.getVolumes().size(); i++) {
            long timestamp = candles.getTimestamps().get(i);

            double high = candles.getHighPrices().get(i);
            double low = candles.getLowPrices().get(i);

            double open = candles.getOpenPrices().get(i);
            double close = candles.getClosePrices().get(i);

            CandleEntry entry = new CandleEntry(i, (float) high, (float) low, (float) open, (float) close);
            entry.setData(timestamp);
            values.add(entry);
        }

        CandleDataSet set = new CandleDataSet(values, "Data Set");

        set.setDrawIcons(false);
        set.setShadowColor(Color.DKGRAY);
        set.setShadowWidth(0.7f);
        set.setDecreasingColor(context.getResources().getColor(R.color.sellIndicatorColor, context.getTheme()));
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setIncreasingColor(context.getResources().getColor(R.color.buyIndicatorColor, context.getTheme()));
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        set.setNeutralColor(Color.GRAY);

        CandlesMarker candlesMarker = new CandlesMarker(context, R.layout.candles_marker, values.size());
        chart.setMarker(candlesMarker);
        CandleData data = new CandleData(set);

        chart.setData(data);
        chartView.buildCandlesChart(chart);
    }
}
