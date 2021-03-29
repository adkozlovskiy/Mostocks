package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.app.Dialog;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.charts.CandleStickChart;

public interface ChartView {

    void showUpdatedCost(String cost);

    void buildCandlesChart(CandleStickChart chart, int size);

    void showQuoteChange(String pq, int color, Drawable drawable);

    void showTimeStamp(long timestamp);

    void hideTimeStamp();

    void showDialog(Dialog dialog);
}
