package com.kozlovskiy.mostocks.ui.stockInfo.fragments.chart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
    private Context context;

    private final Date date = new Date();
    private final long timestamp = date.getTime();
    private final List<CardView> cardViews = new ArrayList<>();

    private CardView cvThirties;
    private CardView cvHour;
    private CardView cvDay;
    private CardView cvWeek;
    private CardView cvMonth;
    private CardView cvSelected;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            String symbol = getArguments().getString(KEY_SYMBOL);

            double currentCost = getArguments().getDouble(KEY_CURRENT_COST);
            double previousCost = getArguments().getDouble(KEY_PREVIOUS_COST);
            chartPresenter = new ChartPresenter(this, candleChart, context, symbol, previousCost);
            chartPresenter.subscribe();

            tvPrice.setText(QuoteConverter.toCurrencyFormat(currentCost, 0, 2));
            chartPresenter.calculateQuoteChange(currentCost, previousCost);
        }

        long MILLIS_IN_SECOND = 1000;
        long MILLIS_IN_MINUTE = 60 * MILLIS_IN_SECOND;
        long MILLIS_IN_HOUR = 60 * MILLIS_IN_MINUTE;

        chartPresenter.initializeCandles((timestamp - 60 * 24 * MILLIS_IN_HOUR) / MILLIS_IN_SECOND, timestamp / MILLIS_IN_SECOND, "D");
        selectCard(cvDay);
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
            from = 90 * MILLIS_IN_HOUR;
            resolution = "30";

        } else if (id == R.id.cv_hour) {
            selectCard(cvHour);
            from = 120 * MILLIS_IN_HOUR;
            resolution = "60";

        } else if (id == R.id.cv_day) {
            selectCard(cvDay);
            from = 24 * MILLIS_IN_HOUR * 60;
            resolution = "D";

        } else if (id == R.id.cv_week) {
            selectCard(cvWeek);
            from = 24 * MILLIS_IN_HOUR * 60 * 7;
            resolution = "W";

        } else if (id == R.id.cv_month) {
            selectCard(cvMonth);
            from = 24 * MILLIS_IN_HOUR * 60 * 7 * 5;
            resolution = "M";
        }

        chartPresenter.initializeCandles((timestamp - from) / MILLIS_IN_SECOND, to, resolution);
    };

    private void selectCard(CardView card) {
        cvSelected = card;
        cvSelected.setCardBackgroundColor(context.getResources().getColor(R.color.colorChartButtonBackgroundAccent, context.getTheme()));

        for (CardView cardView : cardViews) {
            if (cardView != cvSelected) {
                cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorChartButtonBackground, context.getTheme()));
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