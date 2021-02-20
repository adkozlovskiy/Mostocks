package com.kozlovskiy.mostocks.room;

import androidx.room.RoomDatabase;

import com.kozlovskiy.mostocks.entities.StockCost;
import com.kozlovskiy.mostocks.entities.StockProfile;
import com.kozlovskiy.mostocks.entities.Ticker;

@androidx.room.Database(entities = {Ticker.class, StockProfile.class, StockCost.class}, version = 1, exportSchema = false)
public abstract class StocksDatabase extends RoomDatabase {
    public abstract StocksDao getDao();

}

