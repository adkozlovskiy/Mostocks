package com.kozlovskiy.mostocks.ui.stockInfo.fragments.news;

import android.content.Context;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.entities.News;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.repo.StocksRepository;
import com.kozlovskiy.mostocks.room.StocksDao;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class NewsPresenter {

    private final NewsView newsView;
    private final StocksRepository stocksRepository;
    private final StocksDao stocksDao;

    public NewsPresenter(NewsView newsView, Context context) {
        this.newsView = newsView;
        stocksRepository = new StocksRepository(context);
        stocksDao = ((AppDelegate) context.getApplicationContext())
                .getDatabase()
                .getDao();
    }

    public void initializeNews(String ticker) {
        stocksRepository.updateNews(ticker)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<News>>() {
                    @Override
                    public void onSuccess(@NonNull List<News> news) {
                        newsView.updateNews(news);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }


    public void initializeStock(String ticker) {
        if (ticker != null) {
            Stock stock = stocksDao.getStockByTicker(ticker);
            newsView.showStockInfo(stock);
        }
    }
}
