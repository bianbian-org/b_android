package com.techjumper.polyhome.b.home.db;

import android.database.Cursor;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.basic.NumberUtil;
import com.techjumper.polyhome.b.home.sql.AdClick;
import com.techjumper.polyhome_b.adlib.entity.sql.AdStat;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by kevin on 16/8/3.
 */

public class AdClickDbExecutor {
    private volatile static BriteDatabaseHelper mDbHelper;

    private AdClickDbExecutor() {
    }

    public static BriteDatabaseHelper getHelper() {

        if (mDbHelper == null || mDbHelper.isClosed()) {
            synchronized (AdClickDbExecutor.class) {
                if (mDbHelper == null || mDbHelper.isClosed()) {
                    SqlBrite sqlBrite = SqlBrite.create();
                    BriteDatabase briteDatabase = sqlBrite.wrapDatabaseHelper(AdClickDbHelper.create(Utils.appContext)
                            , Schedulers.io());
                    mDbHelper = new BriteDatabaseHelper(briteDatabase);
                }
            }
        }
        return mDbHelper;
    }

    public static class BriteDatabaseHelper {

        private BriteDatabase mDb;

        private BriteDatabaseHelper(BriteDatabase briteDatabase) {
            mDb = briteDatabase;
        }

        public Observable<AdClick> query(String id) {
            return mDb.createQuery(AdClick.TABLE_NAME, AdClick.SELECT_BY_ADID, id)
                    .map(query -> {
                        Cursor cursor = query.run();
                        if (cursor == null || !cursor.moveToNext()) {
                            return null;
                        }
                        AdClick AdClick = null;
                        try {
                            AdClick = AdClick.MAPPER.map(cursor);
                        } finally {
                            cursor.close();
                        }
                        return AdClick;
                    })
                    .first();
        }

        public Observable<List<AdClick>> queryAll() {
            return mDb.createQuery(AdClick.TABLE_NAME, AdClick.SELECT_ALL)
                    .map(SqlBrite.Query::run)
                    .map(cursor -> {
                        List<AdClick> AdClicks = new ArrayList<>();
                        if (cursor == null) {
                            return AdClicks;
                        }
                        try {
                            while (cursor.moveToNext()) {
                                AdClicks.add(AdClick.MAPPER.map(cursor));
                            }
                        } finally {
                            cursor.close();
                        }
                        return AdClicks;
                    })
                    .first();
        }

        public int delete(String adId) {
            return mDb.delete(AdClick.TABLE_NAME, AdClick._ID + "=?", adId);
        }

        public boolean deleteAll() {
            return mDb.delete(AdClick.TABLE_NAME, null) == 1;
        }

        public synchronized void close() {
            try {
                mDb.close();
                mDb = null;
            } catch (Exception ignored) {
            }
        }

        public boolean isClosed() {
            return mDb == null;
        }

        private long insertAd(long adId, long familyId, String time, String position) {
            return mDb.insert(AdClick.TABLE_NAME
                    , AdClick.FACTORY.marshal()
                            .ad_id(adId)
                            .family_id(familyId)
                            .time(time)
                            .position(position)
                            .asContentValues()
            );
        }

        public Observable<Long> insert(long adId, long familyId, String time, String position) {
            long id = insertAd(adId, familyId, time, position);
            return query(String.valueOf(id))
                    .map(adClick -> {
                        return adClick._id();
                    });
        }
    }
}
