package vkraevskiy.com.simpleweatherapp.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import vkraevskiy.com.simpleweatherapp.R;
import vkraevskiy.com.simpleweatherapp.core.WApplication;
import vkraevskiy.com.simpleweatherapp.core.callback.SimpleNetCallback;
import vkraevskiy.com.simpleweatherapp.db.model.DailyForecast;
import vkraevskiy.com.simpleweatherapp.ui.adapter.DaysAdapter;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;

    private WApplication application;
    private RecyclerView recyclerView;
    private DaysAdapter adapter;

    private Observer observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        application = (WApplication) getApplication();

        initView();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Location location = application.getLocationManager().getLastLocation();
        loadData(location);
    }

    @SuppressWarnings("ConstantConditions")
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.am_days_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setFitsSystemWindows(true);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DaysAdapter(new ArrayList<DailyForecast>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        observer = new DbObserver();
        application.getDbFacade().addObserver(observer);
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
                        Toast.makeText(getApplicationContext(), "Data updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void onDataUpdated() {
        final List<DailyForecast> dailyForecasts = application.getDbFacade().getDailyForecasts();

        if (dailyForecasts == null) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.swapData(dailyForecasts);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        application.getDbFacade().deleteObserver(observer);
        observer = null;
    }

    private final class DbObserver implements Observer {
        @Override
        public void update(Observable observable, Object o) {
            onDataUpdated();
        }

    }
}
