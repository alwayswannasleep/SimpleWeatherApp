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
import vkraevskiy.com.simpleweatherapp.core.bridge.ActivityBridge;
import vkraevskiy.com.simpleweatherapp.core.callback.SimpleNetCallback;
import vkraevskiy.com.simpleweatherapp.db.model.DailyForecast;
import vkraevskiy.com.simpleweatherapp.ui.adapter.DaysAdapter;
import vkraevskiy.com.simpleweatherapp.ui.fragments.DailyForecastFragment;

public class MainActivity extends AppCompatActivity implements ActivityBridge {

    private static final int REQUEST_CODE = 100;

    private DaysAdapter adapter;

    private Observer observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Location location = getWApplication().getLocationManager().getLastLocation();
        loadData(location);
    }

    @SuppressWarnings("ConstantConditions")
    private void initView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.am_days_list);
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
        getWApplication().getDbFacade().addObserver(observer);

        adapter.setOnItemSelectedListener(new DaysAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(DailyForecast forecast) {
                launchDailyForecastFragment(forecast);
            }
        });
    }

    @Override
    public WApplication getWApplication() {
        return (WApplication) getApplication();
    }

    private void launchDailyForecastFragment(DailyForecast forecast) {
        DailyForecastFragment fragment = new DailyForecastFragment();
        Bundle bundle = DailyForecastFragment.buildArguments(getWApplication().getDbFacade().saveTransferData(forecast));

        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.am_fragment_container, fragment, null)
                .commitAllowingStateLoss();
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

        Location location = getWApplication().getLocationManager().getLastLocation();
        loadData(location);
    }

    private void loadData(Location location) {
        getWApplication().getNetFacade().loadForecast(location.getLatitude(), location.getLongitude(), new SimpleNetCallback() {
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
        final List<DailyForecast> dailyForecasts = getWApplication().getDbFacade().getDailyForecasts();

        if (dailyForecasts == null) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.swapData(dailyForecasts);

                launchDailyForecastFragment(dailyForecasts.get(0));
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        getWApplication().getDbFacade().deleteObserver(observer);
        observer = null;

        adapter.removeOnItemSelectedListener();
    }

    private final class DbObserver implements Observer {
        @Override
        public void update(Observable observable, Object o) {
            onDataUpdated();
        }

    }
}
