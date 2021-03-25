package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.utils.QuoteConverter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.KEY_CURRENT_COST;
import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.KEY_PREVIOUS_COST;
import static com.kozlovskiy.mostocks.ui.main.adapter.StocksAdapter.KEY_SYMBOL;

public class ChartFragment extends Fragment implements ChartView {

    public static final String TAG = ChartFragment.class.getSimpleName();
    public static final String HALF_HOUR_RESOLUTION = "30";
    public static final String HOUR_RESOLUTION = "60";
    public static final String DAY_RESOLUTION = "D";
    public static final String WEEK_RESOLUTION = "W";
    public static final String MONTH_RESOLUTION = "M";

    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
    private final SimpleDateFormat timeFormat =
            new SimpleDateFormat("HH:mm", Locale.getDefault());
    private ChartPresenter chartPresenter;
    private CandleStickChart candleChart;
    private TextView tvPrice;
    private TextView tvPriceChange;
    private TextView tvDatestamp;
    private TextView tvTimestamp;
    private ProgressBar progressBar;
    private Context context;

    private final Date date = new Date();
    private final long timestamp = date.getTime();
    private final List<CardView> cardViews = new ArrayList<>();

    private boolean candlesChartSelected = false;
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
        candleChart = view.findViewById(R.id.chart_candles);
        tvDatestamp = view.findViewById(R.id.tv_datestamp);
        tvTimestamp = view.findViewById(R.id.tv_timestamp);
        progressBar = view.findViewById(R.id.progress_bar);

        CardView cvChartButton = view.findViewById(R.id.chart_button);
        ivChartButton = view.findViewById(R.id.chart_icon);

        cvChartButton.setOnClickListener(v -> {
            if (candlesChartSelected) {
                ivChartButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_line_chart, context.getTheme()));

            } else {
                ivChartButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_candles_chart, context.getTheme()));
            }

            candlesChartSelected = !candlesChartSelected;
        });

        cvThirties = view.findViewById(R.id.cv_thirties);
        cvThirties.setOnClickListener(cardClickListener);
        cardViews.add(cvThirties);

        cvHour = view.findViewById(R.id.cv_hour);
        cvHour.setOnClickListener(cardClickListener);
        cardViews.add(cvHour);

        cvDay = view.findViewById(R.id.cv_day);
        cvDay.setOnClickListener(cardClickListener);
        cardViews.add(cvDay);

        cvWeek = view.findViewById(R.id.cv_week);
        cvWeek.setOnClickListener(cardClickListener);
        cardViews.add(cvWeek);

        cvMonth = view.findViewById(R.id.cv_month);
        cvMonth.setOnClickListener(cardClickListener);
        cardViews.add(cvMonth);

        if (getArguments() != null) {
            String symbol = getArguments().getString(KEY_SYMBOL);

            double currentCost = getArguments().getDouble(KEY_CURRENT_COST);
            double previousCost = getArguments().getDouble(KEY_PREVIOUS_COST);
            chartPresenter = new ChartPresenter(this, candleChart, context, symbol, previousCost);

            tvPrice.setText(QuoteConverter.toCurrencyFormat(currentCost, 0, 2));
            chartPresenter.calculateQuoteChange(currentCost, previousCost);
        }

        long MILLIS_IN_SECOND = 1000;
        long MILLIS_IN_MINUTE = 60 * MILLIS_IN_SECOND;
        long MILLIS_IN_HOUR = 60 * MILLIS_IN_MINUTE;

        initializeCandles((timestamp - 60 * 24 * MILLIS_IN_HOUR) / MILLIS_IN_SECOND, timestamp / MILLIS_IN_SECOND, "D");
        selectCard(cvDay);
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
    public void buildCandlesChart(CandleStickChart chart) {
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
        String dateString = dateFormat.format(date);
        String timeString = timeFormat.format(date);
        tvDatestamp.setText(dateString);

        if (cvSelected == cvHour | cvSelected == cvThirties) {
            tvTimestamp.setText(timeString);
        }
    }

    View.OnClickListener cardClickListener = v -> {
        int id = v.getId();
        long MILLIS_IN_SECOND = 1000;
        long MILLIS_IN_MINUTE = 60 * MILLIS_IN_SECOND;
        long MILLIS_IN_HOUR = 60 * MILLIS_IN_MINUTE;
        long from = 0;
        long to = timestamp / MILLIS_IN_SECOND;
        String resolution = null;

        if (id == R.id.cv_thirties) {
            selectCard(cvThirties);
            from = 70 * MILLIS_IN_HOUR;
            resolution = HALF_HOUR_RESOLUTION;

        } else if (id == R.id.cv_hour) {
            selectCard(cvHour);
            from = 140 * MILLIS_IN_HOUR;
            resolution = HOUR_RESOLUTION;

        } else if (id == R.id.cv_day) {
            selectCard(cvDay);
            from = 24 * MILLIS_IN_HOUR * 90;
            resolution = DAY_RESOLUTION;

        } else if (id == R.id.cv_week) {
            selectCard(cvWeek);
            from = 24 * MILLIS_IN_HOUR * 60 * 7;
            resolution = WEEK_RESOLUTION;

        } else if (id == R.id.cv_month) {
            selectCard(cvMonth);
            from = 24 * MILLIS_IN_HOUR * 60 * 7 * 5;
            resolution = MONTH_RESOLUTION;
        }

        initializeCandles((timestamp - from) / MILLIS_IN_SECOND, to, resolution);
    };

    private void initializeCandles(long from, long to, String resolution) {
        progressBar.setVisibility(View.VISIBLE);
        candleChart.setVisibility(View.GONE);
        chartPresenter.initializeCandles(from, to, resolution);
    }

    private void selectCard(CardView card) {
        cvSelected = card;
        cvSelected.setCardBackgroundColor(context.getResources()
                .getColor(R.color.colorChartButtonBackgroundAccent, context.getTheme()));

        tvTimestamp.setText("");
        tvDatestamp.setText("");

        for (CardView cardView : cardViews) {
            if (cardView != cvSelected) {
                cardView.setCardBackgroundColor(context.getResources()
                        .getColor(R.color.colorChartButtonBackground, context.getTheme()));
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        chartPresenter.unsubscribe();
        Log.d(TAG, "onStop: ");
    }
}