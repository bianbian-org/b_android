package com.techjumper.commonres.util;

import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/7/26.
 */

public class RxUtil {
    public static final int Time = 1500;

    public static Observable click(View view) {
        return RxView.clicks(view)
                .throttleFirst(Time, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
