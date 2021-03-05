package com.kozlovskiy.mostocks.ui.main.fragments.stocks;

import android.app.Dialog;

import com.kozlovskiy.mostocks.entities.Stock;

import java.util.List;

public interface StocksView {

    void showRetryDialog(Dialog dialog);

    void updateStocks(List<Stock> stocks);
}
