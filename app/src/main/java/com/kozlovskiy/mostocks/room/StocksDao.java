package com.kozlovskiy.mostocks.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.kozlovskiy.mostocks.models.stock.Favorite;
import com.kozlovskiy.mostocks.models.stockInfo.News;
import com.kozlovskiy.mostocks.models.stock.Stock;
import com.kozlovskiy.mostocks.models.sys.Uptime;

import java.util.List;

@Dao
public interface StocksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void cacheStocks(List<Stock> stocks);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFavorite(Favorite favorite);

    @Update()
    void updateStocks(List<Stock> stocks);

    @Query("SELECT * FROM Stock")
    List<Stock> getStocks();

    @Query("SELECT * FROM Favorite")
    List<Favorite> getFavorites();

    @Query("SELECT * FROM Favorite WHERE symbol = :symbol")
    Favorite getFavorite(String symbol);

    @Delete()
    void removeFavorite(Favorite favorite);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void cacheNews(List<News> newsList);

    @Query("SELECT * FROM News WHERE symbol = :symbol ORDER BY datetime DESC")
    List<News> getNewsBySymbol(String symbol);

    @Query("DELETE FROM News")
    void clearNewsCache();

    @Query("UPDATE Uptime SET news_ut = 0")
    void nullNewsUptime();

    @Query("UPDATE Uptime SET news_ut = :ut WHERE symbol = :symbol")
    void setNewsUptime(String symbol, long ut);

    @Query("SELECT * FROM Uptime WHERE symbol = :symbol")
    Uptime getUptimeSymbol(String symbol);

    @Insert()
    void addUptimeSymbol(Uptime uptime);

    @Query("SELECT news_ut FROM Uptime WHERE symbol = :symbol")
    long getNewsUptime(String symbol);
}
