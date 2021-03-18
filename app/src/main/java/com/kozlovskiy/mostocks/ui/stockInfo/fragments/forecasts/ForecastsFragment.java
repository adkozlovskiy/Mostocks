package com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import im.dacer.androidcharts.PieHelper;
import im.dacer.androidcharts.PieView;

public class ForecastsFragment extends Fragment implements ForecastsView {

    private ForecastsPresenter forecastsPresenter;
    private PieView stackedDataChart;
    private ProgressBar progressBar;
    private TextView tvBuy;
    private TextView tvHold;
    private TextView tvSell;
    private TextView tvDate;
    private TextView tvSignal;
    private TextView tvSellHolder;
    private TextView tvHoldHolder;
    private TextView tvBuyHolder;
    private TextView tvTitle;
    private TextView tvSignals;

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
        tvBuy = view.findViewById(R.id.tv_buy);
        tvHold = view.findViewById(R.id.tv_hold);
        tvSell = view.findViewById(R.id.tv_sell);
        tvDate = view.findViewById(R.id.tv_date);
        tvSignal = view.findViewById(R.id.tv_signal);
        tvSellHolder = view.findViewById(R.id.tv_sell_holder);
        tvHoldHolder = view.findViewById(R.id.tv_hold_holder);
        tvBuyHolder = view.findViewById(R.id.tv_buy_holder);
        tvTitle = view.findViewById(R.id.tv_title);
        tvSignals = view.findViewById(R.id.tv_signals);
    }

    @Override
    public void showGraph(ArrayList<PieHelper> entries, int selected) {
        stackedDataChart.setDate(entries);
        stackedDataChart.selectedPie(selected);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showForecastStats(int buy, int hold, int sell, String signal) {
        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm", Locale.getDefault());
        String date = simpleDateFormat.format(currentDate);

        tvTitle.setVisibility(View.VISIBLE);
        tvSignals.setVisibility(View.VISIBLE);

        tvDate.setText(date);
        tvDate.setVisibility(View.VISIBLE);

        tvBuy.setText(String.valueOf(buy));
        tvBuy.setVisibility(View.VISIBLE);
        tvBuyHolder.setVisibility(View.VISIBLE);

        tvHold.setText(String.valueOf(hold));
        tvHold.setVisibility(View.VISIBLE);
        tvHoldHolder.setVisibility(View.VISIBLE);

        tvSell.setText(String.valueOf(sell));
        tvSell.setVisibility(View.VISIBLE);
        tvSellHolder.setVisibility(View.VISIBLE);

        tvSignal.setVisibility(View.VISIBLE);
        Drawable drawable = tvSignal.getCompoundDrawables()[0];

        switch (signal) {
            case "buy":
                tvSignal.setText(getString(R.string.need_to_buy));
                tvSignal.setTextColor(getResources().getColor(R.color.buyIndicatorColor));
                drawable.setColorFilter(new PorterDuffColorFilter(
                                ContextCompat.getColor(tvSignal.getContext(), R.color.buyIndicatorColor), PorterDuff.Mode.SRC_IN
                        )
                );
                break;

            case "sell":
                tvSignal.setText(getString(R.string.need_to_sell));
                tvSignal.setTextColor(getResources().getColor(R.color.sellIndicatorColor));
                drawable.setColorFilter(new PorterDuffColorFilter(
                                ContextCompat.getColor(tvSignal.getContext(), R.color.sellIndicatorColor), PorterDuff.Mode.SRC_IN
                        )
                );
                break;
        }
    }

    private String getTicker() {
        if (getArguments() == null) {
            return null;
        }

        return getArguments().getString(StocksAdapter.KEY_TICKER);
    }
}