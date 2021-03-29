package com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts;

import android.app.Dialog;
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
import com.github.mikephil.charting.data.BarEntry;
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

import static com.kozlovskiy.mostocks.utils.Converter.toDefaultFormat;
import static com.kozlovskiy.mostocks.utils.Converter.toPercentFormat;

public class ForecastsFragment extends Fragment
        implements ForecastsView {

    public static final String TAG = ForecastsFragment.class.getSimpleName();
    private ForecastsPresenter forecastsPresenter;

    private ProgressBar pbChart;
    private Context context;

    private CardView cvTech;
    private CardView cvRec;

    private TextView tvDateTech;
    private TextView tvDateRec;
    private TextView tvSignalTech;
    private TextView tvSignalRec;
    private TextView tvSignals;

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
        pbChart = view.findViewById(R.id.pb_chart);
        pbChart.setVisibility(View.VISIBLE);

        tvDateTech = view.findViewById(R.id.tv_date_tech);
        tvDateRec = view.findViewById(R.id.tv_date_rec);

        tvSignalTech = view.findViewById(R.id.tv_signal_tech);
        tvSignalRec = view.findViewById(R.id.tv_signal_rec);
        tvSignals = view.findViewById(R.id.tv_signals);

        cvRec = view.findViewById(R.id.cv_rec);
        cvTech = view.findViewById(R.id.cv_tech);

        chart = view.findViewById(R.id.chart);
        forecastsPresenter.setChart(chart);

        forecastsPresenter.initializeData(getSymbol());
    }

    @Override
    public void showTechAnalysisResult(String signal) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM yyyy HH:mm", Locale.US);
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
                tvSignals.setVisibility(View.VISIBLE);
                if (e.getData() instanceof HashMap) {
                    BarEntry bar = (BarEntry) e;

                    int index = h.getStackIndex();
                    float val = bar.getYVals()[index];
                    String signalsCount = toDefaultFormat(val, 0, 0);

                    HashMap<String, String> data = (HashMap<String, String>) e.getData();
                    String period = data.getOrDefault("period", "1970-01-01");
                    String signal = data.getOrDefault("signal", "hold");
                    String sum = data.getOrDefault("sum", "N/A");

                    String percent = toPercentFormat(val / Integer.parseInt(sum) * 100, 0, 2);
                    String res = "";
                    switch (index) {
                        case 0:
                            res += getResources().getString(R.string.strong_sell_signals);
                            break;

                        case 1:
                            res += getResources().getString(R.string.sell_signals);
                            break;

                        case 2:
                            res += getResources().getString(R.string.hold_signals);
                            break;

                        case 3:
                            res += getResources().getString(R.string.buy_signals);
                            break;

                        case 4:
                            res += getResources().getString(R.string.strong_buy_signals);
                            break;
                    }

                    String holder = signalsCount + "/" + sum + " (" + percent + ")";
                    res += " " + holder;

                    tvSignals.setText(res);

                    showRecommendationsResult(period, signal);
                }
            }

            @Override
            public void onNothingSelected() {
                tvSignals.setVisibility(View.GONE);
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

    @Override
    public void showDialog(Dialog dialog) {
        dialog.show();
    }

    private String getSymbol() {
        if (getArguments() == null) {
            return null;
        }

        return getArguments().getString(StocksAdapter.KEY_SYMBOL);
    }
}