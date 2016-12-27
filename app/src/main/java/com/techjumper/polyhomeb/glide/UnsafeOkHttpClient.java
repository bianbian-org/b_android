package com.techjumper.polyhomeb.glide;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/27
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class UnsafeOkHttpClient {
    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] x509Certificates = new X509Certificate[0];
                            return x509Certificates;
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
            okHttpClient.sslSocketFactory(sslSocketFactory);
            okHttpClient.protocols(Arrays.asList(Protocol.HTTP_1_1));
            okHttpClient.hostnameVerifier((hostname, session) -> true);

            return okHttpClient.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
