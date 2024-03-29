package com.kozlovskiy.mostocks.ui.splash;

import android.app.AlertDialog;
import android.content.Context;

import com.google.gson.Gson;
import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.models.stock.Stock;
import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.room.StocksDao;
import com.kozlovskiy.mostocks.utils.CacheUtil;
import com.kozlovskiy.mostocks.utils.NetworkUtil;

import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class SplashPresenter {

    private SplashView splashView;
    private final Context context;
    private final AlertDialog.Builder builder;
    private List<Stock> stocks;

    public SplashPresenter(SplashView splashView, Context context) {
        this.splashView = splashView;
        this.context = context;
        this.builder = new AlertDialog.Builder(context);
    }

    public void initializeQuotes(List<Stock> stocks) {
        if (NetworkUtil.isNetworkConnectionNotGranted(context))
            buildNoNetworkDialog();

        else {
            StocksRepository stocksRepository = new StocksRepository(context);
            stocksRepository.getSymbolQuotes(stocks).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<List<Stock>>() {
                        @Override
                        public void onSuccess(@NonNull List<Stock> s) {
                            Gson gson = new Gson();
                            splashView.startMainActivity(gson.toJson(stocks));
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            buildErrorQuotesLoadingDialog(e);
                            e.printStackTrace();
                        }
                    });
        }
    }

    public void initializeSymbols() {
        if (NetworkUtil.isNetworkConnectionNotGranted(context))
            buildNoNetworkDialog();

        else {

            if (CacheUtil.getTickersCachedFlag(context)) {
                StocksDao stocksDao = ((AppDelegate) context.getApplicationContext()).getDatabase().getDao();
                initializeQuotes(stocksDao.getStocks());

            } else {
                StocksRepository stocksRepository = new StocksRepository(context);
                stocksRepository.getStockSymbols()
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableSingleObserver<List<Stock>>() {
                            @Override
                            public void onSuccess(@NonNull List<Stock> response) {
                                stocks = response;
                                initializeQuotes(stocks);
                                CacheUtil.setTickersCachedFlag(context, true);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                buildErrorSymbolsLoadingDialog(e);
                                e.printStackTrace();
                            }
                        });
            }
        }
    }

    public void buildErrorSymbolsLoadingDialog(Throwable e) {
        builder.setTitle(R.string.loading_error);
        if (e instanceof SocketTimeoutException) {
            builder.setMessage(R.string.timed_out);

        } else {
            builder.setMessage(R.string.unknown_error);

        }

        builder.setPositiveButton(R.string.retry, (di, i) -> initializeSymbols())
                .setNegativeButton(R.string.cancel, (di, id) -> di.cancel());

        splashView.showDialog(builder.create());
    }

    public void buildErrorQuotesLoadingDialog(Throwable e) {
        builder.setTitle(R.string.loading_error);
        if (e instanceof SocketTimeoutException) {
            builder.setMessage(R.string.timed_out);

        } else {
            builder.setMessage(R.string.unknown_error);

        }

        builder.setPositiveButton(R.string.retry, (di, i) -> initializeQuotes(stocks))
                .setNegativeButton(R.string.cancel, (di, id) -> di.cancel());

        splashView.showDialog(builder.create());
    }

    public void buildNoNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.network_error)
                .setMessage(R.string.no_network_message)
                .setPositiveButton(R.string.retry, (di, id) -> initializeSymbols())
                .setNegativeButton(R.string.exit, (di, id) -> di.cancel());

        splashView.showDialog(builder.create());
    }

    public void unsubscribe() {
        splashView = null;
    }
}
