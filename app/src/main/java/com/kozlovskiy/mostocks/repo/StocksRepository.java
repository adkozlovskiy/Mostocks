package com.kozlovskiy.mostocks.repo;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.api.StockService;
import com.kozlovskiy.mostocks.entities.ConstituentsResponse;
import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.room.StocksDao;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StocksRepository {

    public static final String TAG = StocksRepository.class.getSimpleName();
    private final StocksDao stocksDao;

    public StocksRepository(Context context) {
        stocksDao = ((AppDelegate) context.getApplicationContext()).getDatabase().getDao();
    }

    public LiveData<List<Stock>> getStocks() {
        updateStocksFromServer();

        MutableLiveData<List<Stock>> data = new MutableLiveData<>();
        data.setValue(stocksDao.getStocks());
        return data;
    }

    public void updateStocksFromServer() {
        // TODO: добавить проверку на время обновления данных.
        // TODO: обновлять с сервера раз в минуту.

        MutableLiveData<List<Stock>> data = new MutableLiveData<>();
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

                            data.setValue(stocks);
                            stocksDao.cacheStocks(stocks);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ConstituentsResponse> call, @NonNull Throwable t) {

                        if (t instanceof SocketTimeoutException) {
                            Log.e(TAG, "onFailure: превышен лимит ожидания ответа.");
                        }

                    }
                });
    }
}
