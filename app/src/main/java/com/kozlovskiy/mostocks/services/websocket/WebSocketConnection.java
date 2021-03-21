package com.kozlovskiy.mostocks.services.websocket;

import android.os.Handler;
import android.util.Log;

public class WebSocketConnection {

    public static final String TAG = WebSocketConnection.class.getSimpleName();
    public static final String HOST = "wss://ws.finnhub.io?token=c0l8c7748v6orbr0u010";
    public static final Long DELAY = 5000L;
    private final Handler socketConnectionHandler;
    private final String symbol;

    private WebSocketClient webSocketClient;
    private WebSocketClient.MessageListener listener;

    public void setListener(WebSocketClient.MessageListener listener) {
        this.listener = listener;
    }

    public WebSocketConnection(String symbol) {
        this.symbol = symbol;
        socketConnectionHandler = new Handler();
    }

    private final Runnable checkConnectionRunnable = () -> {
        if (webSocketClient == null) {
            Log.d(TAG, ": hello from there!");
            openConnection();
        }

        startCheckConnection();
    };

    private void startCheckConnection() {
        socketConnectionHandler.postDelayed(checkConnectionRunnable, DELAY);
    }

    private void stopCheckConnection() {
        socketConnectionHandler.removeCallbacks(checkConnectionRunnable);
    }

    public void openConnection() {
        try {
            webSocketClient = new WebSocketClient(listener, HOST, symbol);
            webSocketClient.openConnection();
            Log.d(TAG, "openConnection: ");

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "openConnection: ");
        }

        startCheckConnection();
    }

    public void closeConnection() {
        if (webSocketClient != null) {
            webSocketClient.closeConnection();
            webSocketClient = null;
        }

        stopCheckConnection();
    }
}