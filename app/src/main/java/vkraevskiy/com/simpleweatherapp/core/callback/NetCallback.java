package vkraevskiy.com.simpleweatherapp.core.callback;

public interface NetCallback {

    void onSuccess();

    void onError(String message);
}
