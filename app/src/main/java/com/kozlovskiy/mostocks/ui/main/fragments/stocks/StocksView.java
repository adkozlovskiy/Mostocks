package com.kozlovskiy.mostocks.ui.main.fragments.stocks;

import android.app.Dialog;

import com.kozlovskiy.mostocks.models.stock.Stock;

import java.util.List;

public interface StocksView {

    void showDialog(Dialog dialog);

    void updateStocks(List<Stock> stocks);

}
