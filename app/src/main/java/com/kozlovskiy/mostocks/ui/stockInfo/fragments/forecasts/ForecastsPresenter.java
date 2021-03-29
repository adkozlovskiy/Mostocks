package com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.kozlovskiy.mostocks.models.stockInfo.Recommendation;
import com.kozlovskiy.mostocks.models.stockInfo.TechAnalysisResponse;
import com.kozlovskiy.mostocks.repo.StocksRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ForecastsPresenter {

    private final ForecastsView forecastsView;
    public static final String TAG = ForecastsPresenter.class.getSimpleName();
    private final StocksRepository stocksRepository;
    private BarChart chart;
    private Context context;

    public ForecastsPresenter(ForecastsView forecastsView, Context context) {
        this.forecastsView = forecastsView;
        this.context = context;
        stocksRepository = new StocksRepository(context);
    }

    public void setChart(BarChart chart) {
        this.chart = chart;
        configureRecommendationBarChart();
    }

    public void initializeData(String symbol) {
        Completable.mergeArray(
                initializeRecommendation(symbol),
                initializeTechAnalysis(symbol)

        ).subscribe(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                forecastsView.setViewsVisibility();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }
        });
    }

    public Completable initializeRecommendation(String symbol) {
        return Completable.create((emitter) -> stocksRepository.getSymbolRecommendation(symbol)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<Recommendation>>() {
                    @Override
                    public void onSuccess(@NonNull List<Recommendation> r) {
                        String signal = null;
                        Recommendation lastRecommendation = r.get(0);
                        int buySignals = lastRecommendation.getBuySignals();
                        int sellSignals = lastRecommendation.getSellSignals();
                        int strongBuySignals = lastRecommendation.getStrongBuySignals();
                        int strongSellSignals = lastRecommendation.getStrongSellSignals();

                        int max = Math.max(Math.max(buySignals, sellSignals), Math.max(strongBuySignals, strongSellSignals));

                        if (max == buySignals)
                            signal = "b";

                        if (max == sellSignals)
                            signal = "s";

                        if (max == strongBuySignals)
                            signal = "sb";

                        if (max == strongSellSignals)
                            signal = "ss";

                        forecastsView.showRecommendationsResult(
                                lastRecommendation.getPeriod(),
                                signal);

                        r = r.subList(0, r.size() / 2);
                        Collections.reverse(r);
                        setChartData(r);

                        emitter.onComplete();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                }));
    }

    public Completable initializeTechAnalysis(String symbol) {
        return Completable.create((emitter) -> stocksRepository.getSymbolTechAnalysis(symbol)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<TechAnalysisResponse.TechnicalAnalysis>() {
                    @Override
                    public void onSuccess(@NonNull TechAnalysisResponse.TechnicalAnalysis object) {
                        forecastsView.showTechAnalysisResult(object.getSignal());
                        emitter.onComplete();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                }));
    }

    public void configureRecommendationBarChart() {
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(0);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setScaleEnabled(false);

        XAxis xl = chart.getXAxis();
        xl.setEnabled(false);

        YAxis yl = chart.getAxisLeft();
        yl.setEnabled(true);

        YAxis yr = chart.getAxisRight();
        yr.setEnabled(true);
        yr.setDrawLabels(false);

        chart.animateY(1000);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setFormSize(8f);
        l.setXEntrySpace(8f);
    }

    public void setChartData(List<Recommendation> recommendations) {
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < recommendations.size(); i++) {
            Log.d(TAG, "setChartData: mopmop");
            int strongSellSignals = recommendations.get(i).getStrongSellSignals();
            int sellSignals = recommendations.get(i).getSellSignals();
            int holdSignals = recommendations.get(i).getHoldSignals();
            int buySignals = recommendations.get(i).getBuySignals();
            int strongBuySignals = recommendations.get(i).getStrongBuySignals();

            int sum = strongSellSignals + sellSignals + holdSignals + buySignals + strongBuySignals;

            String signal = null;
            int max = Math.max(Math.max(buySignals, sellSignals), Math.max(strongBuySignals, strongSellSignals));

            if (max == buySignals)
                signal = "b";

            if (max == sellSignals)
                signal = "s";

            if (max == strongBuySignals)
                signal = "sb";

            if (max == strongSellSignals)
                signal = "ss";

            BarEntry entry = new BarEntry(i, new float[]{strongSellSignals, sellSignals, holdSignals, buySignals, strongBuySignals}, null);
            HashMap<String, String> data = new HashMap<>();
            data.put("signal", signal);
            data.put("period", recommendations.get(i).getPeriod());
            data.put("sum", String.valueOf(sum));
            entry.setData(data);

            values.add(entry);
        }

        BarDataSet dataSet;

        dataSet = new BarDataSet(values, null);
        dataSet.setDrawIcons(false);
        dataSet.setColors(getColors());
        dataSet.setStackLabels(new String[]{"Strong sell", "Sell", "Hold", "Buy", "Strong buy"});
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        BarData data = new BarData(dataSets);
        data.setValueTextColor(Color.WHITE);

        chart.setData(data);
        chart.setFitBars(false);

        forecastsView.buildRecommendationChart(chart);
    }

    public int[] getColors() {
        return new int[]{
                Color.rgb(178, 36, 36),
                Color.rgb(221, 63, 63),
                Color.rgb(244, 202, 45),
                Color.rgb(36, 178, 93),
                Color.rgb(18, 115, 57)
        };
    }
}
