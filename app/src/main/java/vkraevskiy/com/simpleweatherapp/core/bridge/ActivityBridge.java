package vkraevskiy.com.simpleweatherapp.core.bridge;

import vkraevskiy.com.simpleweatherapp.core.WApplication;

public interface ActivityBridge {

    WApplication getWApplication();

    void setTitle(CharSequence charSequence);
}
