package vkraevskiy.com.simpleweatherapp.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import vkraevskiy.com.simpleweatherapp.R;
import vkraevskiy.com.simpleweatherapp.core.WApplication;
import vkraevskiy.com.simpleweatherapp.core.callback.SimpleNetCallback;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;

    private WApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        application = (WApplication) getApplication();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

            return;
        }

        Location location = application.getLocationManager().getLastLocation();
        loadData(location);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != REQUEST_CODE) {
            return;
        }

        int result = grantResults[0];

        if (result == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "Please enable location permission for us.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Location location = application.getLocationManager().getLastLocation();
        loadData(location);
    }

    private void loadData(Location location) {
        application.getNetFacade().loadForecast(location.getLatitude(), location.getLongitude(), new SimpleNetCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
