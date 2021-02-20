package com.kozlovskiy.mostocks.room;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.kozlovskiy.mostocks.entities.StockCost;
import com.kozlovskiy.mostocks.entities.StockProfile;
import com.kozlovskiy.mostocks.entities.Ticker;

import java.util.List;

@androidx.room.Dao
public interface StocksDao {

    @Query("SELECT * FROM StockCost WHERE ticker = :ticker")
    StockCost getStockCost(String ticker);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void cacheStockCosts(List<StockCost> stockCosts);

    @Query("SELECT * FROM StockProfile WHERE ticker = :ticker")
    StockProfile getStockProfile(String ticker);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void cacheStockProfiles(List<StockProfile> stockProfiles);

    @Query("SELECT * FROM Ticker")
    List<Ticker> getTickers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void cacheTickers(List<Ticker> tickers);
}
