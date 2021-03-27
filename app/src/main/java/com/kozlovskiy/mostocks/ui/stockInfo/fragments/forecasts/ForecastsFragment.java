package com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ForecastsFragment extends Fragment implements ForecastsView {

    public static final String TAG = ForecastsFragment.class.getSimpleName();
    private Context context;
    private ForecastsPresenter forecastsPresenter;

    private ProgressBar pbChart;

    private CardView cvTech;
    private CardView cvRec;

    private TextView tvDateTech;
    private TextView tvDateRec;
    private TextView tvSignalTech;
    private TextView tvSignalRec;

    private BarChart chart;

    public ForecastsFragment() {
        super(R.layout.fragment_forecasts);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        forecastsPresenter = new ForecastsPresenter(this, context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String symbol = getSymbol();

        pbChart = view.findViewById(R.id.pb_chart);
        pbChart.setVisibility(View.VISIBLE);

        tvDateTech = view.findViewById(R.id.tv_date_tech);
        tvDateRec = view.findViewById(R.id.tv_date_rec);

        tvSignalTech = view.findViewById(R.id.tv_signal_tech);
        tvSignalRec = view.findViewById(R.id.tv_signal_rec);

        cvRec = view.findViewById(R.id.cv_rec);
        cvTech = view.findViewById(R.id.cv_tech);

        chart = view.findViewById(R.id.chart);
        forecastsPresenter.setChart(chart);

        if (symbol != null) {
            forecastsPresenter.initializeData(symbol);
        }
    }

    @Override
    public void showTechAnalysisResult(String signal) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.US);
        String result = "on " + simpleDateFormat.format(date);

        tvDateTech.setText(result);
        Drawable drawable = tvSignalTech.getCompoundDrawables()[0];

        switch (signal) {
            case "buy":
                tvSignalTech.setText(getString(R.string.need_to_buy));
                tvSignalTech.setTextColor(getResources().getColor(R.color.buyIndicatorColor, context.getTheme()));
                drawable.setColorFilter(new PorterDuffColorFilter(
                                ContextCompat.getColor(context, R.color.buyIndicatorColor), PorterDuff.Mode.SRC_IN
                        )
                );
                break;

            case "sell":
                tvSignalTech.setText(getString(R.string.need_to_sell));
                tvSignalTech.setTextColor(getResources().getColor(R.color.sellIndicatorColor, context.getTheme()));
                drawable.setColorFilter(new PorterDuffColorFilter(
                                ContextCompat.getColor(context, R.color.sellIndicatorColor), PorterDuff.Mode.SRC_IN
                        )
                );
                break;
        }
    }

    @Override
    public void showRecommendationsResult(String period, String signal) {
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat newDateFormat = new SimpleDateFormat("MMMM yyyy", Locale.US);

        try {
            Date date = oldDateFormat.parse(period);
            if (date != null) {
                String result = "on " + newDateFormat.format(date);
                tvDateRec.setText(result);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Drawable drawable = tvSignalRec.getCompoundDrawables()[0];

        switch (signal) {
            case "b":
                tvSignalRec.setText(getString(R.string.need_to_buy));
                tvSignalRec.setTextColor(getResources().getColor(R.color.buyIndicatorColor, context.getTheme()));
                drawable.setColorFilter(new PorterDuffColorFilter(
                                ContextCompat.getColor(context, R.color.buyIndicatorColor), PorterDuff.Mode.SRC_IN
                        )
                );
                break;

            case "s":
                tvSignalRec.setText(getString(R.string.need_to_sell));
                tvSignalRec.setTextColor(getResources().getColor(R.color.sellIndicatorColor, context.getTheme()));
                drawable.setColorFilter(new PorterDuffColorFilter(
                                ContextCompat.getColor(context, R.color.sellIndicatorColor), PorterDuff.Mode.SRC_IN
                        )
                );
                break;

            case "sb":
                tvSignalRec.setText(getString(R.string.strong_buy));
                tvSignalRec.setTextColor(getResources().getColor(R.color.strongBuyIndicatorColor, context.getTheme()));
                drawable.setColorFilter(new PorterDuffColorFilter(
                                ContextCompat.getColor(context, R.color.strongBuyIndicatorColor), PorterDuff.Mode.SRC_IN
                        )
                );
                break;

            case "ss":
                tvSignalRec.setText(getString(R.string.strong_sell));
                tvSignalRec.setTextColor(getResources().getColor(R.color.strongSellIndicatorColor, context.getTheme()));
                drawable.setColorFilter(new PorterDuffColorFilter(
                                ContextCompat.getColor(context, R.color.strongSellIndicatorColor), PorterDuff.Mode.SRC_IN
                        )
                );
                break;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void buildRecommendationChart(BarChart chart) {
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e.getData() instanceof HashMap) {
                    HashMap<String, String> data = (HashMap<String, String>) e.getData();
                    String period = data.getOrDefault("period", "1970-01-01");
                    String signal = data.getOrDefault("signal", "hold");
                    showRecommendationsResult(period, signal);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    @Override
    public void setViewsVisibility() {
        pbChart.setVisibility(View.GONE);

        cvTech.setVisibility(View.VISIBLE);
        cvRec.setVisibility(View.VISIBLE);

        chart.setVisibility(View.VISIBLE);
    }

    private String getSymbol() {
        if (getArguments() == null) {
            return null;
        }

        return getArguments().getString(StocksAdapter.KEY_SYMBOL);
    }
}