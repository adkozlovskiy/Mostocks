package com.kozlovskiy.mostocks.ui.main.fragments.favorites;

import android.content.Context;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.entities.Favorite;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.room.StocksDao;

import java.util.ArrayList;
import java.util.List;

public class FavoritesPresenter {

    private FavoritesView favoritesView;
    private final StocksDao stocksDao;
    private List<Stock> stocks;

    public List<Stock> getStocks() {
        return stocks;
    }

    public FavoritesPresenter(FavoritesView favoritesView, Context context) {
        this.favoritesView = favoritesView;

        stocksDao = ((AppDelegate) context.getApplicationContext())
                .getDatabase()
                .getDao();

    }

    public void initializeFavorites() {
        stocks = stocksDao.getStocks();

        ArrayList<Stock> filteredStocks = new ArrayList<>();
        List<Favorite> favorites = stocksDao.getFavorites();
        List<String> favoritesStrings = new ArrayList<>();

        for (Favorite favorite : favorites) {
            favoritesStrings.add(favorite.getTicker());
        }

        for (Stock stock : stocks) {
            if (favoritesStrings.contains(stock.getTicker())) {
                filteredStocks.add(stock);
            }
        }

        favoritesView.updateStocks(filteredStocks);
    }

    public void unsubscribe() {
        favoritesView = null;
    }
}
