package com.kozlovskiy.mostocks.ui.stocks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.Favorite;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.room.StocksDao;
import com.kozlovskiy.mostocks.utils.SettingsUtils;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class StocksPresenter {

    private StocksView stocksView;
    public static final String TAG = StocksPresenter.class.getSimpleName();
    private List<Stock> stocks;

    private final Context context;
    private final StocksRepository stocksRepository;
    private final AlertDialog.Builder builder;
    private final StocksDao stocksDao;

    public StocksPresenter(StocksView stocksView, Context context) {
        this.stocksView = stocksView;
        this.context = context;
        this.stocksRepository = new StocksRepository(context);
        this.builder = new AlertDialog.Builder(context);

        this.stocksDao = ((AppDelegate) context
                .getApplicationContext())
                .getDatabase()
                .getDao();

        if (this.stocks == null) {
            this.stocks = this.stocksDao.getStocks();
        }
    }

    public void removeFilter() {
        stocksView.setFilteredStocks(stocks);
    }

    public void initializeStocks() {
        stocks = stocksRepository.getActualStocks().getValue();

        if (stocks == null || stocks.isEmpty()) {
            // TODO: 22.02.2021 alertDialog
            stocksView.showRetryDialog(getRetryDialog(new NullPointerException()));

        } else {
            Log.d(TAG, "initializeStocks: ");
            stocksRepository.updateProfilesFromServer(stocks)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(s -> this.stocks = s)
                    .subscribe(new DisposableSingleObserver<List<Stock>>() {

                        @Override
                        public void onSuccess(@NonNull List<Stock> stocks) {
                            SettingsUtils.updateStocksUptime(context);
                            stocksView.showStocks(stocks);
                        }

                        @Override
                        public void onError(@NonNull Throwable throwable) {
                            stocksView.showRetryDialog(getRetryDialog(throwable));
                        }
                    });
        }
    }

    private Dialog getRetryDialog(Throwable throwable) {
        builder.setTitle(R.string.loading_error);

        if (throwable instanceof SocketTimeoutException) {
            builder.setMessage(R.string.timed_out)
                    .setPositiveButton(R.string.retry, (dialog, id) -> dialog.cancel());
        }

        return builder.create();
    }

    public void filterStocks(String s) {
        ArrayList<Stock> filteredStocks = new ArrayList<>();
        for (Stock stock : stocks) {
            if (stock.getTicker().startsWith(s.toUpperCase())
                    || stock.getName().toUpperCase().startsWith(s.toUpperCase())) {
                filteredStocks.add(stock);
            }
        }

        stocksView.setFilteredStocks(filteredStocks);
    }

    public void filterFavorites() {
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

        stocksView.setFilteredStocks(filteredStocks);
    }

    public void unsubscribe() {
        stocksView = null;
    }
}
