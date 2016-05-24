package vkraevskiy.com.simpleweatherapp.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import vkraevskiy.com.simpleweatherapp.core.bridge.DbFacade;
import vkraevskiy.com.simpleweatherapp.db.model.City;
import vkraevskiy.com.simpleweatherapp.db.model.DailyForecast;
import vkraevskiy.com.simpleweatherapp.db.model.Forecast;

final class ForecastsDao {

    private static final String SELECT_FORECASTS_BETWEEN_DATES =
            "SELECT * FROM " + DbHelper.Forecast.TABLE_NAME + " WHERE " + DbHelper.Forecast.COLUMN_NAME_TIMESTAMP + " BETWEEN ? AND ?;";

    private static final int DAYS_COUNT = 5;

    private final SQLiteDatabase database;
    private final DbFacade dbManager;

    ForecastsDao(SQLiteDatabase database, DbManager dbManager) {
        this.database = database;
        this.dbManager = dbManager;
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
                values.put(DbHelper.Forecast.COLUMN_NAME_HUMIDITY, forecast.getMain().getHumidity());

                long value = forecast.getTimestamp() * 1000;
                forecast.setTimestamp(value);

                values.put(DbHelper.Forecast.COLUMN_NAME_TIMESTAMP, value);

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

    List<DailyForecast> getDailyForecasts() {
        List<DailyForecast> dailyForecasts = new ArrayList<>(DAYS_COUNT);

        Calendar instance = Calendar.getInstance();
        for (int i = 0; i < DAYS_COUNT; i++) {
            long startTime = instance.getTimeInMillis();

            instance.set(Calendar.HOUR_OF_DAY, 23);
            instance.set(Calendar.MINUTE, 59);
            instance.set(Calendar.SECOND, 59);

            long endTime = instance.getTimeInMillis();

            List<Forecast> forecasts = getForecastsByDate(startTime, endTime);

            if (forecasts != null) {
                DailyForecast dailyForecast = new DailyForecast();
                dailyForecast.setForecasts(forecasts);
                dailyForecast.setCity(forecasts.get(0).getCity());

                dailyForecasts.add(dailyForecast);
            }

            instance.add(Calendar.SECOND, 1);
        }

        return dailyForecasts;
    }

    private List<Forecast> getForecastsByDate(long startTime, long endTime) {
        Cursor cursor = database.rawQuery(SELECT_FORECASTS_BETWEEN_DATES,
                new String[]{String.valueOf(startTime), String.valueOf(endTime)});

        List<Forecast> forecasts = new ArrayList<>();

        if (!cursor.moveToFirst()) {
            return null;
        }

        int cityId = cursor.getColumnIndex(DbHelper.Forecast.COLUMN_NAME_CITY_ID);
        int cloudinessIndex = cursor.getColumnIndex(DbHelper.Forecast.COLUMN_NAME_CLOUDINESS);
        int humidityIndex = cursor.getColumnIndex(DbHelper.Forecast.COLUMN_NAME_HUMIDITY);
        int pressureIndex = cursor.getColumnIndex(DbHelper.Forecast.COLUMN_NAME_PRESSURE);
        int temperatureIndex = cursor.getColumnIndex(DbHelper.Forecast.COLUMN_NAME_TEMPERATURE);
        int timestampIndex = cursor.getColumnIndex(DbHelper.Forecast.COLUMN_NAME_TIMESTAMP);
        int weatherIndex = cursor.getColumnIndex(DbHelper.Forecast.COLUMN_NAME_WEATHER_ID);
        int weatherIconIndex = cursor.getColumnIndex(DbHelper.Forecast.COLUMN_NAME_WEATHER_ICON);
        int weatherMainIndex = cursor.getColumnIndex(DbHelper.Forecast.COLUMN_NAME_WEATHER_MAIN);
        int weatherDescriptionIndex = cursor.getColumnIndex(DbHelper.Forecast.COLUMN_NAME_WEATHER_DESCRIPTION);
        int windSpeedIndex = cursor.getColumnIndex(DbHelper.Forecast.COLUMN_NAME_WIND_SPEED);
        int windDegreeIndex = cursor.getColumnIndex(DbHelper.Forecast.COLUMN_NAME_WIND_DEGREE);

        do {
            Forecast forecast = new Forecast();
            Forecast.Main main = new Forecast.Main();
            Forecast.Wind wind = new Forecast.Wind();
            Forecast.Weather weather = new Forecast.Weather();
            Forecast.Clouds clouds = new Forecast.Clouds();

            forecast.setTimestamp(cursor.getLong(timestampIndex));

            main.setHumidity(cursor.getFloat(humidityIndex));
            main.setPressure(cursor.getFloat(pressureIndex));
            main.setTemp(cursor.getFloat(temperatureIndex));

            forecast.setMain(main);

            wind.setDegree(cursor.getFloat(windDegreeIndex));
            wind.setSpeed(cursor.getFloat(windSpeedIndex));

            forecast.setWind(wind);

            weather.setId(cursor.getLong(weatherIndex));
            weather.setIcon(cursor.getString(weatherIconIndex));
            weather.setMain(cursor.getString(weatherMainIndex));
            weather.setDescription(cursor.getString(weatherDescriptionIndex));

            forecast.setWeather(new ArrayList<>(Arrays.asList(new Forecast.Weather[]{weather})));

            clouds.setCloudiness(cloudinessIndex);

            forecast.setClouds(clouds);

            City city = dbManager.getCityByID(cursor.getLong(cityId));
            forecast.setCity(city);

            forecasts.add(forecast);
        } while (cursor.moveToNext());

        cursor.close();
        return forecasts;
    }
}
