package com.kozlovskiy.mostocks.api;

import com.kozlovskiy.mostocks.entities.Cost;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StocksService {

    @GET("quote?")
    Observable<Cost> getStockCost(
            @Query("symbol") String symbol,
            @Query("token") String token
    );
}
