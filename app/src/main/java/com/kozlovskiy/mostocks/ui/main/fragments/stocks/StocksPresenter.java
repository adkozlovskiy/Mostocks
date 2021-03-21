package com.kozlovskiy.mostocks.ui.main.fragments.stocks;

import android.accounts.NetworkErrorException;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.R;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.room.StocksDao;
import com.kozlovskiy.mostocks.utils.CacheUtil;

import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class StocksPresenter {

    private final Context context;
    private final StocksRepository stocksRepository;
    private final AlertDialog.Builder builder;
    private final StocksDao stocksDao;
    public static final String TAG = StocksPresenter.class.getSimpleName();

    private StocksView stocksView;
    private List<Stock> stocks;

    public StocksPresenter(StocksView stocksView, Context context, List<Stock> stocks) {
        this.stocksView = stocksView;
        this.context = context;
        this.stocks = stocks;
        this.stocksRepository = new StocksRepository(context);
        this.builder = new AlertDialog.Builder(context);

        this.stocksDao = ((AppDelegate) context
                .getApplicationContext())
                .getDatabase()
                .getDao();

    }

    public void initializeStocks() {
        if (CacheUtil.quoteCacheIsUpToDate(context)) {
            stocksView.updateStocks(stocks);

        } else {
            stocksRepository.getSymbolQuotes(stocks).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<List<Stock>>() {
                        @Override
                        public void onSuccess(@NonNull List<Stock> stocks) {
                            if (stocksDao.getStocks().size() == 0) {
                                stocksDao.cacheStocks(stocks);
                            } else {
                                stocksDao.updateStocks(stocks);
                            }

                            CacheUtil.updateQuoteUptime(context);
                            stocksView.updateStocks(stocks);

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            stocksView.showRetryDialog(getRetryDialog(e));
                            e.printStackTrace();
                        }
                    });
        }
    }

    private Dialog getRetryDialog(Throwable throwable) {
        builder.setTitle(R.string.loading_error);

        if (throwable instanceof SocketTimeoutException) {
            builder.setMessage(R.string.timed_out)
                    .setPositiveButton(R.string.retry, (dialog, id) -> initializeStocks())
                    .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel());

        } else if (throwable instanceof NetworkErrorException) {
            builder.setMessage(R.string.no_network)
                    .setPositiveButton(R.string.turn_on_network, (dialog, id) -> dialog.cancel())
                    .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel());
        }

        return builder.create();
    }

    public void unsubscribe() {
        stocksView = null;
    }
}
