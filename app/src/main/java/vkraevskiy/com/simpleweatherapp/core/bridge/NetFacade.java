package vkraevskiy.com.simpleweatherapp.core.bridge;

import vkraevskiy.com.simpleweatherapp.core.callback.NetCallback;

public interface NetFacade {
    void loadForecast(double latitude, double longitude, NetCallback netCallback);
}
