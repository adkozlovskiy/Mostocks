package com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts;

import com.github.mikephil.charting.charts.BarChart;

public interface ForecastsView {

    void showTechAnalysisResult(String signal);

    void showRecommendationsResult(String period, String symbol);

    void buildRecommendationChart(BarChart chart);

    void setViewsVisibility();

}
