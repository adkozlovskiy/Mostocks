package com.kozlovskiy.mostocks.repo;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.SharedPreferences;

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
import io.reactivex.Single;
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

    public LiveData<StockCost> getActualCost(String ticker) {
        // TODO: 20.02.2021 Обновление с сервера.

        MutableLiveData<StockCost> data = new MutableLiveData<>();
        data.setValue(stocksDao.getStockCost(ticker));
        return data;
    }


    public Single<List<StockProfile>> updateProfilesFromServer(List<Ticker> tickers) {
        return Single.create(emitter -> {
            if (NetworkUtils.isNetworkConnectionNotGranted(context)) {
                emitter.onError(new NetworkErrorException());

            } else if (new Date().getTime() - profilesLastUpdateTime > UPDATE_INTERVAL) {
                List<StockProfile> stockProfiles = new ArrayList<>();
                for (Ticker ticker : tickers) {
                    StockService.getInstance().getApi().getStockProfile(ticker.getTicker(), StockService.TOKEN)
                            .enqueue(new Callback<StockProfile>() {
                                @Override
                                public void onResponse(@NonNull Call<StockProfile> call, @NonNull Response<StockProfile> response) {
                                    if (response.body() != null) {
                                        stockProfiles.add(response.body());

                                        if (stockProfiles.size() == tickers.size()) {
                                            stocksDao.cacheStockProfiles(stockProfiles);
                                            emitter.onSuccess(stockProfiles);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<StockProfile> call, @NonNull Throwable t) {
                                    emitter.onError(t);
                                }
                            });
                }
            } else emitter.onSuccess(stocksDao.getStockProfiles());
        });
    }

    public Completable updateTickersFromServer() {
        return Completable.create(emitter -> {
            if (NetworkUtils.isNetworkConnectionNotGranted(context)) {
                emitter.onError(new NetworkErrorException());

            } else if (new Date().getTime() - tickersLastUpdateTime > UPDATE_INTERVAL) {
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
            } else emitter.onComplete();
        });
    }
}
