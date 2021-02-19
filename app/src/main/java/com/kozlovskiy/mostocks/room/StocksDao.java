package com.kozlovskiy.mostocks.room;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.kozlovskiy.mostocks.entities.Stock;

import java.util.List;

@androidx.room.Dao
public interface StocksDao {

    @Query("SELECT * FROM Stock")
    List<Stock> getStocks();

    @Query("SELECT * FROM Stock WHERE id = :id")
    Stock getStockById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void cacheStocks(List<Stock> stockList);
}
