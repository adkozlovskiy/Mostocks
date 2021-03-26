package com.kozlovskiy.mostocks.ui.stockInfo.fragments.forecasts;

import android.content.Context;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ForecastsFragment extends Fragment implements ForecastsView {

    private Context context;
    private ForecastsPresenter forecastsPresenter;

    private ProgressBar pbTech;
    private ProgressBar pbRec;

    private TextView tvDateTech;
    private TextView tvDateRec;

    private TextView tvSignalTech;
    private TextView tvSignalRec;

    private TextView tvTitleTech;
    private TextView tvTitleRec;

    HorizontalBarChart chart;

    public ForecastsFragment() {
        super(R.layout.fragment_forecasts);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forecastsPresenter = new ForecastsPresenter(this, context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String symbol = getSymbol();

        pbTech = view.findViewById(R.id.pb_tech);
        pbTech.setVisibility(View.VISIBLE);

        pbRec = view.findViewById(R.id.progress_bar_rec); //fixme
        pbRec.setVisibility(View.VISIBLE);

        tvDateTech = view.findViewById(R.id.tv_date_tech);
        tvDateRec = view.findViewById(R.id.tv_date_rec);

        tvSignalTech = view.findViewById(R.id.tv_signal_tech);
        tvSignalRec = view.findViewById(R.id.tv_signal_rec);

        tvTitleTech = view.findViewById(R.id.tv_title_tech);
        tvTitleRec = view.findViewById(R.id.tv_title_rec);

        chart = view.findViewById(R.id.chart);

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(0);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setScaleEnabled(false);

        XAxis xl = chart.getXAxis();
        xl.setEnabled(false);

        YAxis yl = chart.getAxisLeft();
        yl.setEnabled(false);

        YAxis yr = chart.getAxisRight();
        yr.setEnabled(false);

        chart.setFitBars(true);
        chart.animateY(1000);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);

        setData();

        if (symbol != null) {
            forecastsPresenter.initializeTechAnalysis(symbol);
            forecastsPresenter.initializeRecommendation(symbol);
        }
    }

    @Override
    public void showTechAnalysisResult(String signal) {
        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM yyyy HH:mm", Locale.US);
        String date = simpleDateFormat.format(currentDate);

        tvTitleTech.setVisibility(View.VISIBLE);
        pbTech.setVisibility(View.GONE);

        tvDateTech.setText(date);

        tvSignalTech.setVisibility(View.VISIBLE);
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
    public void showRecommendationsResult(String period) {
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat newDateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.US);

        Date date;
        try {
            date = oldDateFormat.parse(period);
            if (date != null) {
                String result = newDateFormat.format(date);
                tvDateRec.setText(result);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvTitleRec.setVisibility(View.VISIBLE);
        pbRec.setVisibility(View.GONE);

        tvSignalRec.setVisibility(View.VISIBLE);
        Drawable drawable = tvSignalTech.getCompoundDrawables()[0];

        /* switch (signal) {
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
        } */
    }

    @Override
    public void buildRecommendationChart(HorizontalBarChart chart) {

    }

    private String getSymbol() {
        if (getArguments() == null) {
            return null;
        }

        return getArguments().getString(StocksAdapter.KEY_SYMBOL);
    }

    private void setData() {
        ArrayList<BarEntry> values = new ArrayList<>();

        float val1 = 4;
        float val2 = 6;
        float val3 = 8;

        values.add(new BarEntry(0,
                new float[]{val1, val2, val3},
                getResources().getDrawable(R.drawable.ic_star_gold)));

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);

            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, null);
            set1.setDrawIcons(false);
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{"Buy", "Hold", "Sell"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextColor(Color.WHITE);

            chart.setData(data);
        }

        chart.setFitBars(true);
        chart.invalidate();
    }

    private int[] getColors() {
        int[] colors = new int[3];
        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3);

        return colors;
    }
}