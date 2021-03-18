package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import com.github.mikephil.charting.charts.CandleStickChart;

public interface ChartView {

    void showUpdatedCost(String cost);

    void buildCandlesChart(CandleStickChart chart);

    void showInitValues(double currentCost);
}
