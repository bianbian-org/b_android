package com.techjumper.lib2.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.litesuits.orm.db.assit.SQLiteHelper;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.JLog;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/2/16
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

/**
 * 通过RxJava封装了LiteOrm
 */
public class RxORM {

    //    private static volatile LiteHelper INSTANCE;
    public final static String DB_NAME = "jumper.db";
    private static int DB_VERSION = 1;

    private RxORM() {
    }

//    public static LiteHelper getInstance() {
//        if (INSTANCE == null) {
//            synchronized (LiteHelper.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = new LiteHelper();
//                }
//            }
//        }
//        return INSTANCE;
//    }

    public static void setDefaultDbVersion(int version) {
        DB_VERSION = version;
    }


//    /**
//     * 得到默认的LiteOrm
//     */
//    private LiteOrm getLiteOrm() {
//        return getLiteOrm(null);
//    }
//
//    public LiteOrm getLiteOrm(SQLiteHelper.OnUpdateListener updateListener) {
//        return getLiteOrm(DB_NAME, DB_VERSION, updateListener);
//    }

    private static LiteOrm getLiteOrm(String name, int version, SQLiteHelper.OnUpdateListener updateListener) {
        DataBaseConfig config = new DataBaseConfig(Utils.appContext, getDbName(name), version, updateListener);
        return LiteOrm.newSingleInstance(config);
    }


    /**
     * 得到默认的LiteOrm Observable
     */
    public static Observable<LiteOrm> asObservable() {
        return asObservable(null);
    }

    public static Observable<LiteOrm> asObservable(SQLiteHelper.OnUpdateListener updateListener) {
        return asObservable(DB_NAME, DB_VERSION, updateListener);
    }

    public static Observable<LiteOrm> asObservable(String name, int version, SQLiteHelper.OnUpdateListener updateListener) {
        return Observable
                .create(new Observable.OnSubscribe<LiteOrm>() {
                    @Override
                    public void call(Subscriber<? super LiteOrm> subscriber) {
                        LiteOrm liteOrm = getLiteOrm(name, version, updateListener);
                        subscriber.onNext(liteOrm);
                        subscriber.onCompleted();
                        subscriber.add(Subscriptions.create(() -> {
                            try {
                                liteOrm.close();
                            } catch (Exception ignored) {
                            }
                        }));
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    private static String getDbName(String name) {
        return TextUtils.isEmpty(name) ? DB_NAME : name;
    }

//    public static void close(LiteOrm liteOrm) {
//        try {
//            if (liteOrm != null)
//                liteOrm.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
