package com.kozlovskiy.mostocks.repo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.api.StockService;
import com.kozlovskiy.mostocks.entities.ConstituentsResponse;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.room.StocksDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StocksRepository {

    public static final String TAG = StocksRepository.class.getSimpleName();
    private final StocksDao stocksDao;
    private SharedPreferences sharedPreferences;
    private long lastUpdateTime;

    public StocksRepository(Context context) {
        stocksDao = ((AppDelegate) context.getApplicationContext()).getDatabase().getDao();

        sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        lastUpdateTime = sharedPreferences.getLong("last_update", 0);
    }

    public LiveData<List<Stock>> getStocks() {
        updateStocksFromServer()
                .subscribeOn(Schedulers.io())
                .doOnError(Throwable::printStackTrace)
                .subscribe();

        MutableLiveData<List<Stock>> data = new MutableLiveData<>();
        data.setValue(stocksDao.getStocks());
        return data;
    }

    public Completable updateStocksFromServer() {
        return Completable.create(emitter -> {
            if (new Date().getTime() - lastUpdateTime > 1000 * 90) {
                Log.d(TAG, "updateStocksFromServer: ");
                StockService.getInstance().getApi().getTickers("^DJI", StockService.TOKEN)
                        .enqueue(new Callback<ConstituentsResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<ConstituentsResponse> call, @NonNull Response<ConstituentsResponse> response) {
                                if (response.body() != null) {
                                    List<String> tickers = response.body().getConstituents();
                                    List<Stock> stocks = new ArrayList<>();

                                    for (int i = 0; i < tickers.size(); i++) {
                                        stocks.add(new Stock(i + 1, tickers.get(i), "Test company", "USD", "123321"));
                                    }

                                    stocksDao.cacheStocks(stocks);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putLong("last_update", new Date().getTime());
                                    editor.apply();

                                    emitter.onComplete();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ConstituentsResponse> call, @NonNull Throwable t) {
                                t.printStackTrace();
                            }
                        });
            } else {
                Log.d(TAG, "updateStocksFromServer: just cached one!");
                emitter.onComplete();
            }
        });
    }
}
