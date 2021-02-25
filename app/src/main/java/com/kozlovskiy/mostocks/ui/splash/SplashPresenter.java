package com.kozlovskiy.mostocks.ui.splash;

import android.app.Dialog;

import com.kozlovskiy.mostocks.repo.StocksRepository;

import io.reactivex.schedulers.Schedulers;

public class SplashPresenter {

    private SplashView splashView;
    private final StocksRepository stocksRepository;

    public SplashPresenter(SplashView splashView, StocksRepository stocksRepository) {
        this.splashView = splashView;
        this.stocksRepository = stocksRepository;
    }

    public void updateStocksFromServer() {
        stocksRepository.updateStocksFromServer()
                .subscribeOn(Schedulers.io())
                .doOnComplete(splashView::startStocksActivity)
                .doOnError(throwable -> splashView.showAlertDialog(getErrorAlertDialog(throwable)))
                .subscribe();
    }

    public Dialog getErrorAlertDialog(Throwable throwable) {

        return null;
    }

    public void unsubscribe() {
        splashView = null;
    }
}
