package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.KEY_CURRENT_COST;
import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.KEY_PREVIOUS_COST;
import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.KEY_SYMBOL;

public class ChartFragment extends Fragment implements ChartView {

    public static final String TAG = ChartFragment.class.getSimpleName();
    private ChartPresenter chartPresenter;
    private CandleStickChart candleChart;
    private TextView tvPrice;
    private TextView tvPriceChange;
    private Context context;

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
        tvPriceChange = view.findViewById(R.id.tv_price_change);
        candleChart = view.findViewById(R.id.chart_candles);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            String symbol = getArguments().getString(KEY_SYMBOL);
            chartPresenter = new ChartPresenter(this, context, symbol);
            chartPresenter.subscribe(symbol);

            double currentCost = getArguments().getDouble(KEY_CURRENT_COST);
            double previousCost = getArguments().getDouble(KEY_PREVIOUS_COST);

            tvPrice.setText(QuoteConverter.convertToCurrencyFormat(currentCost, 1, 2));
            chartPresenter.calculateQuoteChange(currentCost, previousCost);
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
    public void showQuoteChange(String pq, int color, Drawable drawable) {
        tvPriceChange.setText(pq);
        tvPriceChange.setTextColor(color);
        tvPriceChange.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    @Override
    public void onStop() {
        super.onStop();
        chartPresenter.unsubscribe();
        Log.d(TAG, "onStop: ");
    }
}