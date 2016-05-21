package vkraevskiy.com.simpleweatherapp.net;

import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vkraevskiy.com.simpleweatherapp.BuildConfig;
import vkraevskiy.com.simpleweatherapp.core.bridge.DbFacade;
import vkraevskiy.com.simpleweatherapp.core.bridge.NetFacade;
import vkraevskiy.com.simpleweatherapp.core.callback.NetCallback;
import vkraevskiy.com.simpleweatherapp.db.model.City;
import vkraevskiy.com.simpleweatherapp.db.model.Forecast;

public class NetManager implements NetFacade {

    private WeatherAPI weatherAPI;
    private final DbFacade dbFacade;
    private final Executor executor;

    public NetManager(DbFacade dbFacade) {
        this.dbFacade = dbFacade;
        executor = Executors.newSingleThreadExecutor();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.API_URL)
                .client(client)
                .build();

        weatherAPI = retrofit.create(WeatherAPI.class);
    }

    @Override
    public void loadForecast(final double latitude, final double longitude, final NetCallback netCallback) {
        dbFacade.onStartLoading();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Call<ResponseWrapper> call = weatherAPI.loadForecast(latitude, longitude);
                try {
                    Response<ResponseWrapper> response = call.execute();

                    if (response.code() != 200) {
                        netCallback.onError(response.message());
                        return;
                    }

                    ResponseWrapper responseWrapper = response.body();
                    City city = responseWrapper.getCity();
                    List<Forecast> forecasts = responseWrapper.getForecast();

                    dbFacade.saveCity(city);
                    dbFacade.saveForecast(forecasts, city);
                    netCallback.onSuccess();
                } catch (IOException e) {
                    Log.e("RETROFIT", "loadForecast: ", e);
                }
            }
        });
    }
}
