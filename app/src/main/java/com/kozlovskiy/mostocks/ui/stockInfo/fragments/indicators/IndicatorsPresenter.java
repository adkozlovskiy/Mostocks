package com.kozlovskiy.mostocks.ui.stockInfo.fragments.indicators;

import android.content.Context;

public class IndicatorsPresenter {

    private final IndicatorsView indicatorsView;
    private final Context context;

    public IndicatorsPresenter(IndicatorsView indicatorsView, Context context) {
        this.indicatorsView = indicatorsView;
        this.context = context;
    }
}
