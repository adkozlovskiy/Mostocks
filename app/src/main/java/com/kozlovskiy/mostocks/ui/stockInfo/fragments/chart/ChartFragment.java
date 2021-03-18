package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.utils.StockCostUtils;

import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.KEY_CURRENT_COST;
import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.KEY_PREVIOUS_COST;
import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.KEY_TICKER;

public class ChartFragment extends Fragment implements ChartView {

    private ChartPresenter chartPresenter;
    private TextView tvPrice;
    private String ticker;
    public static final String TAG = ChartFragment.class.getSimpleName();
    private CandleStickChart candleChart;
    private double currentCost, previousCost;

    public ChartFragment() {
        super(R.layout.fragment_chart);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ticker = getTicker();
        currentCost = getCurrentCost();
        previousCost = getPreviousCost();
        chartPresenter = new ChartPresenter(this, getContext(), ticker);
    }

    @Override
    public void onResume() {
        super.onResume();
        chartPresenter.subscribe(ticker);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvPrice = view.findViewById(R.id.tv_price);
        tvPrice.setText(StockCostUtils.convertCost(currentCost));
        candleChart = view.findViewById(R.id.chart_candles);

        chartPresenter.configureCandlesChart(candleChart);
    }

    private String getTicker() {
        return getArguments().getString(KEY_TICKER);
    }

    private double getCurrentCost() {
        return getArguments().getDouble(KEY_CURRENT_COST);
    }

    private double getPreviousCost() {
        return getArguments().getDouble(KEY_PREVIOUS_COST);
    }

    @Override
    public void showUpdatedCost(String cost) {
        tvPrice.setText(cost);
    }

    @Override
    public void buildCandlesChart(CandleStickChart chart) {

    }

    @Override
    public void showInitValues(double currentCost) {

    }

    @Override
    public void onStop() {
        super.onStop();
        chartPresenter.unsubscribe();
    }
}