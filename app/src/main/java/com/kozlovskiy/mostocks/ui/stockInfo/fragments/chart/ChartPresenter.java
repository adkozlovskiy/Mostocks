package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.models.chart.Candles;
import com.kozlovskiy.mostocks.models.chart.CandlesMarker;
import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.utils.Converter;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ChartPresenter {

    public static final String TAG = ChartPresenter.class.getSimpleName();
    private final AlertDialog.Builder builder;
    private final StocksRepository stocksRepository;
    private final CandleStickChart candlesChart;
    private final ChartView chartView;
    private final Context context;
    private final String symbol;
    private long from;
    private long to;
    private String resolution;

    public ChartPresenter(ChartView chartView, CandleStickChart candlesChart, Context context, String symbol) {
        this.stocksRepository = new StocksRepository(context);
        this.chartView = chartView;
        this.candlesChart = candlesChart;
        this.context = context;
        this.builder = new AlertDialog.Builder(context);
        this.symbol = symbol;

        configureCandlesChart();
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

    public void initializeCandles(long from, long to, String resolution) {
        this.from = from;
        this.to = to;
        this.resolution = resolution;
        stocksRepository.getSymbolCandles(symbol, resolution, from, to)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Candles>() {
                    @Override
                    public void onSuccess(@NonNull Candles candles) {
                        updateCandlesData(candles);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        buildErrorLoadingDialog(e);
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

    void updateCandlesData(Candles candles) {
        ArrayList<CandleEntry> values = new ArrayList<>();
        for (int i = 0; i < candles.getVolumes().size(); i++) {
            long timestamp = candles.getTimestamps().get(i);

            if (timestamp < from)
                continue;

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

    public void buildErrorLoadingDialog(Throwable e) {
        builder.setTitle(R.string.loading_error);
        if (e instanceof SocketTimeoutException) {
            builder.setMessage(R.string.timed_out);

        } else {
            builder.setMessage(R.string.unknown_error);

        }

        builder.setPositiveButton(R.string.retry, (di, i) -> initializeCandles(from, to, resolution))
                .setNegativeButton(R.string.cancel, (di, id) -> di.cancel());

        chartView.showDialog(builder.create());
    }
}