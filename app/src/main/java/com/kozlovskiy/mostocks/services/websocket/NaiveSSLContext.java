package com.kozlovskiy.mostocks.services.websocket;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class NaiveSSLContext {

    private NaiveSSLContext() {

    }

    public static SSLContext getInstance(String protocol) throws NoSuchAlgorithmException {
        return init(SSLContext.getInstance(protocol));
    }

    private static SSLContext init(SSLContext context) {
        try {
            context.init(null, new TrustManager[]{new NaiveTrustManager()}, null);

        } catch (KeyManagementException e) {
            throw new RuntimeException("Failed to initialize an SSLContext.", e);
        }

        return context;
    }

    // TODO: 21.03.2021 скопировал код на траст менеджер, надо понять как он работает вообще :)
    private static class NaiveTrustManager implements X509TrustManager {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }


        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }
}