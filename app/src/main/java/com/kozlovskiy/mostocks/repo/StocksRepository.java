package com.kozlovskiy.mostocks.repo;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.api.StockService;
import com.kozlovskiy.mostocks.entities.ConstituentsResponse;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.entities.StockCost;
import com.kozlovskiy.mostocks.room.StocksDao;
import com.kozlovskiy.mostocks.utils.NetworkUtils;
import com.kozlovskiy.mostocks.utils.SettingsUtils;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StocksRepository {

    public static final String TAG = StocksRepository.class.getSimpleName();
    private static final long UPDATE_INTERVAL = 60 * 1000; // 60 * 60 * 24 * 1000

    private final StocksDao stocksDao;
    private final Context context;

    public StocksRepository(Context context) {
        this.context = context;
        stocksDao = ((AppDelegate) context.getApplicationContext())
                .getDatabase().getDao();
    }

    public LiveData<List<Stock>> getActualStocks() {
        updateStocksFromServer()
                .subscribeOn(Schedulers.io())
                .doOnError(Throwable::printStackTrace)
                .subscribe();

        MutableLiveData<List<Stock>> data = new MutableLiveData<>();
        data.setValue(stocksDao.getStocks());
        return data;
    }

    public LiveData<StockCost> getActualCost(String ticker) {
        // TODO: 20.02.2021 Обновление с сервера.

        MutableLiveData<StockCost> data = new MutableLiveData<>();
        data.setValue(stocksDao.getStockCost(ticker));
        return data;
    }


    public Single<List<Stock>> updateProfilesFromServer(List<Stock> stocks) {
        return Single.create(emitter -> {
            if (NetworkUtils.isNetworkConnectionNotGranted(context)) {
                emitter.onError(new NetworkErrorException());
                Log.d(TAG, "updateProfilesFromServer: no network");

            } else if (new Date().getTime() - SettingsUtils.getStocksUptime(context) > UPDATE_INTERVAL) {
                List<Stock> stockProfiles = new ArrayList<>();
                Log.d(TAG, "updateProfilesFromServer: from server");
                for (Stock stock : stocks) {
                    StockService.getInstance().getApi().getStockProfile(stock.getTicker(), StockService.TOKEN)
                            .enqueue(new Callback<Stock>() {
                                @Override
                                public void onResponse(@NonNull Call<Stock> call, @NonNull Response<Stock> response) {
                                    if (response.body() != null) {
                                        stockProfiles.add(response.body());

                                        if (stockProfiles.size() == stocks.size()) {
                                            stocksDao.cacheStocks(stockProfiles);
                                            emitter.onSuccess(stockProfiles);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<Stock> call, @NonNull Throwable t) {

                                    if (t instanceof SocketTimeoutException)
                                        Toast.makeText(context, "Время вышло", Toast.LENGTH_SHORT).show();

                                    emitter.onError(t);
                                }
                            });
                }
            } else {
                emitter.onSuccess(stocksDao.getStocks());
                Log.d(TAG, "updateProfilesFromServer: from cache");
            }
        });
    }

    public Completable updateStocksFromServer() {
        return Completable.create(emitter -> {
            if (NetworkUtils.isNetworkConnectionNotGranted(context)) {
                emitter.onError(new NetworkErrorException());

            } else if (new Date().getTime() - SettingsUtils.getStocksUptime(context) > UPDATE_INTERVAL) {
                StockService.getInstance().getApi().getTickers("^DJI", StockService.TOKEN)
                        .enqueue(new Callback<ConstituentsResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<ConstituentsResponse> call, @NonNull Response<ConstituentsResponse> response) {
                                if (response.body() != null) {
                                    List<String> tickers = response.body().getConstituents();
                                    List<Stock> stocks = new ArrayList<>();

                                    for (int i = 0; i < tickers.size(); i++) {
                                        stocks.add(new Stock(tickers.get(i)));
                                    }

                                    stocksDao.cacheStocks(stocks);
                                    emitter.onComplete();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ConstituentsResponse> call, @NonNull Throwable t) {
                                if (t instanceof SocketTimeoutException)
                                    Toast.makeText(context, "Время вышло", Toast.LENGTH_SHORT).show();

                                t.printStackTrace();
                            }
                        });
            } else emitter.onComplete();
        });
    }
}
