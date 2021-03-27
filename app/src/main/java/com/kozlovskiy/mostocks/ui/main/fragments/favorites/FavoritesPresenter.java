package com.kozlovskiy.mostocks.ui.main.fragments.favorites;

import android.content.Context;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.models.stock.Favorite;
import com.kozlovskiy.mostocks.models.stock.Stock;
import com.kozlovskiy.mostocks.room.StocksDao;

import java.util.ArrayList;
import java.util.List;

public class FavoritesPresenter {

    private FavoritesView favoritesView;
    private final StocksDao stocksDao;
    private List<Stock> stocks;

    public FavoritesPresenter(FavoritesView favoritesView, Context context) {
        this.favoritesView = favoritesView;

        stocksDao = ((AppDelegate) context.getApplicationContext())
                .getDatabase()
                .getDao();

    }

    public void initializeFavorites(List<Stock> stocks) {
        ArrayList<Stock> filteredStocks = new ArrayList<>();
        List<Favorite> favorites = stocksDao.getFavorites();
        List<String> favoritesStrings = new ArrayList<>();

        for (Favorite favorite : favorites) {
            favoritesStrings.add(favorite.getSymbol());
        }

        for (Stock stock : stocks) {
            if (favoritesStrings.contains(stock.getSymbol())) {
                filteredStocks.add(stock);
            }
        }

        favoritesView.updateStocks(filteredStocks);
    }

    public void unsubscribe() {
        favoritesView = null;
    }
}
