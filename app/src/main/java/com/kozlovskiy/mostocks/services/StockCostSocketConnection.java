package com.kozlovskiy.mostocks.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

public class StockCostSocketConnection implements ClientWebSocket.MessageListener {
    private ClientWebSocket clientWebSocket;
    private final Context context;
    public Gson gson = new Gson();
    private final Handler socketConnectionHandler;

    private final Runnable checkConnectionRunnable = () -> {
        if (!clientWebSocket.getConnection().isOpen()) {
            openConnection();
        }
        startCheckConnection();
    };

    private void startCheckConnection() {
        socketConnectionHandler.postDelayed(checkConnectionRunnable, 5000);
    }

    private void stopCheckConnection() {
        socketConnectionHandler.removeCallbacks(checkConnectionRunnable);
    }

    public StockCostSocketConnection(Context context) {
        this.context = context;
        socketConnectionHandler = new Handler();
    }

    public void openConnection() {
        if (clientWebSocket != null) clientWebSocket.close();
        try {
            clientWebSocket = new ClientWebSocket(this,
                    "wss://ws.finnhub.io?token=c0l8c7748v6orbr0u010");
            clientWebSocket.connect();

            Log.i("Websocket", "Socket connected by user ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        initScreenStateListener();
        startCheckConnection();
    }

    public void closeConnection() {
        if (clientWebSocket != null) {
            clientWebSocket.close();
            clientWebSocket = null;
        }
        releaseScreenStateListener();
        stopCheckConnection();
    }


    @Override
    public void onSocketMessage(String message) {
        Log.d("websocket", "onSocketMessage: mes" + message);
    }

    /**
     * Screen state listener for socket live cycle
     */
    private void initScreenStateListener() {
        context.registerReceiver(screenStateReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        context.registerReceiver(screenStateReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    private void releaseScreenStateListener() {
        try {
            context.unregisterReceiver(screenStateReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver screenStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.i("Websocket", "Screen ON");
                openConnection();
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.i("Websocket", "Screen OFF");
                closeConnection();
            }
        }
    };

    public boolean isConnected() {
        return clientWebSocket != null &&
                clientWebSocket.getConnection() != null &&
                clientWebSocket.getConnection().isOpen();
    }
}