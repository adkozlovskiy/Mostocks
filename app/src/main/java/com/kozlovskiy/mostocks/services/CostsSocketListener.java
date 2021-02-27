package com.kozlovskiy.mostocks.services;

import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class CostsSocketListener extends WebSocketListener {

    public static final String TAG = CostsSocketListener.class.getSimpleName();
    private static final int NORMAL_CLOSURE_STATUS = 1000;

    @Override
    public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
        Log.d(TAG, "onOpen: ");
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
        Log.d(TAG, "onMessage: " + text);
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, ByteString bytes) {
        Log.d(TAG, "Receiving bytes : " + bytes.hex());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, @NonNull String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        Log.d(TAG, "Closing : " + code + " / " + reason);
    }

    @Override
    public void onFailure(@NonNull WebSocket webSocket, Throwable t, Response response) {
        Log.d(TAG, "Error : " + t.getMessage());
    }
}
