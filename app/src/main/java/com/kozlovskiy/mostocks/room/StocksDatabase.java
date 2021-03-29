package com.kozlovskiy.mostocks.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.kozlovskiy.mostocks.models.stock.Favorite;
import com.kozlovskiy.mostocks.models.stock.Stock;
import com.kozlovskiy.mostocks.models.stockInfo.News;
import com.kozlovskiy.mostocks.models.stockInfo.Recommendation;
import com.kozlovskiy.mostocks.models.sys.Uptime;

import static com.kozlovskiy.mostocks.models.stockInfo.IndicatorsResponse.Indicators;

@Database(entities = {Stock.class, Favorite.class, News.class, Uptime.class, Indicators.class, Recommendation.class}, version = 1, exportSchema = false)
public abstract class StocksDatabase extends RoomDatabase {

    public abstract StocksDao getDao();

}