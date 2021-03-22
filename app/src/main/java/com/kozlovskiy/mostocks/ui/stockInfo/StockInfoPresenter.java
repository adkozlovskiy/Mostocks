package com.kozlovskiy.mostocks.ui.stockInfo;

import android.content.Context;

public class StockInfoPresenter {

    private StockInfoView stockInfoView;

    public StockInfoPresenter(StockInfoView stockInfoView, Context context) {
        this.stockInfoView = stockInfoView;

    }

    public void unsubscribe() {
        stockInfoView = null;
    }

}
