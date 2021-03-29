package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.utils.Converter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.KEY_CURRENT_COST;
import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.KEY_PREVIOUS_COST;
import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.KEY_SYMBOL;

public class ChartFragment extends Fragment
        implements ChartView {

    public static final String TAG = ChartFragment.class.getSimpleName();
    public static final String HALF_HOUR_RESOLUTION = "30";
    public static final long HALF_HOUR_PERIOD = TimeUnit.HOURS.toMillis(90);
    public static final String HOUR_RESOLUTION = "60";
    public static final long HOUR_PERIOD = TimeUnit.HOURS.toMillis(180);
    public static final String DAY_RESOLUTION = "D";
    public static final long DAY_PERIOD = TimeUnit.DAYS.toMillis(60);
    public static final String WEEK_RESOLUTION = "W";
    public static final long WEEK_PERIOD = TimeUnit.DAYS.toMillis(60 * 7);
    public static final String MONTH_RESOLUTION = "M";
    public static final long MONTH_PERIOD = TimeUnit.DAYS.toMillis(60 * 30);

    public static final long MILLIS_IN_SECOND = 1000;

    private final SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMMM yyyy", Locale.US);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.US);
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);

    private final List<CardView> navigationCards = new ArrayList<>(5);
    private final Date date = new Date();
    private final long timestamp = date.getTime();

    private ChartPresenter chartPresenter;

    private LineChart lineChart;
    private CandleStickChart candleChart;
    private TextView tvPrice;
    private TextView tvPriceChange;
    private TextView tvDatestamp;
    private TextView tvTimestamp;
    private ProgressBar progressBar;
    private Context context;

    private CardView cvThirties;
    private CardView cvHour;
    private CardView cvDay;
    private CardView cvWeek;
    private CardView cvMonth;
    private CardView cvSelected;

    private ImageView ivChartButton;

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
        lineChart = view.findViewById(R.id.chart_lines);
        candleChart = view.findViewById(R.id.chart_candles);
        tvDatestamp = view.findViewById(R.id.tv_datestamp);
        tvTimestamp = view.findViewById(R.id.tv_timestamp);
        progressBar = view.findViewById(R.id.progress_bar);

        CardView cvChartButton = view.findViewById(R.id.chart_button);
        ivChartButton = view.findViewById(R.id.chart_icon);

        cvThirties = view.findViewById(R.id.cv_thirties);
        cvThirties.setOnClickListener(cardClickListener);
        navigationCards.add(cvThirties);

        cvHour = view.findViewById(R.id.cv_hour);
        cvHour.setOnClickListener(cardClickListener);
        navigationCards.add(cvHour);

        cvDay = view.findViewById(R.id.cv_day);
        cvDay.setOnClickListener(cardClickListener);
        navigationCards.add(cvDay);

        cvWeek = view.findViewById(R.id.cv_week);
        cvWeek.setOnClickListener(cardClickListener);
        navigationCards.add(cvWeek);

        cvMonth = view.findViewById(R.id.cv_month);
        cvMonth.setOnClickListener(cardClickListener);
        navigationCards.add(cvMonth);

        double currentQuote = getCurrentQuote();
        tvPrice.setText(Converter.toCurrencyFormat(currentQuote, 0, 2));

        double previousQuote = getPreviousQuote();
        chartPresenter = new ChartPresenter(this, candleChart, lineChart, context, getSymbol(), previousQuote);
        chartPresenter.calculateQuoteChange(currentQuote, previousQuote);

        initializeCandles((timestamp - DAY_PERIOD) / MILLIS_IN_SECOND, timestamp / MILLIS_IN_SECOND, "D");
        selectCard(cvDay);

        cvChartButton.setOnClickListener(v -> {
            if (chartPresenter.isCandlesChartSelected()) {
                ivChartButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_line_chart, context.getTheme()));


            } else {
                ivChartButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_candles_chart, context.getTheme()));
            }

            chartPresenter.setCandlesChartSelected(!chartPresenter.isCandlesChartSelected());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        chartPresenter.subscribe();
    }

    @Override
    public void showUpdatedCost(String cost) {
        try {
            tvPrice.setText(cost);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void buildCandlesChart(CandleStickChart chart, int size) {
        candleChart.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        chart.invalidate();
    }

    @Override
    public void buildLinesChart(LineChart chart, int size) {
        lineChart.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        chart.invalidate();
    }

    @Override
    public void showQuoteChange(String pq, int color, Drawable drawable) {
        try {
            tvPriceChange.setText(pq);
            tvPriceChange.setTextColor(color);
            tvPriceChange.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void showTimeStamp(long timestamp) {
        Date date = new Date(timestamp);

        String dateString;
        if (cvSelected == cvMonth)
            dateString = monthDateFormat.format(date);

        else dateString = dateFormat.format(date);

        String timeString = timeFormat.format(date);
        tvDatestamp.setText(dateString);

        if (cvSelected == cvHour | cvSelected == cvThirties) {
            tvTimestamp.setText(timeString);
        }
    }

    @Override
    public void hideTimeStamp() {
        tvTimestamp.setText("");
        tvDatestamp.setText("");
    }

    View.OnClickListener cardClickListener = v -> {
        if (v == cvSelected)
            return;

        long to = timestamp / MILLIS_IN_SECOND;
        long period = HALF_HOUR_PERIOD;
        String resolution = HALF_HOUR_RESOLUTION;

        int id = v.getId();
        if (id == R.id.cv_hour) {
            selectCard(cvHour);
            period = HOUR_PERIOD;
            resolution = HOUR_RESOLUTION;

        } else if (id == R.id.cv_day) {
            selectCard(cvDay);
            period = DAY_PERIOD;
            resolution = DAY_RESOLUTION;

        } else if (id == R.id.cv_week) {
            selectCard(cvWeek);
            period = WEEK_PERIOD;
            resolution = WEEK_RESOLUTION;

        } else if (id == R.id.cv_month) {
            selectCard(cvMonth);
            period = MONTH_PERIOD;
            resolution = MONTH_RESOLUTION;

        } else selectCard(cvThirties);

        initializeCandles((timestamp - period) / MILLIS_IN_SECOND, to, resolution);
    };

    private void initializeCandles(long from, long to, String resolution) {
        progressBar.setVisibility(View.VISIBLE);
        candleChart.setVisibility(View.GONE);
        candleChart.highlightValue(null);
        chartPresenter.initializeCandles(from, to, resolution);
    }

    private void selectCard(CardView card) {
        cvSelected = card;
        cvSelected.setCardBackgroundColor(context.getResources()
                .getColor(R.color.colorChartButtonBackgroundAccent, context.getTheme()));

        for (CardView cardView : navigationCards) {
            if (cardView != cvSelected) {
                cardView.setCardBackgroundColor(context.getResources()
                        .getColor(R.color.colorChartButtonBackground, context.getTheme()));
            }
        }

        hideTimeStamp();
    }

    @Override
    public void onStop() {
        super.onStop();
        chartPresenter.unsubscribe();
    }

    private String getSymbol() {
        return getArguments() == null ? null
                : getArguments().getString(KEY_SYMBOL);
    }

    private Double getCurrentQuote() {
        return getArguments() == null ? null
                : getArguments().getDouble(KEY_CURRENT_COST);
    }

    private Double getPreviousQuote() {
        return getArguments() == null ? null
                : getArguments().getDouble(KEY_PREVIOUS_COST);
    }
}