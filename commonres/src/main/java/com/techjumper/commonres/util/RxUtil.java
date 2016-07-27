package com.techjumper.commonres.util;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kevin on 16/7/26.
 */

public class RxUtil {

    public static final int Time = 5000;

    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.throttleFirst(Time, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }



}
