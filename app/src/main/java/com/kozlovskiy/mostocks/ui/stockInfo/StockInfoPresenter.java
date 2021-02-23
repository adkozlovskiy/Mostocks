package com.kozlovskiy.mostocks.ui.stockInfo;

public class StockInfoPresenter {

    private StockInfoView stockInfoView;

    public StockInfoPresenter(StockInfoView stockInfoView) {
        this.stockInfoView = stockInfoView;
    }

    public void unsubscribe() {
        stockInfoView = null;
    }
}
