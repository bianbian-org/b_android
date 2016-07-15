package com.techjumper.lib2.utils;

import android.graphics.Bitmap;
import android.net.Uri;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.file.FileUtils;
import com.techjumper.lib2.Config;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/2/19
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PicassoHelper {

    private static Picasso mPicasso = new Picasso.Builder(Utils.appContext)
            .downloader(new OkHttp3Downloader(create()))
            .build();

    private static OkHttpClient create() {
        int cacheSize;
        String cachePath = FileUtils.getCacheDir();
        if (FileUtils.isSDCardMounted()) {
            cacheSize = 100 * 1024 * 1024;
        } else {
            cacheSize = 20 * 1024 * 1024;
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(new Cache(new File(cachePath), cacheSize))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        if (Config.sIsDebug) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
            JLog.d("开启HTTP日志");
        }
        return builder.build();
    }

    public static RequestCreator load(String path) {
        RequestCreator creator = mPicasso.load(path);
        return createDefault(creator);
    }


    public static RequestCreator load(File file) {
        RequestCreator creator = mPicasso.load(file);
        return createDefault(creator);
    }


    public static RequestCreator load(int resId) {
        RequestCreator creator = mPicasso.load(resId);
        return createDefault(creator);
    }

    public static RequestCreator load(Uri uri) {
        RequestCreator creator = mPicasso.load(uri);
        return createDefault(creator);
    }

    public static Picasso getDefault() {
        return mPicasso;
    }

    private static RequestCreator createDefault(RequestCreator creator) {
        return creator.config(Bitmap.Config.RGB_565);
    }
}
