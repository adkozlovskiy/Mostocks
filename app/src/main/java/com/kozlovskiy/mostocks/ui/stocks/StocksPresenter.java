package com.kozlovskiy.mostocks.ui.stocks;

import android.app.Dialog;

import com.kozlovskiy.mostocks.entities.Ticker;
import com.kozlovskiy.mostocks.repo.StocksRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class StocksPresenter {

    private StocksView stocksView;
    private StocksRepository stocksRepository;

    public StocksPresenter(StocksView stocksView, StocksRepository stocksRepository) {
        this.stocksView = stocksView;
        this.stocksRepository = stocksRepository;
    }

    public void initializeStocks() {
        List<Ticker> tickers = stocksRepository.getActualTickers().getValue();

        if (tickers == null || tickers.isEmpty()) {
            // TODO: 22.02.2021 alertDialog
            stocksView.showAlertDialog(getAlertDialog(new NullPointerException()));

        } else {
            stocksRepository.updateProfilesFromServer(tickers)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> stocksView.showAlertDialog(getAlertDialog(throwable))) // TODO: 23.02.2021
                    .doOnComplete(() -> stocksView.showStocks(tickers))
                    .subscribe();

        }
    }

    private Dialog getAlertDialog(Throwable throwable) {
        return null;
    }

    public void unsubscribe() {
        stocksView = null;
    }
}
