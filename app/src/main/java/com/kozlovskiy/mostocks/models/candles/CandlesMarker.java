package com.kozlovskiy.mostocks.models.candles;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.utils.QuoteConverter;

public class CandlesMarker extends MarkerView {

    public static final float MARGIN = 15f;
    private final TextView tvOpen;
    private final TextView tvClose;
    private final TextView tvHigh;
    private final TextView tvLow;
    private final int xes;
    private float position;

    public CandlesMarker(Context context, int layoutResource, int xes) {
        super(context, layoutResource);
        this.xes = xes;

        tvOpen = findViewById(R.id.open_value);
        tvClose = findViewById(R.id.close_value);
        tvHigh = findViewById(R.id.high_value);
        tvLow = findViewById(R.id.low_value);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        CandleEntry candleEntry = (CandleEntry) e;
        position = e.getX();
        tvOpen.setText(QuoteConverter.toCurrencyFormat(candleEntry.getOpen(), 2, 2));
        tvClose.setText(QuoteConverter.toCurrencyFormat(candleEntry.getClose(), 2, 2));
        tvHigh.setText(QuoteConverter.toCurrencyFormat(candleEntry.getHigh(), 2, 2));
        tvLow.setText(QuoteConverter.toCurrencyFormat(candleEntry.getLow(), 2, 2));
        super.refreshContent(e, highlight);
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