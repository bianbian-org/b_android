package com.techjumper.polyhome_b.adlib.db.utils;

import android.database.Cursor;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.basic.NumberUtil;
import com.techjumper.polyhome_b.adlib.db.AdStatDbHelper;
import com.techjumper.polyhome_b.adlib.entity.sql.AdStat;
import com.techjumper.polyhome_b.adlib.entity.sql.AdStatTime;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

public class AdStatDbExecutor {

    private volatile static BriteDatabaseHelper mDbHelper;

    private AdStatDbExecutor() {
    }

    public static BriteDatabaseHelper getHelper() {

        if (mDbHelper == null || mDbHelper.isClosed()) {
            synchronized (AdStatDbExecutor.class) {
                if (mDbHelper == null || mDbHelper.isClosed()) {
                    SqlBrite sqlBrite = SqlBrite.create();
                    BriteDatabase briteDatabase = sqlBrite.wrapDatabaseHelper(AdStatDbHelper.create(Utils.appContext)
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

        public Observable<AdStat> query(String adId, String type) {
            return mDb.createQuery(AdStat.TABLE_NAME, AdStat.SELECT_BY_ADID_AND_TYPE, adId, type)
                    .map(query -> {
                        Cursor cursor = query.run();
                        if (cursor == null || !cursor.moveToNext()) {
                            return null;
                        }
                        AdStat adStat = null;
                        try {
                            adStat = AdStat.MAPPER.map(cursor);
                        } finally {
                            cursor.close();
                        }
                        return adStat;
                    })
                    .onErrorResumeNext(throwable -> {
                        try {
                            deleteAll();
                        } catch (Exception ignored) {
                        }
                        return null;
                    })
                    .first();
        }

        public Observable<List<AdStat>> queryAll() {
            return mDb.createQuery(AdStat.TABLE_NAME, AdStat.SELECT_ALL)
                    .map(SqlBrite.Query::run)
                    .map(cursor -> {
                        List<AdStat> adStats = new ArrayList<>();
                        if (cursor == null) {
                            return adStats;
                        }
                        try {
                            while (cursor.moveToNext()) {
                                adStats.add(AdStat.MAPPER.map(cursor));
                            }
                        } finally {
                            cursor.close();
                        }
                        return adStats;
                    })
                    .onErrorResumeNext(throwable -> {
                        try {
                            deleteAll();
                        } catch (Exception ignored) {
                        }
                        return null;
                    })
                    .first();
        }

        public Observable<Long> insertOrUpdate(String adId, int count, String type) {
            return query(adId, type)
                    .map(adStat -> {
                        if (adStat == null) {
                            return insert(adId, count, type);
                        }
                        return (long) update(adId, count, type);
                    });
        }

        public Observable<Long> increase(String adId, String type) {
            return query(adId, type)
                    .map(adStat -> {
                        if (adStat == null) {
                            return insert(adId, 1, type);
                        }
                        return (long) update(adId, adStat.count() + 1L, type);
                    });
        }

        public int delete(String adId, String type) {
            return mDb.delete(AdStat.TABLE_NAME, AdStat.ADID + "=? and " + AdStat.POSITION + " =?", adId, type);
        }

        public boolean deleteAll() {
            clearTime();
            return mDb.delete(AdStat.TABLE_NAME, null) == 1;
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

        private long insert(String adId, int count, String type) {
            return
                    mDb.insert(AdStat.TABLE_NAME
                            , AdStat.FACTORY.marshal()
                                    .adId(NumberUtil.convertTolong(adId, -1L))
                                    .count(count)
                                    .position(NumberUtil.convertTolong(type, 0L))
                                    .asContentValues()
                    );
        }

        private int update(String adId, long count, String type) {
            return
                    mDb.update(AdStat.TABLE_NAME
                            , AdStat.FACTORY.marshal()
                                    .count(count)
                                    .asContentValues()
                            , AdStat.ADID + "=? and " + AdStat.POSITION + " =?", adId, type);
        }

        public Observable<AdStatTime> getAdStatTime() {
            return mDb.createQuery(AdStatTime.TABLE_NAME, AdStatTime.SELECT_ALL)
                    .map(query -> {
                        Cursor cursor = query.run();
                        if (cursor == null || !cursor.moveToNext()) {
                            return null;
                        }
                        AdStatTime adStatTime = null;
                        try {
                            adStatTime = AdStatTime.MAPPER.map(cursor);
                        } finally {
                            cursor.close();
                        }
                        return adStatTime;
                    })
                    .onErrorResumeNext(throwable -> {
                        try {
                            deleteAll();
                        } catch (Exception ignored) {
                        }
                        return null;
                    })
                    .first();
        }

        public void updateTimeIfNotExist(long time) {
            getAdStatTime()
                    .map(adStatTime -> {
                        if (adStatTime == null) {
                            insertTime(time);
                        }
                        return null;
                    })
                    .subscribe();
        }

        private long insertTime(long time) {
            return
                    mDb.insert(AdStatTime.TABLE_NAME
                            , AdStatTime.FACTORY.marshal()
                                    .time(time + "")
                                    .asContentValues()
                    );
        }

        public void clearTime() {
            mDb.delete(AdStatTime.TABLE_NAME, null);
        }

    }

}