package com.kozlovskiy.mostocks.ui.main.fragments.stocks;

import android.content.Context;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.models.stock.Stock;
import com.kozlovskiy.mostocks.room.StocksDao;

import java.util.ArrayList;
import java.util.List;

public class StocksPresenter {

    public static final String TAG = StocksPresenter.class.getSimpleName();
    private final StocksView stocksView;
    private final StocksDao stocksDao;
    private List<Stock> stocks;

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public StocksPresenter(StocksView stocksView, Context context, List<Stock> stocks) {
        this.stocksView = stocksView;
        this.stocks = stocks;

        this.stocksDao = ((AppDelegate) context
                .getApplicationContext())
                .getDatabase()
                .getDao();

    }

    public void initializeQuotes() {
        stocksView.updateStocks(stocksDao.getStocks());
        stocksView.stopRefreshing();
    }

    public void filter(String s) {
        List<Stock> filtered = new ArrayList<>();
        for (Stock stock : stocks) {
            if (stock.getSymbol().toUpperCase().startsWith(s.toUpperCase()) || stock.getName().toUpperCase().startsWith(s.toUpperCase())) {
                filtered.add(stock);
            }
        }

        stocksView.updateStocks(filtered);
    }
}
