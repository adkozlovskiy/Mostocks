package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.utils.QuoteConverter;

import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.KEY_SYMBOL;

public class ChartFragment extends Fragment implements ChartView {

    public static final String TAG = ChartFragment.class.getSimpleName();
    private double currentCost, previousCost;
    private ChartPresenter chartPresenter;
    private CandleStickChart candleChart;
    private TextView tvPrice;
    private Context context;
    private String symbol;

    public ChartFragment() {
        super(R.layout.fragment_chart);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvPrice = view.findViewById(R.id.tv_price);
        tvPrice.setText(QuoteConverter.convertToCurrencyFormat(currentCost, 2, 4));
        candleChart = view.findViewById(R.id.chart_candles);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            symbol = getArguments().getString(KEY_SYMBOL);
            chartPresenter = new ChartPresenter(this, context, symbol);
            chartPresenter.subscribe(symbol);
            Log.d(TAG, "onResume: ");
        }

        chartPresenter.configureCandlesChart(candleChart);
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
        Log.d(TAG, "onStop: ");
    }
}