package vkraevskiy.com.simpleweatherapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.Observable;

import vkraevskiy.com.simpleweatherapp.core.bridge.DbFacade;
import vkraevskiy.com.simpleweatherapp.db.model.City;
import vkraevskiy.com.simpleweatherapp.db.model.DailyForecast;
import vkraevskiy.com.simpleweatherapp.db.model.Forecast;

public final class DbManager extends Observable implements DbFacade {

    private CitiesDao citiesDao;
    private ForecastsDao forecastsDao;

    public DbManager(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.setForeignKeyConstraintsEnabled(true);

        citiesDao = new CitiesDao(writableDatabase);
        forecastsDao = new ForecastsDao(writableDatabase);
    }


    @Override
    public void onStartLoading() {
        setChanged();
        notifyObservers();
    }

    @Override
    public void saveCity(City city) {
        citiesDao.save(city);

        setChanged();
        notifyObservers();
    }

    @Override
    public void saveForecast(List<Forecast> forecasts, City city) {
        forecastsDao.save(forecasts, city);

        setChanged();
        notifyObservers();
    }

    @Override
    public List<DailyForecast> getDailyForecasts() {
        return forecastsDao.getDailyForecasts();
    }
}
