package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.charts.CandleStickChart;

public interface ChartView {

    void showUpdatedCost(String cost);

    void buildCandlesChart(CandleStickChart chart);

    void showQuoteChange(String pq, int color, Drawable drawable);
}
