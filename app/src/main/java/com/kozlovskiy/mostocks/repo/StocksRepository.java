package com.kozlovskiy.mostocks.repo;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.api.StockService;
import com.kozlovskiy.mostocks.entities.ConstituentsResponse;
import com.kozlovskiy.mostocks.entities.Cost;
import com.kozlovskiy.mostocks.entities.News;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.room.StocksDao;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StocksRepository {

    private final StocksDao stocksDao;
    private final Context context;

    public StocksRepository(Context context) {
        this.context = context;
        stocksDao = ((AppDelegate) context.getApplicationContext())
                .getDatabase()
                .getDao();
    }

    public Completable updateProfiles(List<Stock> stocks) {
        return Completable.create(emitter -> {
            List<Stock> stockProfiles = new ArrayList<>();
            for (Stock stock : stocks) {
                StockService.getInstance().getApi()
                        .getStockProfile(stock.getTicker(), StockService.TOKEN)
                        .enqueue(new Callback<Stock>() {
                            @Override
                            public void onResponse(@NonNull Call<Stock> call, @NonNull Response<Stock> response) {
                                if (response.body() != null) {
                                    stockProfiles.add(response.body());

                                    if (stockProfiles.size() == stocks.size()) {
                                        stocksDao.updateStocks(stockProfiles);
                                        emitter.onComplete();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Stock> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException) {
                                    Toast.makeText(context, "Время вышло", Toast.LENGTH_SHORT).show();
                                }

                                emitter.onError(t);
                            }
                        });
            }
        });
    }

    public Single<List<Stock>> updateTickers() {
        return Single.create(emitter -> StockService.getInstance().getApi()
                .getTickers("^DJI", StockService.TOKEN)
                .enqueue(new Callback<ConstituentsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ConstituentsResponse> call, @NonNull Response<ConstituentsResponse> response) {
                        if (response.body() != null) {
                            List<String> tickers = response.body().getConstituents();
                            List<Stock> stocks = new ArrayList<>();

                            for (int i = 0; i < 14; i++) {
                                stocks.add(new Stock(tickers.get(i)));
                            }

                            if (stocksDao.getStocks().size() == 0) {
                                stocksDao.cacheStocks(stocks);
                            } else {
                                stocksDao.updateStocks(stocks);
                            }

                            emitter.onSuccess(stocks);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ConstituentsResponse> call, @NonNull Throwable t) {
                        if (t instanceof SocketTimeoutException)
                            Toast.makeText(context, "Время вышло", Toast.LENGTH_SHORT).show();

                        t.printStackTrace();
                    }
                }));
    }

    public Completable updateCost(List<Stock> stocks) {
        return Completable.create(emitter -> {
            List<Cost> stockCost = new ArrayList<>();
            for (Stock stock : stocks) {
                StockService.getInstance().getApi()
                        .getStockCost(stock.getTicker(), StockService.TOKEN)
                        .enqueue(new Callback<Cost>() {
                            @Override
                            public void onResponse(@NonNull Call<Cost> call, @NonNull Response<Cost> response) {
                                if (response.body() != null) {
                                    Cost cost = new Cost(stock.getTicker());
                                    cost.setCurrentCost(response.body().getCurrentCost());
                                    cost.setPreviousCost(response.body().getPreviousCost());
                                    stockCost.add(cost);

                                    if (stockCost.size() == stocks.size()) {
                                        if (stocksDao.getCosts().size() == 0) {
                                            stocksDao.insertCost(stockCost);
                                        } else {
                                            stocksDao.updateCost(stockCost);
                                        }

                                        emitter.onComplete();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Cost> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException) {
                                    Toast.makeText(context, "Время вышло", Toast.LENGTH_SHORT).show();
                                }

                                emitter.onError(t);
                            }
                        });
            }
        });
    }

    public Single<List<News>> updateNews(String ticker) {
        return Single.create(emitter -> StockService.getInstance().getApi()
                .getCompanyNews(ticker, "2021-01-01", "2021-03-01", StockService.TOKEN)
                .enqueue(new Callback<List<News>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<News>> call, @NonNull Response<List<News>> response) {
                        if (response.body() != null) {
                            List<News> newsList = response.body();

                            for (News news : newsList) {
                                news.setTicker(ticker);
                            }

                            stocksDao.cacheNews(newsList);
                            emitter.onSuccess(newsList);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<News>> call, @NonNull Throwable t) {
                        emitter.onError(t);
                    }
                }));
    }
}
