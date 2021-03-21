package com.kozlovskiy.mostocks.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.google.gson.Gson;
import com.kozlovskiy.mostocks.entities.SocketData;
import com.kozlovskiy.mostocks.entities.SocketResponse;
import com.kozlovskiy.mostocks.services.websocket.WebSocketClient;
import com.kozlovskiy.mostocks.services.websocket.WebSocketConnection;
import com.kozlovskiy.mostocks.ui.main.fragments.stocks.StocksFragment;

public class WebSocketService extends Service implements WebSocketClient.MessageListener {

    public static final String TAG = WebSocketService.class.getSimpleName();
    public static final String KEY_TRADE = "trade";
    public static final String KEY_SYMBOL = "symbol";
    public static final String KEY_QUOTE = "quote";

    private final Intent intent = new Intent(StocksFragment.BROADCAST_ACTION);
    private final IBinder binder = new QuoteBinder();
    private final Gson gson = new Gson();

    private WebSocketConnection webSocketConnection;

    public class QuoteBinder extends Binder {
        public WebSocketService getInstance() {
            return WebSocketService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        webSocketConnection.closeConnection();
        return super.onUnbind(intent);
    }

    public void bindSocket(String symbol) {
        webSocketConnection = new WebSocketConnection(symbol);
        webSocketConnection.setListener(this);
        webSocketConnection.openConnection();
    }

    @Override
    public void onSocketMessage(String message) {
        try {
            SocketResponse response = gson.fromJson(message, SocketResponse.class);

            if (response.getType().equals(KEY_TRADE)) {
                SocketData data = response.getData().get(0);

                intent.putExtra(KEY_SYMBOL, data.getSymbol());
                intent.putExtra(KEY_QUOTE, data.getQuote());
                sendBroadcast(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}