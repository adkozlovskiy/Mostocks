package com.kozlovskiy.mostocks.services.websocket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.kozlovskiy.mostocks.services.websocket.ClientWebSocket;

public class StockCostSocketConnection implements ClientWebSocket.MessageListener {
    private ClientWebSocket clientWebSocket;
    private final Context context;
    private final Handler socketConnectionHandler;
    private String ticker;

    private final Runnable checkConnectionRunnable = () -> {
        if (!clientWebSocket.getConnection().isOpen()) {
            openConnection(ticker);
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

    public void openConnection(String ticker) {
        this.ticker = ticker;
        if (clientWebSocket != null) clientWebSocket.close();
        try {
            clientWebSocket = new ClientWebSocket(this,
                    "wss://ws.finnhub.io?token=c0l8c7748v6orbr0u010", ticker);
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
                openConnection(ticker);
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