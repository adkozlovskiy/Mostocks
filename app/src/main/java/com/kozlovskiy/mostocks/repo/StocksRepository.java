package com.kozlovskiy.mostocks.repo;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kozlovskiy.mostocks.AppDelegate;
import com.kozlovskiy.mostocks.api.StockService;
import com.kozlovskiy.mostocks.entities.ConstituentsResponse;
import com.kozlovskiy.mostocks.entities.StockCost;
import com.kozlovskiy.mostocks.entities.StockProfile;
import com.kozlovskiy.mostocks.entities.Ticker;
import com.kozlovskiy.mostocks.room.StocksDao;
import com.kozlovskiy.mostocks.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StocksRepository {

    public static final String TAG = StocksRepository.class.getSimpleName();
    private static final long UPDATE_INTERVAL = 60 * 1000; // 60 * 60 * 24 * 1000

    private final SharedPreferences sharedPreferences;
    private final StocksDao stocksDao;
    private final long tickersLastUpdateTime;
    private final long profilesLastUpdateTime;
    private final Context context;

    public StocksRepository(Context context) {
        this.context = context;
        stocksDao = ((AppDelegate) context.getApplicationContext()).getDatabase().getDao();

        sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        tickersLastUpdateTime = sharedPreferences.getLong("tickersLastUpdateTime", 0);
        profilesLastUpdateTime = sharedPreferences.getLong("profilesLastUpdateTime", 0);
    }

    public LiveData<List<Ticker>> getActualTickers() {
        updateTickersFromServer()
                .subscribeOn(Schedulers.io())
                .doOnError(Throwable::printStackTrace)
                .subscribe();

        MutableLiveData<List<Ticker>> data = new MutableLiveData<>();
        data.setValue(stocksDao.getTickers());
        return data;
    }

    public LiveData<List<StockProfile>> getActualProfiles() {
        // TODO: 20.02.2021 Обновление с сервера.

        MutableLiveData<List<StockProfile>> data = new MutableLiveData<>();
        data.setValue(stocksDao.getStockProfiles());
        return data;
    }

    public LiveData<StockCost> getActualCost(String ticker) {
        // TODO: 20.02.2021 Обновление с сервера.

        MutableLiveData<StockCost> data = new MutableLiveData<>();
        data.setValue(stocksDao.getStockCost(ticker));
        return data;
    }


    public Observable<StockProfile> updateProfilesFromServer(List<Ticker> tickers) {
        return Observable.create(emitter -> {
            if (NetworkUtils.isNetworkConnectionNotGranted(context)) {
                emitter.onError(new NetworkErrorException());

            } else if (new Date().getTime() - profilesLastUpdateTime > UPDATE_INTERVAL) {

                for (Ticker ticker : tickers) {
                    Log.d(TAG, "updateProfilesFromServer for: " + ticker.getTicker());
                    StockService.getInstance().getApi().getStockProfile(ticker.getTicker(), StockService.TOKEN)
                            .enqueue(new Callback<StockProfile>() {
                                @Override
                                public void onResponse(@NonNull Call<StockProfile> call, @NonNull Response<StockProfile> response) {

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putLong("profilesLastUpdateTime", new Date().getTime());
                                    editor.apply();

                                    emitter.onNext(response.body());
                                }

                                @Override
                                public void onFailure(@NonNull Call<StockProfile> call, @NonNull Throwable t) {
                                    emitter.onError(t);
                                }
                            });
                }
            }

        });

    }

    public Completable updateTickersFromServer() {
        return Completable.create(emitter -> {
            if (NetworkUtils.isNetworkConnectionNotGranted(context)) {
                emitter.onError(new NetworkErrorException());

            } else if (new Date().getTime() - tickersLastUpdateTime > UPDATE_INTERVAL) {
                Log.d(TAG, "updateStocksFromServer: ");
                StockService.getInstance().getApi().getTickers("^DJI", StockService.TOKEN)
                        .enqueue(new Callback<ConstituentsResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<ConstituentsResponse> call, @NonNull Response<ConstituentsResponse> response) {
                                if (response.body() != null) {
                                    List<String> tickers = response.body().getConstituents();
                                    List<Ticker> oTickers = new ArrayList<>();

                                    for (int i = 0; i < tickers.size(); i++) {
                                        oTickers.add(new Ticker(tickers.get(i)));
                                    }

                                    stocksDao.cacheTickers(oTickers);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putLong("tickersLastUpdateTime", new Date().getTime());
                                    editor.apply();

                                    emitter.onComplete();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ConstituentsResponse> call, @NonNull Throwable t) {
                                t.printStackTrace();
                            }
                        });
            }
        });
    }
}
