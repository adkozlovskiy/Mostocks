package com.kozlovskiy.mostocks.services.websocket;

import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

public class WebSocketClient {

    private static final String TAG = WebSocketClient.class.getSimpleName();
    private final MessageListener listener;
    private final String host;
    private final String symbol;

    private WebSocket webSocket;

    public interface MessageListener {
        void onSocketMessage(String message);
    }

    public WebSocketClient(MessageListener listener, String host, String symbol) {
        this.listener = listener;
        this.host = host;
        this.symbol = symbol;
    }

    public void openConnection() {
        Thread connectionThread = new Thread(() -> {
            if (webSocket != null) {
                reopenConnection();
                Log.d(TAG, "openConnection: reopen !!!");

            } else {
                try {
                    WebSocketFactory factory = new WebSocketFactory();

                    SSLContext context = NaiveSSLContext.getInstance("TLS");
                    factory.setSSLContext(context);

                    webSocket = factory.createSocket(host);
                    webSocket.addListener(new SocketListener());
                    webSocket.connect();

                } catch (WebSocketException | IOException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });

        connectionThread.start();
    }

    public WebSocket getConnection() {
        return webSocket;
    }

    public void reopenConnection() {
        try {
            webSocket = webSocket.recreate().connect();

        } catch (WebSocketException | IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        webSocket.disconnect();
    }

    public void subscribe(String symbol) {
        try {
            Log.d(TAG, "subscribe: subscribed to " + symbol);
            webSocket.sendText("{\"type\":\"subscribe\",\"symbol\":\"" + symbol + "\"}");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class SocketListener extends WebSocketAdapter {

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            super.onConnected(websocket, headers);

            Log.d(TAG, "onConnected: ");
            subscribe(symbol);
        }

        public void onTextMessage(WebSocket websocket, String message) {
            listener.onSocketMessage(message);
            Log.i(TAG, "Message --> " + message);
        }

        @Override
        public void onError(WebSocket websocket, WebSocketException cause) {
            Log.i(TAG, "Error -->" + cause.getMessage());
            reopenConnection();
        }

        @Override
        public void onDisconnected(WebSocket websocket,
                                   WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                                   boolean closedByServer) {
            Log.i(TAG, "onDisconnected");

            if (closedByServer) {
                Log.d(TAG, "onDisconnected: reopen connection.");
                reopenConnection();
            }
        }

        @Override
        public void onUnexpectedError(WebSocket websocket, WebSocketException cause) {
            Log.i(TAG, "Error -->" + cause.getMessage());
            reopenConnection();
        }

        @Override
        public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
            super.onPongFrame(websocket, frame);
            websocket.sendPing("Are you there?");
        }
    }
}
