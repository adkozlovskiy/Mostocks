package com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts;

import com.github.mikephil.charting.charts.HorizontalBarChart;

public interface ForecastsView {

    void showTechAnalysisResult(String signal);

    void showRecommendationsResult(String period);

    void buildRecommendationChart(HorizontalBarChart chart);

}
