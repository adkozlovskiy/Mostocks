package com.kozlovskiy.mostocks.services.websocket;

import android.os.Handler;
import android.util.Log;

public class StockCostSocketConnection {
    private ClientWebSocket clientWebSocket;
    private final Handler socketConnectionHandler;
    private final String ticker;
    private ClientWebSocket.MessageListener listener;

    public void setListener(ClientWebSocket.MessageListener listener) {
        this.listener = listener;
    }

    private final Runnable checkConnectionRunnable = () -> {
        if (clientWebSocket == null || !clientWebSocket.getConnection().isOpen()) {
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

    public StockCostSocketConnection(String ticker) {
        this.ticker = ticker;
        socketConnectionHandler = new Handler();
    }

    public void openConnection() {
        if (clientWebSocket != null) clientWebSocket.close();
        try {
            clientWebSocket = new ClientWebSocket(listener,
                    "wss://ws.finnhub.io?token=c0l8c7748v6orbr0u010", ticker);
            clientWebSocket.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        startCheckConnection();
    }

    public void closeConnection() {
        if (clientWebSocket != null) {
            clientWebSocket.close();
            clientWebSocket = null;
        }
        stopCheckConnection();
    }
}