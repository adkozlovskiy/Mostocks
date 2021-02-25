package com.kozlovskiy.mostocks.ui.stocks;

import android.app.Dialog;

import com.kozlovskiy.mostocks.entities.Stock;

import java.util.List;

public interface StocksView {

    void showStocks(List<Stock> stocks);

    void showRetryDialog(Dialog dialog);

    void setFilteredStocks(List<Stock> stocks);
}
