package com.kozlovskiy.mostocks.room;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.kozlovskiy.mostocks.entities.Favorite;
import com.kozlovskiy.mostocks.entities.News;
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

    @Query("SELECT * FROM Stock")
    List<Stock> getStocks();

    @Query("SELECT * FROM Stock WHERE symbol = :symbol")
    Stock getStockBySymbol(String symbol);

    @Query("SELECT * FROM Favorite")
    List<Favorite> getFavorites();

    @Query("SELECT * FROM Favorite WHERE symbol = :symbol")
    Favorite getFavorite(String symbol);

    @Delete()
    void removeFavorite(Favorite favorite);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void cacheNews(List<News> newsList);
}
