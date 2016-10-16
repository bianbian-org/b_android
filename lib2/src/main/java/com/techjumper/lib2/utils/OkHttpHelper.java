package com.techjumper.lib2.utils;

import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.file.FileUtils;
import com.techjumper.corelib.utils.system.AppUtils;
import com.techjumper.lib2.Config;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/2/19
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class OkHttpHelper {

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {


        Request request = chain.request();

        // Add Cache Control only for GET methods
        if (AppUtils.isNetworkAvailable()) {
            request.newBuilder()
                    .header("Accept", "application/json;versions=1")
                    .header("Cache-Control", "public, max-age=" + 120)
                    .build();
        } else {
            request.newBuilder()
                    .header("Accept", "application/json;versions=1")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 28)
                    .build();
        }

        Response response = chain.proceed(request);
        // Re-write response CC header to force use of cache

//        JLog.d("HTTP返回: " + response.body().string());
        return response.newBuilder()
                .header("Cache-Control", "public, max-age=" + 120)
                .build();

//        Response originalResponse = chain.proceed(chain.request());
//        if (AppUtils.isNetworkAvailable()) {
//            return originalResponse.newBuilder()
//                    .header("Cache-Control", "public, max-age=" + 120)
////                    .header("Cache-Control", String.format("max-age=%d, only-if-cached, max-stale=%d", 120, 0))
//                    .build();
//        }
//        return originalResponse.newBuilder()
//                .header("Cache-Control", "public, only-if-cached, max-stale=" + 120)
//                .build();
    };

    private static OkHttpClient mClient;


    public static OkHttpClient getDefault() {
        if (mClient == null) {
            synchronized (OkHttpHelper.class) {
                if (mClient == null) {
                    mClient = create();
                }
            }
        }
        return mClient;
    }

    private static OkHttpClient create() {

        int cacheSize;
        String cachePath = FileUtils.getCacheDir();
        if (FileUtils.isSDCardMounted()) {
            cacheSize = 500 * 1024 * 1024; //500mb
        } else {
            cacheSize = 20 * 1024 * 1024;//20mb
        }

        InputStream inputStream = new Buffer().writeUtf8(CER_MEDICAL).inputStream();
        InputStream[] inputStreams = new InputStream[]{inputStream};
        HTTPSUtils.SSLParams sslParams = HTTPSUtils.getSslSocketFactory(inputStreams, null, null);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .hostnameVerifier((hostname, session) -> true)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
//                .cache(new Cache(new File(cachePath), cacheSize))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        if (Config.sIsDebug) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
            JLog.d("开启HTTP日志");
        }
        builder.networkInterceptors().add(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        return builder.build();
    }

    /**
     * 医疗网页证书
     */
    public static final String CER_MEDICAL = "PEM encoded chain: -----BEGIN CERTIFICATE-----\n" +
            "MIICGzCCAYQCCQDCWPUrzNwBFDANBgkqhkiG9w0BAQUFADBSMQswCQYDVQQGEwJD\n" +
            "TjEQMA4GA1UECAwHQmVpamluZzEQMA4GA1UEBwwHQmVpamluZzESMBAGA1UECgwJ\n" +
            "TmV3IFJlYWNoMQswCQYDVQQLDAJJVDAeFw0xNDA1MjEwNzU1NDdaFw0yNDA1MTgw\n" +
            "NzU1NDdaMFIxCzAJBgNVBAYTAkNOMRAwDgYDVQQIDAdCZWlqaW5nMRAwDgYDVQQH\n" +
            "DAdCZWlqaW5nMRIwEAYDVQQKDAlOZXcgUmVhY2gxCzAJBgNVBAsMAklUMIGfMA0G\n" +
            "CSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGHM75uAj4KkvXPt6P9vnJeBq8NGvvQA1F\n" +
            "FI89SM1RhJEr4Qf4KF3ufeoJ2HBURFlcbfePca6f2LdCDkgZb2KA6x6SKvWiBjXM\n" +
            "WqVViTXl8XP0VnCfp4y+EU6OeucwBwEmJ7CJQBCZdWAUbj0OIOmnZ3yxAnIXuB/s\n" +
            "ZWJO6BFZlQIDAQABMA0GCSqGSIb3DQEBBQUAA4GBAB7dA7eiU4/hNYQ+qGKEYi/f\n" +
            "2PE4O7wCsvXeVYiRg5XXISld26r6dAD5rHgukI/rFzRCBEWqCKN5UIZL4Vizu7lR\n" +
            "bNCBSo9+NPYLyq91D4xEQ4XFuzHrzkFJiv2iyKxlRSguZaxkEjbSJruExygjLdx5\n" +
            "dX1Ziyc7cr9nKftZ6AY+\n" +
            "-----END CERTIFICATE-----";


}
