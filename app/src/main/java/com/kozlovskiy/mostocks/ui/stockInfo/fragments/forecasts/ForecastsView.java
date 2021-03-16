package com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts;

import java.util.ArrayList;

import im.dacer.androidcharts.PieHelper;

public interface ForecastsView {

    void showGraph(ArrayList<PieHelper> entries, int selected);

    void showForecastStats(int buy, int hold, int sell, String signal);
}
