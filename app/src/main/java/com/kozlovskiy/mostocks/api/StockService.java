package com.kozlovskiy.mostocks.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StockService {

    public static final String TOKEN = "sandbox_c0l8c7748v6orbr0u01g";
    private static final String BASE_URL = "https://finnhub.io/api/v1/";
    private static StockService instance;
    private final Retrofit mRetrofit;

    private StockService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static StockService getInstance() {
        if (instance == null)
            instance = new StockService();

        return instance;
    }

    public StockApi getApi() {
        return mRetrofit.create(StockApi.class);
    }
}
