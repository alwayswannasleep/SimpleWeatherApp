package vkraevskiy.com.simpleweatherapp.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import vkraevskiy.com.simpleweatherapp.db.model.City;
import vkraevskiy.com.simpleweatherapp.db.model.Forecast;

final class ForecastsDao {

    private SQLiteDatabase database;

    ForecastsDao(SQLiteDatabase database) {
        this.database = database;
    }

    void save(List<Forecast> forecasts, City city) {
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();

            for (Forecast forecast : forecasts) {
                values.put(DbHelper.Forecast.COLUMN_NAME_CITY_ID, city.getId());
                values.put(DbHelper.Forecast.COLUMN_NAME_CLOUDINESS, forecast.getClouds().getCloudiness());
                values.put(DbHelper.Forecast.COLUMN_NAME_PRESSURE, forecast.getMain().getPressure());
                values.put(DbHelper.Forecast.COLUMN_NAME_TEMPERATURE, forecast.getMain().getTemp());
                values.put(DbHelper.Forecast.COLUMN_NAME_TIMESTAMP, forecast.getTimestamp());

                Forecast.Weather weather = forecast.getWeather().get(0);
                values.put(DbHelper.Forecast.COLUMN_NAME_WEATHER_DESCRIPTION, weather.getDescription());
                values.put(DbHelper.Forecast.COLUMN_NAME_WEATHER_MAIN, weather.getMain());
                values.put(DbHelper.Forecast.COLUMN_NAME_WEATHER_ICON, weather.getIcon());
                values.put(DbHelper.Forecast.COLUMN_NAME_WEATHER_ID, weather.getId());

                values.put(DbHelper.Forecast.COLUMN_NAME_WIND_DEGREE, forecast.getWind().getDegree());
                values.put(DbHelper.Forecast.COLUMN_NAME_WIND_SPEED, forecast.getWind().getSpeed());

                database.insertWithOnConflict(
                        DbHelper.Forecast.TABLE_NAME,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_REPLACE
                );

                values = new ContentValues();
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
}
