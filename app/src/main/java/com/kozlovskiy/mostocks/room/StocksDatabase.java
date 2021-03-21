package com.kozlovskiy.mostocks.room;

import androidx.room.RoomDatabase;

import com.kozlovskiy.mostocks.entities.Favorite;
import com.kozlovskiy.mostocks.entities.News;
import com.kozlovskiy.mostocks.entities.Stock;

@androidx.room.Database(entities = {Stock.class, Favorite.class, News.class}, version = 1, exportSchema = false)
public abstract class StocksDatabase extends RoomDatabase {

    public abstract StocksDao getDao();
}