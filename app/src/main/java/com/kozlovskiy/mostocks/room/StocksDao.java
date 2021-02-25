package com.kozlovskiy.mostocks.room;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.entities.StockCost;

import java.util.List;

@androidx.room.Dao
public interface StocksDao {

    @Query("SELECT * FROM StockCost WHERE ticker = :ticker")
    StockCost getStockCost(String ticker);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void cacheStocks(List<Stock> stocks);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateStocks(List<Stock> stocks);

    @Query("SELECT * FROM Stock")
    List<Stock> getStocks();
}
