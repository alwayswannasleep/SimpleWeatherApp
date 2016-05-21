package vkraevskiy.com.simpleweatherapp.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import vkraevskiy.com.simpleweatherapp.R;
import vkraevskiy.com.simpleweatherapp.core.WApplication;
import vkraevskiy.com.simpleweatherapp.core.callback.SimpleNetCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ((WApplication) getApplication()).getNetFacade().loadForecast(35, 35, new SimpleNetCallback() {
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
