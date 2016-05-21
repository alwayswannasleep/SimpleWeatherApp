package vkraevskiy.com.simpleweatherapp.core.bridge;

import java.util.List;
import java.util.Observer;

import vkraevskiy.com.simpleweatherapp.db.model.City;
import vkraevskiy.com.simpleweatherapp.db.model.Forecast;

public interface DbFacade {
    void onStartLoading();

    void saveCity(City city);

    void saveForecast(List<Forecast> forecasts, City city);

    void addObserver(Observer observer);

    void deleteObserver(Observer observer);
}
