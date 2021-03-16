package com.kozlovskiy.mostocks;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.kozlovskiy.mostocks.room.StocksDatabase;
import com.kozlovskiy.mostocks.services.BackgroundManager;
import com.kozlovskiy.mostocks.services.StockCostSocketConnection;

public class AppDelegate extends Application {

    private StocksDatabase database;
    private StockCostSocketConnection stockCostSocketConnection;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(),
                StocksDatabase.class, "database").allowMainThreadQueries().build();
        stockCostSocketConnection = new StockCostSocketConnection(this);
        BackgroundManager.get(this).registerListener(appActivityListener);
    }

    public StocksDatabase getDatabase() {
        return database;
    }


    public void closeSocketConnection() {
        stockCostSocketConnection.closeConnection();
    }

    public void openSocketConnection() {
        stockCostSocketConnection.openConnection();
    }

    public boolean isSocketConnected() {
        return stockCostSocketConnection.isConnected();
    }

    public void reconnect() {
        stockCostSocketConnection.openConnection();
    }


    private BackgroundManager.Listener appActivityListener = new BackgroundManager.Listener() {
        public void onBecameForeground() {
            openSocketConnection();
            Log.i("Websocket", "Became Foreground");
        }

        public void onBecameBackground() {
            closeSocketConnection();
            Log.i("Websocket", "Became Background");
        }
    };

}
