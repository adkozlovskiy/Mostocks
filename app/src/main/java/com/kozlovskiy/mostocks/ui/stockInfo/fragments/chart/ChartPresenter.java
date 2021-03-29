package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.models.chart.Candles;
import com.kozlovskiy.mostocks.models.chart.CandlesMarker;
import com.kozlovskiy.mostocks.models.socket.SocketResponse;
import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.services.websocket.WebSocketClient;
import com.kozlovskiy.mostocks.services.websocket.WebSocketConnection;
import com.kozlovskiy.mostocks.utils.Converter;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static android.os.Looper.getMainLooper;

public class ChartPresenter implements WebSocketClient.MessageListener {

    public static final String TAG = ChartPresenter.class.getSimpleName();
    private final StocksRepository stocksRepository;
    private final CandleStickChart candlesChart;
    private final LineChart lineChart;
    private final ChartView chartView;
    private final Context context;
    private final String symbol;
    private final double pq;
    private boolean candlesChartSelected = false;

    public void setCandlesChartSelected(boolean candlesChartSelected) {
        this.candlesChartSelected = candlesChartSelected;
    }

    public boolean isCandlesChartSelected() {
        return candlesChartSelected;
    }

    private WebSocketConnection webSocketConnection;

    public ChartPresenter(ChartView chartView, CandleStickChart candlesChart, LineChart lineChart, Context context, String symbol, double pq) {
        this.stocksRepository = new StocksRepository(context);
        this.chartView = chartView;
        this.candlesChart = candlesChart;
        this.lineChart = lineChart;
        this.context = context;
        this.symbol = symbol;
        this.pq = pq;

        configureCandlesChart();
        configureLinesChart();
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
        String changeString = Converter.toCurrencyFormat(difference, 0, 2);
        String percentString = Converter.toDefaultFormat(difference / pq * 100, 0, 2);

        if (difference > 0) {
            color = context.getResources().getColor(R.color.positiveCost, context.getTheme());
            changeString = "+" + changeString;
            quoteDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_go_up);

        } else if (difference < 0) {
            color = context.getResources().getColor(R.color.negativeCost, context.getTheme());
            percentString = Converter.toDefaultFormat(difference / pq * -100, 0, 2);
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
                SocketResponse.Data data = response.getData().get(response.getData().size() - 1);

                if (data.getSymbol().equals(symbol)) {
                    Handler mainHandler = new Handler(getMainLooper());

                    Runnable mainRunnable = () -> {
                        if (data.getQuote() == null) {
                            Log.d(TAG, "onSocketMessage: data null");
                        }

                        chartView.showUpdatedCost(
                                Converter.toCurrencyFormat(data.getQuote(), 0, 2));
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
        stocksRepository.getSymbolCandles(symbol, resolution, from, to)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Candles>() {
                    @Override
                    public void onSuccess(@NonNull Candles candles) {
                        updateLinesData(candles);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace(); // todo alert dialog
                    }
                });
    }

    public void configureCandlesChart() {
        candlesChart.setBackgroundColor(Color.WHITE);
        candlesChart.getDescription().setEnabled(false);
        candlesChart.setMaxVisibleValueCount(0);
        candlesChart.setPinchZoom(true);
        candlesChart.setDoubleTapToZoomEnabled(false);
        candlesChart.setDrawGridBackground(false);
        candlesChart.setScaleEnabled(true);

        candlesChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e.getData() != null & e.getData() instanceof Long) {
                    long timestamp = (Long) e.getData() * 1000;
                    chartView.showTimeStamp(timestamp);
                }
            }

            @Override
            public void onNothingSelected() {
                chartView.hideTimeStamp();
            }
        });

        candlesChart.getXAxis().setEnabled(false);
        candlesChart.getAxisLeft().setEnabled(false);
        candlesChart.getAxisRight().setEnabled(false);
        candlesChart.getLegend().setEnabled(false);
    }

    public void configureLinesChart() {
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setMaxVisibleValueCount(0);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });
        lineChart.setDrawGridBackground(false);

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);

        lineChart.getXAxis().setEnabled(false);

        YAxis yAxis;
        yAxis = lineChart.getAxisLeft();
        yAxis.enableGridDashedLine(10f, 10f, 0f);

        lineChart.getAxisRight().setEnabled(false);

        lineChart.animateX(1500);
        lineChart.getLegend().setEnabled(false);
    }

    void updateCandlesData(Candles candles) {
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

        CandleDataSet set = new CandleDataSet(values, null);

        set.setDrawIcons(false);
        set.setShadowColor(Color.DKGRAY);
        set.setShadowWidth(0.7f);
        set.setDecreasingColor(context.getResources().getColor(R.color.sellIndicatorColor, context.getTheme()));
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setIncreasingColor(context.getResources().getColor(R.color.buyIndicatorColor, context.getTheme()));
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        set.setNeutralColor(Color.GRAY);

        CandlesMarker candlesMarker = new CandlesMarker(context, R.layout.candles_marker, values.size());
        candlesChart.setMarker(candlesMarker);

        CandleData data = new CandleData(set);
        candlesChart.setData(data);
        chartView.buildCandlesChart(candlesChart, values.size());
    }

    void updateLinesData(Candles candles) {
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < candles.getVolumes().size(); i++) {
            long timestamp = candles.getTimestamps().get(i);
            double close = candles.getClosePrices().get(i);

            Entry entry = new Entry(i, (float) close, null);
            entry.setData(timestamp);
            values.add(entry);
        }

        LineDataSet dataSet;
        dataSet = new LineDataSet(values, null);
        dataSet.setDrawIcons(false);
        dataSet.setDrawCircleHole(false);
        dataSet.setColor(Color.rgb(37, 86, 123));

        dataSet.setLineWidth(1.3f);
        dataSet.setDrawCircles(false);

        dataSet.setDrawFilled(true);
        dataSet.setFillFormatter((dataSt, dataProvider) -> lineChart.getAxisLeft().getAxisMinimum());

        dataSet.setFillColor(Color.rgb(240, 244, 247));

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData data = new LineData(dataSets);

        lineChart.setData(data);
        chartView.buildLinesChart(lineChart, values.size());
    }
}
