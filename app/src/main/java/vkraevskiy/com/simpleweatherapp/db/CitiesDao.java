package vkraevskiy.com.simpleweatherapp.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import vkraevskiy.com.simpleweatherapp.db.model.City;

final class CitiesDao {
    private SQLiteDatabase database;

    CitiesDao(SQLiteDatabase database) {
        this.database = database;
    }

    void save(City city) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.City._ID, city.getId());
        values.put(DbHelper.City.COLUMN_NAME_CITY_NAME, city.getName());
        values.put(DbHelper.City.COLUMN_NAME_COUNTRY_NAME, city.getCountry());
        values.put(DbHelper.City.COLUMN_NAME_SYNC_TIMESTAMP, city.getSyncTimestamp());

        database.insertWithOnConflict(
                DbHelper.City.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }
}
