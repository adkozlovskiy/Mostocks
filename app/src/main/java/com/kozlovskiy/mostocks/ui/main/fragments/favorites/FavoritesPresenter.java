package com.kozlovskiy.mostocks.ui.main.fragments.favorites;

import android.content.Context;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.models.stock.Favorite;
import com.kozlovskiy.mostocks.models.stock.Stock;
import com.kozlovskiy.mostocks.room.StocksDao;

import java.util.ArrayList;
import java.util.List;

public class FavoritesPresenter {

    public static final String TAG = FavoritesPresenter.class.getSimpleName();
    private final FavoritesView favoritesView;
    private final StocksDao stocksDao;
    private List<Stock> stocks;

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public FavoritesPresenter(FavoritesView favoritesView, Context context) {
        this.favoritesView = favoritesView;

        stocksDao = ((AppDelegate) context.getApplicationContext())
                .getDatabase()
                .getDao();

    }

    public void initializeFavorites(List<Stock> stocks) {
        ArrayList<Stock> favoritesStocks = new ArrayList<>();
        List<Favorite> favorites = stocksDao.getFavorites();
        List<String> favoritesStrings = new ArrayList<>();

        for (Favorite favorite : favorites) {
            favoritesStrings.add(favorite.getSymbol());
        }

        for (Stock stock : stocks) {
            if (favoritesStrings.contains(stock.getSymbol())) {
                favoritesStocks.add(stock);
            }
        }

        this.stocks = favoritesStocks;
        favoritesView.updateStocks(favoritesStocks);
    }

    public void filter(String s) {
        List<Stock> filtered = new ArrayList<>();
        for (Stock stock : stocks) {
            if (stock.getSymbol().toUpperCase().startsWith(s.toUpperCase()) || stock.getName().toUpperCase().startsWith(s.toUpperCase())) {
                filtered.add(stock);
            }
        }

        favoritesView.updateStocks(filtered);
    }
}
