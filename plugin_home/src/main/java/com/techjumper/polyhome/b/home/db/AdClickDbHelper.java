package com.techjumper.polyhome.b.home.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.techjumper.polyhome.b.home.sql.AdClick;

/**
 * Created by kevin on 16/8/3.
 */

public class AdClickDbHelper extends SQLiteOpenHelper{

    public static final String DB_NAME = "ad_click";
    public static final int VERSION = 1;

    public static AdClickDbHelper create(Context context) {
        return new AdClickDbHelper(context);
    }

    private AdClickDbHelper(Context context){
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AdClick.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
