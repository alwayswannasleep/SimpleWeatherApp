package vkraevskiy.com.simpleweatherapp.core;

import android.app.Application;

import vkraevskiy.com.simpleweatherapp.core.bridge.DbFacade;
import vkraevskiy.com.simpleweatherapp.core.bridge.NetFacade;
import vkraevskiy.com.simpleweatherapp.db.DbManager;
import vkraevskiy.com.simpleweatherapp.net.LocationManager;
import vkraevskiy.com.simpleweatherapp.net.NetManager;

public final class WApplication extends Application {

    private DbFacade dbFacade;
    private NetFacade netFacade;
    private LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        dbFacade = new DbManager(this);
        netFacade = new NetManager(dbFacade);
        locationManager = new LocationManager(this);
    }

    public DbFacade getDbFacade() {
        return dbFacade;
    }

    public NetFacade getNetFacade() {
        return netFacade;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }
}
