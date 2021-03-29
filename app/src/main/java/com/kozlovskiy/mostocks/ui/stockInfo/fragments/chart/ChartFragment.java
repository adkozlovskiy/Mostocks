package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.utils.Converter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.kozlovskiy.mostocks.services.WebSocketService.ACTION_NEW_MESSAGE;
import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.KEY_CURRENT_COST;
import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.KEY_PREVIOUS_COST;
import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.KEY_SYMBOL;

public class ChartFragment extends Fragment
        implements ChartView {

    private final QuoteReceiver quoteReceiver = new QuoteReceiver();

    public static final long HALF_HOUR_PERIOD = TimeUnit.HOURS.toMillis(90);
    public static final long HOUR_PERIOD = TimeUnit.HOURS.toMillis(180);
    public static final long DAY_PERIOD = TimeUnit.DAYS.toMillis(60);
    public static final long WEEK_PERIOD = TimeUnit.DAYS.toMillis(60 * 7);
    public static final long MONTH_PERIOD = TimeUnit.DAYS.toMillis(60 * 30);

    public static final String TAG = ChartFragment.class.getSimpleName();
    public static final String HALF_HOUR_RESOLUTION = "30";
    public static final String HOUR_RESOLUTION = "60";
    public static final String DAY_RESOLUTION = "D";
    public static final String WEEK_RESOLUTION = "W";
    public static final String MONTH_RESOLUTION = "M";

    public static final long MILLIS_IN_SECOND = 1000;

    private final SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMMM yyyy", Locale.US);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.US);
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);

    private final List<CardView> navigationCards = new ArrayList<>(5);
    private final Date date = new Date();
    private final long timestamp = date.getTime();

    private ChartPresenter chartPresenter;

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

    private double previousQuote;

    public ChartFragment() {
        super(R.layout.fragment_chart);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

        context.registerReceiver(quoteReceiver, new IntentFilter(ACTION_NEW_MESSAGE));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvPrice = view.findViewById(R.id.tv_price);
        tvPriceChange = view.findViewById(R.id.tv_price_change);
        candleChart = view.findViewById(R.id.chart_candles);
        tvDatestamp = view.findViewById(R.id.tv_datestamp);
        tvTimestamp = view.findViewById(R.id.tv_timestamp);
        progressBar = view.findViewById(R.id.progress_bar);

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

        previousQuote = getPreviousQuote();
        chartPresenter = new ChartPresenter(this, candleChart, context, getSymbol());
        chartPresenter.calculateQuoteChange(currentQuote, previousQuote);

        initializeCandles((timestamp - DAY_PERIOD) / MILLIS_IN_SECOND, timestamp / MILLIS_IN_SECOND, "D");
        selectCard(cvDay);
    }

    @Override
    public void onResume() {
        super.onResume();
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

    @Override
    public void showDialog(Dialog dialog) {
        dialog.show();
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
        context.unregisterReceiver(quoteReceiver);
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

    private final class QuoteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String symbol = intent.getStringExtra("symbol");

            if (symbol.equals(getSymbol())) {
                double quote = intent.getDoubleExtra("quote", 0);
                chartPresenter.calculateQuoteChange(quote, previousQuote);
                showUpdatedCost(Converter.toCurrencyFormat(quote, 0, 2));
            }
        }
    }
}