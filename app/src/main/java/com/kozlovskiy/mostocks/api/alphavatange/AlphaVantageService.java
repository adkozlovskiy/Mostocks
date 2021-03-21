package com.kozlovskiy.mostocks.api.alphavatange;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlphaVantageService {

    public static final String TOKEN = "AO9NDF3OU9FCIF2J";
    private static final String BASE_URL = "https://www.alphavantage.co/";
    private static AlphaVantageService instance;
    private final Retrofit mRetrofit;

    private AlphaVantageService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * @return {@link AlphaVantageService} instance.
     */
    public static AlphaVantageService getInstance() {
        if (instance == null) {
            instance = new AlphaVantageService();
        }

        return instance;
    }

    /**
     * @return API instance.
     */
    public AlphaVantageApi getApi() {
        return mRetrofit.create(AlphaVantageApi.class);
    }
}
