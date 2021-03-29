package com.kozlovskiy.mostocks.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.kozlovskiy.mostocks.models.socket.SocketResponse;
import com.kozlovskiy.mostocks.room.RoomDelegate;
import com.kozlovskiy.mostocks.services.websocket.WebSocketClient;
import com.kozlovskiy.mostocks.services.websocket.WebSocketConnection;

import java.util.List;

public class WebSocketService extends Service implements WebSocketClient.MessageListener {

    public static final String TAG = WebSocketService.class.getSimpleName();
    public static final String KEY_TRADE = "trade";

    private final Gson gson = new Gson();

    private WebSocketConnection webSocketConnection;
    private RoomDelegate roomDelegate;

    @Override
    public void onCreate() {
        roomDelegate = new RoomDelegate(this);
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<String> symbols = intent.getStringArrayListExtra("symbols");
        webSocketConnection = new WebSocketConnection(symbols);
        webSocketConnection.setListener(this);
        webSocketConnection.openConnection();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webSocketConnection != null)
            webSocketConnection.closeConnection();
    }

    @Override
    public void onSocketMessage(String message) {
        try {
            SocketResponse response = gson.fromJson(message, SocketResponse.class);

            if (response.getType().equals(KEY_TRADE)) {
                SocketResponse.Data data = response.getData().get(0);
                roomDelegate.updateStockQuote(data.getSymbol(), data.getQuote());
                Log.d(TAG, "onSocketMessage: updated current quote for " + data.getSymbol() + "= " + data.getQuote());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}