package com.kozlovskiy.mostocks.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.kozlovskiy.mostocks.models.stock.Favorite;
import com.kozlovskiy.mostocks.models.stock.Stock;
import com.kozlovskiy.mostocks.models.stockInfo.IndicatorsResponse;
import com.kozlovskiy.mostocks.models.stockInfo.News;
import com.kozlovskiy.mostocks.models.stockInfo.Recommendation;
import com.kozlovskiy.mostocks.models.sys.Uptime;

import java.util.List;

@Dao
public interface StocksDao {

    /*
    Stocks
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void cacheStocks(List<Stock> stocks);

    @Update()
    void updateStocks(List<Stock> stocks);

    @Query("SELECT * FROM Stock")
    List<Stock> getStocks();

    /*
    Quote
     */

    @Query("UPDATE Stock SET current = :quote WHERE symbol = :symbol")
    void updateStockQuote(String symbol, Double quote);


    /*
    Favorites
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFavorite(Favorite favorite);

    @Query("SELECT * FROM Favorite")
    List<Favorite> getFavorites();

    @Query("SELECT * FROM Favorite WHERE symbol = :symbol")
    Favorite getFavorite(String symbol);

    @Delete()
    void removeFavorite(Favorite favorite);

    /*
    News
     */

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void cacheNews(List<News> newsList);

    @Query("SELECT * FROM News WHERE symbol = :symbol ORDER BY datetime DESC")
    List<News> getNewsBySymbol(String symbol);

    @Query("DELETE FROM News")
    void clearNewsCache();

    /*
    Uptime global
     */

    @Insert()
    void addUptimeSymbol(Uptime uptime);

    @Query("SELECT * FROM Uptime WHERE symbol = :symbol")
    Uptime getUptimeSymbol(String symbol);

    /*
    Uptime news
     */

    @Query("UPDATE Uptime SET news_ut = :ut WHERE symbol = :symbol")
    void setNewsUptime(String symbol, long ut);

    @Query("SELECT news_ut FROM Uptime WHERE symbol = :symbol")
    long getNewsUptime(String symbol);

    @Query("UPDATE Uptime SET news_ut = 0")
    void nullNewsUptime();

    /*
    Uptime quote
     */

    @Query("UPDATE Uptime SET quote_ut = :ut WHERE symbol = :symbol")
    void setQuoteUptime(String symbol, long ut);

    @Query("SELECT quote_ut FROM Uptime WHERE symbol = :symbol")
    long getQuoteUptime(String symbol);

    @Query("UPDATE Uptime SET quote_ut = 0")
    void nullQuoteUptime();

    /*
    Indicators
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void cacheIndicators(IndicatorsResponse.Indicators indicators);

    @Query("SELECT * FROM Indicators WHERE symbol = :symbol")
    IndicatorsResponse.Indicators getIndicatorsBySymbol(String symbol);

    /*
    Indicators uptime
     */

    @Query("UPDATE Uptime SET indicators_ut = :ut WHERE symbol = :symbol")
    void setIndicatorsUptime(String symbol, long ut);

    @Query("SELECT indicators_ut FROM Uptime WHERE symbol = :symbol")
    long getIndicatorsUptime(String symbol);

    /*
    Forecasts
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void cacheRecommendations(List<Recommendation> recommendations);

    @Query("SELECT * FROM Recommendation WHERE symbol = :symbol")
    List<Recommendation> getRecommendationsBySymbol(String symbol);

    /*
    Forecasts uptime
     */

    @Query("UPDATE Uptime SET rec_ut = :ut WHERE symbol = :symbol")
    void setRecommendationUptime(String symbol, long ut);

    @Query("SELECT rec_ut FROM Uptime WHERE symbol = :symbol")
    long getRecommendationUptime(String symbol);

}
