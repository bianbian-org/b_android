package com.techjumper.polyhome_b.adlib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.techjumper.polyhome_b.adlib.entity.sql.AdStat;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/7/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AdStatDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ad_stat";
    public static final int VERSION = 1;

    public static AdStatDbHelper create(Context context) {
        return new AdStatDbHelper(context);
    }

    private AdStatDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AdStat.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
