package com.kozlovskiy.mostocks.api.mstack;

import com.kozlovskiy.mostocks.entities.StockData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MStackApi {

    @GET("tickers?")
    Call<StockData> getStockData(@Query("exchange") String exchange, @Query("limit") String limit, @Query("access_key") String token);
}
