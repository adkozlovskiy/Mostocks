package com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public interface ForecastsView {

    void showGraph(ArrayList<BarEntry> entries);

}
