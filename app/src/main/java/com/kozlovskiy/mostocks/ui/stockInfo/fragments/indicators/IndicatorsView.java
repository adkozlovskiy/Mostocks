package com.kozlovskiy.mostocks.ui.stockInfo.fragments.indicators;

import android.app.Dialog;

import java.util.List;

public interface IndicatorsView {

    void showIndicators(List<Object> indicators);

    void showDialog(Dialog dialog);
}
