package com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter;

import java.util.ArrayList;
import java.util.List;

public class ForecastsFragment extends Fragment implements ForecastsView {

    private ForecastsPresenter forecastsPresenter;
    private BarChart stackedDataChart;
    public static final String TAG = ForecastsPresenter.TAG;

    public ForecastsFragment() {
        super(R.layout.fragment_forecasts);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forecastsPresenter = new ForecastsPresenter(this, getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String ticker = getTicker();

        Log.d(TAG, "onViewCreated: " + ticker);
        if (ticker != null) {
            forecastsPresenter.initializeGraphData(ticker);
        }

        stackedDataChart = view.findViewById(R.id.bar_chart);
    }

    @Override
    public void showGraph(ArrayList<BarEntry> entries) {
        int[] colors = new int[]{Color.BLUE, Color.YELLOW, Color.RED};
        BarDataSet barDataSet = new BarDataSet(entries, "");
        barDataSet.setColors(colors);

        BarData barData = new BarData(barDataSet);
        stackedDataChart.setData(barData);

        XAxis xAxis = stackedDataChart.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);

        YAxis yAxis = stackedDataChart.getAxisLeft();
        yAxis.setEnabled(false);
        yAxis.setDrawLabels(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);

        Legend legend = stackedDataChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);

        List<LegendEntry> legendEntries = new ArrayList<>();
        legendEntries.add(new LegendEntry("buy", Legend.LegendForm.CIRCLE, 6f, 20f, null, Color.BLUE));
        legendEntries.add(new LegendEntry("hold", Legend.LegendForm.CIRCLE, 6f, 20f, null, Color.YELLOW));
        legendEntries.add(new LegendEntry("sell", Legend.LegendForm.CIRCLE, 6f, 20f, null, Color.RED));
        legend.setEntries(legendEntries);
    }

    private String getTicker() {
        return getArguments().getString(StocksAdapter.KEY_TICKER);
    }
}