package com.techjumper.polyhome_b.adlib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.techjumper.polyhome_b.adlib.entity.sql.AdStat;
import com.techjumper.polyhome_b.adlib.entity.sql.AdStatTime;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/7/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class AdStatDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ad_stat";
    public static final int VERSION = 2;

    public static AdStatDbHelper create(Context context) {
        return new AdStatDbHelper(context);
    }

    private AdStatDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AdStat.CREATE_TABLE);
        db.execSQL(AdStatTime.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL(AdStatTime.CREATE_TABLE);
        }
    }
}
