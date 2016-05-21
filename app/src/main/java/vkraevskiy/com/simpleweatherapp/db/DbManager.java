package vkraevskiy.com.simpleweatherapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import vkraevskiy.com.simpleweatherapp.core.bridge.DbFacade;

public class DbManager implements DbFacade {
    private SQLiteDatabase writableDatabase;

    public DbManager(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        writableDatabase = dbHelper.getWritableDatabase();
    }


}
