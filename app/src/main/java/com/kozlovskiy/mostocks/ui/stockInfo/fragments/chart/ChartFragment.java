package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter;

public class ChartFragment extends Fragment implements ChartView {

    private ChartPresenter chartPresenter;
    private TextView tvPrice;
    private String ticker;
    public static final String TAG = ChartFragment.class.getSimpleName();
    
    public ChartFragment() {
        super(R.layout.fragment_chart);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chartPresenter = new ChartPresenter(this, getContext());
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
        ticker = getTicker();
    }

    private String getTicker() {
        return getArguments().getString(StocksAdapter.KEY_TICKER);
    }

    @Override
    public void showUpdatedCost(String cost) {
        tvPrice.setText(cost);
    }

    @Override
    public void onStop() {
        super.onStop();
        chartPresenter.unsubscribe();
    }
}