package com.kozlovskiy.mostocks.room;

import android.content.Context;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.models.stock.Favorite;
import com.kozlovskiy.mostocks.models.stock.Stock;
import com.kozlovskiy.mostocks.models.stockInfo.IndicatorsResponse;
import com.kozlovskiy.mostocks.models.stockInfo.Recommendation;

import java.util.List;

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

    public void cacheIndicators(IndicatorsResponse.Indicators indicators) {
        new Thread(() -> stocksDao.cacheIndicators(indicators)).start();
    }

    public void cacheRecommendations(List<Recommendation> recommendations) {
        new Thread(() -> stocksDao.cacheRecommendations(recommendations)).start();
    }

    public void cacheStocks(List<Stock> stocks) {
        new Thread(() -> stocksDao.cacheStocks(stocks)).start();
    }
}
