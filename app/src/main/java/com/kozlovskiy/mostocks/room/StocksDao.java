package com.kozlovskiy.mostocks.room;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.kozlovskiy.mostocks.entities.Cost;
import com.kozlovskiy.mostocks.entities.Favorite;
import com.kozlovskiy.mostocks.entities.Stock;

import java.util.List;

@androidx.room.Dao
public interface StocksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void cacheStocks(List<Stock> stocks);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFavorite(Favorite favorite);

    @Update()
    void updateStocks(List<Stock> stocks);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCost(List<Cost> costs);

    @Update()
    void updateCost(List<Cost> costs);

    @Query("SELECT * FROM Stock")
    List<Stock> getStocks();

    @Query("SELECT * FROM Favorite")
    List<Favorite> getFavorites();

    @Query("SELECT * FROM Cost")
    List<Cost> getCosts();

    @Query("SELECT * FROM Cost WHERE ticker = :ticker")
    Cost getCost(String ticker);
}
