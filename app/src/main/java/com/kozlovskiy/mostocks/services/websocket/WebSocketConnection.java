package com.kozlovskiy.mostocks.services.websocket;

import android.os.Handler;

import java.util.List;

public class WebSocketConnection {

    public static final String HOST = "wss://ws.finnhub.io?token=c0l8c7748v6orbr0u010";
    public static final String TAG = WebSocketConnection.class.getSimpleName();
    private final Handler socketConnectionHandler;
    public static final Long DELAY = 5000L;
    private final List<String> symbols;

    private WebSocketClient.MessageListener listener;
    private WebSocketClient webSocketClient;

    public void setListener(WebSocketClient.MessageListener listener) {
        this.listener = listener;
    }

    public WebSocketConnection(List<String> symbols) {
        this.symbols = symbols;
        socketConnectionHandler = new Handler();
    }

    private final Runnable checkConnectionRunnable = () -> {
        if (webSocketClient == null)
            openConnection();

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
            webSocketClient = new WebSocketClient(listener, HOST, symbols);
            webSocketClient.openConnection();

        } catch (Exception e) {
            e.printStackTrace();
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