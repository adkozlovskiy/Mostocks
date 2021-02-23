package com.kozlovskiy.mostocks.ui.stocks;

import android.app.Dialog;

import com.kozlovskiy.mostocks.entities.Ticker;

import java.util.List;

public interface StocksView {

    void showStocks(List<Ticker> tickers);

    void showAlertDialog(Dialog dialog);
}
