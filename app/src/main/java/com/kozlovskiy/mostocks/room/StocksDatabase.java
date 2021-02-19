package com.kozlovskiy.mostocks.room;

import androidx.room.RoomDatabase;

import com.kozlovskiy.mostocks.entities.Stock;

@androidx.room.Database(entities = {Stock.class}, version = 1, exportSchema = false)
public abstract class StocksDatabase extends RoomDatabase {
    public abstract StocksDao getDao();
}

