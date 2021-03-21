package com.kozlovskiy.mostocks;

import android.app.Application;

import androidx.room.Room;

import com.kozlovskiy.mostocks.room.StocksDatabase;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class AppDelegate extends Application {

    private StocksDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(),
                StocksDatabase.class, "database").allowMainThreadQueries().build();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));

        Picasso built = builder.build();
        Picasso.setSingletonInstance(built);
    }

    public StocksDatabase getDatabase() {
        return database;
    }
}
