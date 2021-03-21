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

public class ClientWebSocket {

    private static final String TAG = ClientWebSocket.class.getSimpleName();
    private final MessageListener listener;
    private final String host;
    private WebSocket webSocket;
    private final String ticker;

    public ClientWebSocket(MessageListener listener, String host, String ticker) {
        this.listener = listener;
        this.host = host;
        this.ticker = ticker;
    }

    public void connect() {
        new Thread(() -> {

            if (webSocket != null) {
                reconnect();
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
        }).start();
    }

    private void reconnect() {
        try {
            webSocket = webSocket.recreate().connect();
        } catch (WebSocketException | IOException e) {
            e.printStackTrace();
        }
    }

    public WebSocket getConnection() {
        return webSocket;
    }

    public void close() {
        if (webSocket != null) {
            webSocket.disconnect();
        }
    }

    public class SocketListener extends WebSocketAdapter {

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            super.onConnected(websocket, headers);

            Log.d(TAG, "onConnected: ");
            subscribe(ticker);
        }

        public void onTextMessage(WebSocket websocket, String message) {
            listener.onSocketMessage(message);
            Log.i(TAG, "Message --> " + message);
        }

        @Override
        public void onError(WebSocket websocket, WebSocketException cause) {
            Log.i(TAG, "Error -->" + cause.getMessage());
            reconnect();
        }

        @Override
        public void onDisconnected(WebSocket websocket,
                                   WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                                   boolean closedByServer) {
            Log.i(TAG, "onDisconnected");
            if (closedByServer) {
                reconnect();
            }
        }

        @Override
        public void onUnexpectedError(WebSocket websocket, WebSocketException cause) {
            Log.i(TAG, "Error -->" + cause.getMessage());
            reconnect();
        }

        @Override
        public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
            super.onPongFrame(websocket, frame);
            websocket.sendPing("Are you there?");
        }
    }

    public void subscribe(String ticker) {
        try {
            webSocket.sendText("{\"type\":\"subscribe\",\"symbol\":\"" + ticker + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface MessageListener {
        void onSocketMessage(String message);
    }
}
