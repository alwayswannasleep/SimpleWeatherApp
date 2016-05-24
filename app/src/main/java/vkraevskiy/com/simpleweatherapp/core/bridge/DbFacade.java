package vkraevskiy.com.simpleweatherapp.core.bridge;

import java.util.List;
import java.util.Observer;

import vkraevskiy.com.simpleweatherapp.db.model.City;
import vkraevskiy.com.simpleweatherapp.db.model.DailyForecast;
import vkraevskiy.com.simpleweatherapp.db.model.Forecast;

public interface DbFacade {
    void onStartLoading();

    void saveCity(City city);

    City getCityByID(long id);

    void saveForecast(List<Forecast> forecasts, City city);

    void addObserver(Observer observer);

    void deleteObserver(Observer observer);

    List<DailyForecast> getDailyForecasts();

    <T> String saveTransferData(T object);

    <T> T getTransferData(String key);
}
