package com.techjumper.polyhome_b.adlib.db.utils;

import android.database.Cursor;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.basic.NumberUtil;
import com.techjumper.polyhome_b.adlib.db.AdStatDbHelper;
import com.techjumper.polyhome_b.adlib.entity.sql.AdStat;

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

        public Observable<AdStat> query(String adId) {
            return mDb.createQuery(AdStat.TABLE_NAME, AdStat.SELECT_BY_ADID, adId)
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
                    .first();
        }

        public Observable<Long> insertOrUpdate(String adId, int count) {
            return query(adId)
                    .map(adStat -> {
                        if (adStat == null) {
                            return insert(adId, count);
                        }
                        return (long) update(adId, count);
                    });
        }

        public Observable<Long> increase(String adId) {
            return query(adId)
                    .map(adStat -> {
                        if (adStat == null) {
                            return insert(adId, 1);
                        }
                        return (long) update(adId, (int) (adStat.count() + 1));
                    });
        }

        public int delete(String adId) {
            return mDb.delete(AdStat.TABLE_NAME, AdStat._ID + "=?", adId);
        }

        public boolean deleteAll() {
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

        private long insert(String adId, int count) {
            return
                    mDb.insert(AdStat.TABLE_NAME
                            , AdStat.FACTORY.marshal()
                                    ._id(NumberUtil.convertToint(adId, -1))
                                    .count(count)
                                    .asContentValues()
                    );
        }

        private int update(String adId, int count) {
            return
                    mDb.update(AdStat.TABLE_NAME
                            , AdStat.FACTORY.marshal()
                                    .count(count)
                                    .asContentValues()
                            , AdStat._ID + "=?", adId);
        }

    }

}