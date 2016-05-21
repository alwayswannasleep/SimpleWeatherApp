package vkraevskiy.com.simpleweatherapp.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import vkraevskiy.com.simpleweatherapp.db.model.City;

final class CitiesDao {

    private static final String SAVE_SQL =
            "INSERT INTO " + DbHelper.City.TABLE_NAME + " VALUES (?, ?, ?, ?)";

    private SQLiteDatabase database;

    CitiesDao(SQLiteDatabase database) {
        this.database = database;
    }

    void save(City city) {
        SQLiteStatement statement = database.compileStatement(SAVE_SQL);

        int index = 1;
        statement.bindLong(index++, city.getId());
        statement.bindString(index++, city.getName());
        statement.bindString(index++, city.getCountry());
        statement.bindLong(index, city.getSyncTimestamp());

        statement.execute();
    }
}
