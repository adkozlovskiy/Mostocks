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
    private final List<String> symbols;

    private WebSocket webSocket;

    public interface MessageListener {
        void onSocketMessage(String message);
    }

    public WebSocketClient(MessageListener listener, String host, List<String> symbols) {
        this.listener = listener;
        this.host = host;
        this.symbols = symbols;
    }

    public void openConnection() {
        Thread connectionThread = new Thread(() -> {
            if (webSocket != null)
                reopenConnection();

            else try {
                WebSocketFactory factory = new WebSocketFactory();

                SSLContext context = NaiveSSLContext.getInstance("TLS");
                factory.setSSLContext(context);

                webSocket = factory.createSocket(host);
                webSocket.addListener(new SocketListener());
                webSocket.connect();

            } catch (WebSocketException | IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        });

        connectionThread.start();
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

    public void subscribe(List<String> symbols) {
        for (String symbol : symbols) {
            Log.d(TAG, "subscribe: in loop");
            try {
                webSocket.sendText("{\"type\":\"subscribe\",\"symbol\":\"" + symbol + "\"}");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class SocketListener extends WebSocketAdapter {

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            super.onConnected(websocket, headers);
            subscribe(symbols);
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
            if (closedByServer)
                reopenConnection();

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
