package com.kozlovskiy.mostocks.api.mstack;

import com.kozlovskiy.mostocks.models.stock.StockResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MStackApi {

    @GET("tickers?")
    Call<StockResponse> getStockData(@Query("exchange") String exchange, @Query("limit") String limit, @Query("access_key") String token);
}
