package com.kozlovskiy.mostocks;

import android.app.Application;

import androidx.room.Room;

import com.kozlovskiy.mostocks.entities.Stock;
import com.kozlovskiy.mostocks.room.StocksDatabase;

import java.util.ArrayList;
import java.util.List;

public class AppDelegate extends Application {

    private StocksDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(),
                StocksDatabase.class, "database").allowMainThreadQueries().build();
    }

    public StocksDatabase getDatabase() {
        return database;
    }
}
