package vkraevskiy.com.simpleweatherapp.db.model;

import java.util.List;

public class DailyForecast {

    private List<Forecast> forecasts;

    private City city;

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
