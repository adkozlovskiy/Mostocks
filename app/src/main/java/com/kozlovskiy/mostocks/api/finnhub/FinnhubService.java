package com.kozlovskiy.mostocks.api.finnhub;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FinnhubService {

    public static final String TOKEN = "c0l8c7748v6orbr0u010";
    private static final String BASE_URL = "https://finnhub.io/api/v1/";
    private static FinnhubService instance;
    private final Retrofit mRetrofit;

    private FinnhubService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static FinnhubService getInstance() {
        if (instance == null) {
            instance = new FinnhubService();
        }

        return instance;
    }

    public FinnhubApi getApi() {
        return mRetrofit.create(FinnhubApi.class);
    }
}
