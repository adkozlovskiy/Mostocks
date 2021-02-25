package com.kozlovskiy.mostocks.ui.stocks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.util.Log;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.repo.StocksRepository;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class StocksPresenter {

    private StocksView stocksView;
    private final StocksRepository stocksRepository;
    private final AlertDialog.Builder builder;
    public static final String TAG = StocksPresenter.class.getSimpleName();
    private List<Stock> stocks;

    public StocksPresenter(StocksView stocksView, StocksRepository stocksRepository, AlertDialog.Builder builder) {
        this.stocksView = stocksView;
        this.stocksRepository = stocksRepository;
        this.builder = builder;
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

    public void unsubscribe() {
        stocksView = null;
    }
}
