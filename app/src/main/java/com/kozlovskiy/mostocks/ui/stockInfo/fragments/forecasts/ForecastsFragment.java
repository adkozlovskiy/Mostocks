package com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter;

import java.util.ArrayList;

import im.dacer.androidcharts.PieHelper;
import im.dacer.androidcharts.PieView;

public class ForecastsFragment extends Fragment implements ForecastsView {

    private ForecastsPresenter forecastsPresenter;
    private PieView stackedDataChart;
    private ProgressBar progressBar;

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

        if (ticker != null) {
            forecastsPresenter.initializeGraphData(ticker);
        }

        stackedDataChart = view.findViewById(R.id.pie_view);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    @Override
    public void showGraph(ArrayList<PieHelper> entries) {
        stackedDataChart.setDate(entries);
        progressBar.setVisibility(View.GONE);

    }

    private String getTicker() {
        if (getArguments() == null) {
            return null;
        }

        return getArguments().getString(StocksAdapter.KEY_TICKER);
    }
}