package com.kozlovskiy.mostocks.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.kozlovskiy.mostocks.entities.SocketData;
import com.kozlovskiy.mostocks.entities.SocketResponse;
import com.kozlovskiy.mostocks.services.websocket.ClientWebSocket;
import com.kozlovskiy.mostocks.services.websocket.StockCostSocketConnection;
import com.kozlovskiy.mostocks.ui.main.fragments.stocks.StocksFragment;

public class CostMonitorService extends Service implements ClientWebSocket.MessageListener {

    public static final String TAG = CostMonitorService.class.getSimpleName();
    private final IBinder binder = new QuoteBinder();
    private StockCostSocketConnection stockCostSocketConnection;
    Intent intent = new Intent(StocksFragment.BROADCAST_ACTION);
    Gson gson = new Gson();

    @Override
    public void onSocketMessage(String message) {

        try {
            SocketResponse response = gson.fromJson(message, SocketResponse.class);
            if (response.getType().equals("trade")) {
                SocketData data = response.getData().get(0);

                String symbol = data.getS();
                double quote = data.getP();

                intent.putExtra("symbol", symbol);
                intent.putExtra("quote", quote);
                sendBroadcast(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class QuoteBinder extends Binder {
        public CostMonitorService getInstance() {
            return CostMonitorService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        stockCostSocketConnection = new StockCostSocketConnection("AAPL");
        stockCostSocketConnection.setListener(this);
        stockCostSocketConnection.openConnection();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return binder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind: ");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        stockCostSocketConnection.closeConnection();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
}