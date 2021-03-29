package com.kozlovskiy.mostocks.models.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.kozlovskiy.mostocks.R;

import static com.kozlovskiy.mostocks.utils.Converter.toCurrencyFormat;

@SuppressLint("ViewConstructor")
public class CandlesMarker extends MarkerView {

    public static final String TAG = CandlesMarker.class.getSimpleName();
    public static final float MARGIN = 15f;

    private final TextView tvOpen;
    private final TextView tvClose;
    private final TextView tvHigh;
    private final TextView tvLow;

    private float position;
    private final int xes;

    public CandlesMarker(Context context, int layoutResource, int xes) {
        super(context, layoutResource);
        this.xes = xes;

        tvOpen = findViewById(R.id.open_value);
        tvClose = findViewById(R.id.close_value);
        tvHigh = findViewById(R.id.high_value);
        tvLow = findViewById(R.id.low_value);
    }

    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        position = entry.getX();

        CandleEntry candle = (CandleEntry) entry;
        tvOpen.setText(toCurrencyFormat(candle.getOpen(), 2, 2));
        tvClose.setText(toCurrencyFormat(candle.getClose(), 2, 2));
        tvHigh.setText(toCurrencyFormat(candle.getHigh(), 2, 2));
        tvLow.setText(toCurrencyFormat(candle.getLow(), 2, 2));

        super.refreshContent(entry, highlight);
    }

    @Override
    public MPPointF getOffset() {
        MPPointF offset;
        if (position >= (float) xes / 2)
            offset = new MPPointF(-((float) getWidth()) - MARGIN, -getHeight() - MARGIN);

        else offset = new MPPointF(MARGIN, -getHeight() - MARGIN);

        return offset;
    }
}