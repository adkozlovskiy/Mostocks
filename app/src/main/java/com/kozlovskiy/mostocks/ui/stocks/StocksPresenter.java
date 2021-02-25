package com.kozlovskiy.mostocks.ui.stocks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.util.Log;

import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.Ticker;
import com.kozlovskiy.mostocks.repo.StocksRepository;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class StocksPresenter {

    private StocksView stocksView;
    private StocksRepository stocksRepository;
    private AlertDialog.Builder builder;
    public static final String TAG = StocksPresenter.class.getSimpleName();
    private List<Ticker> tickers;

    public StocksPresenter(StocksView stocksView, StocksRepository stocksRepository, AlertDialog.Builder builder) {
        this.stocksView = stocksView;
        this.stocksRepository = stocksRepository;
        this.builder = builder;
    }

    public void initializeStocks() {
        tickers = stocksRepository.getActualTickers().getValue();

        if (tickers == null || tickers.isEmpty()) {
            // TODO: 22.02.2021 alertDialog
            stocksView.showRetryDialog(getRetryDialog(new NullPointerException()));

        } else {
            Log.d(TAG, "initializeStocks: ");
            stocksRepository.updateProfilesFromServer(tickers)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            stocksView.showStocks(tickers);
                        }

                        @Override
                        public void onError(Throwable throwable) {
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

    public void filter(String s) {
        ArrayList<Ticker> filteredTickers = new ArrayList<>();
        for (Ticker ticker : tickers) {
            if (ticker.getTicker().contains(s.toUpperCase())) {
                filteredTickers.add(ticker);
            }
        }

        stocksView.setFilteredTickers(filteredTickers);

    }

    public void unsubscribe() {
        stocksView = null;
    }
}
