package vkraevskiy.com.simpleweatherapp.net;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import vkraevskiy.com.simpleweatherapp.BuildConfig;

public interface WeatherAPI {
    @GET("data/2.5/forecast?units=metric&appid=" + BuildConfig.API_KEY)
    Call<ResponseWrapper> loadForecast(@Query("lat") double latitude, @Query("lon") double longitude);
}
