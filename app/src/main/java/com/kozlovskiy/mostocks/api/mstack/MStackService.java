package com.kozlovskiy.mostocks.api.mstack;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MStackService {

    public static final String TOKEN = "1da05620a1c9f3f9eb22706683a3ad1c";
    private static final String BASE_URL = "http://api.marketstack.com/v1/";
    private static MStackService instance;
    private final Retrofit mRetrofit;

    private MStackService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static MStackService getInstance() {
        if (instance == null) {
            instance = new MStackService();
        }

        return instance;
    }

    public MStackApi getApi() {
        return mRetrofit.create(MStackApi.class);
    }
}
