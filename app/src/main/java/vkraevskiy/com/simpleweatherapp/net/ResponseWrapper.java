package vkraevskiy.com.simpleweatherapp.net;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vkraevskiy.com.simpleweatherapp.db.model.City;
import vkraevskiy.com.simpleweatherapp.db.model.Forecast;

public final class ResponseWrapper {

    @SerializedName("city")
    private City city;

    @SerializedName("list")
    private List<Forecast> forecast;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }

    public void setForecast(List<Forecast> forecast) {
        this.forecast = forecast;
    }
}
