package com.kozlovskiy.mostocks.room;

import android.content.Context;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.models.stock.Favorite;

public class RoomDelegate {

    private final StocksDao stocksDao;

    public RoomDelegate(Context context) {
        this.stocksDao = ((AppDelegate) context.getApplicationContext())
                .getDatabase()
                .getDao();
    }

    public void addFavorite(Favorite favorite) {
        new Thread(() -> stocksDao.addFavorite(favorite)).start();
    }

    public void removeFavorite(Favorite favorite) {
        new Thread(() -> stocksDao.removeFavorite(favorite)).start();
    }

    public Favorite getFavorite(String symbol) {
        return stocksDao.getFavorite(symbol);
    }

    public void updateStockQuote(String symbol, Double quote) {
        new Thread(() -> stocksDao.updateStockQuote(symbol, quote)).start();
    }
}
