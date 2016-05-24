package vkraevskiy.com.simpleweatherapp.net;

import android.content.Context;
import android.location.Location;

public final class LocationManager {

    private android.location.LocationManager service;

    public LocationManager(Context context) {
        service = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public Location getLastLocation() {
        //noinspection MissingPermission
        return service.getLastKnownLocation(android.location.LocationManager.PASSIVE_PROVIDER);
    }
}
