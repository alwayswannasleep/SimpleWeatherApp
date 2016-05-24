package vkraevskiy.com.simpleweatherapp.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import vkraevskiy.com.simpleweatherapp.BuildConfig;
import vkraevskiy.com.simpleweatherapp.R;
import vkraevskiy.com.simpleweatherapp.core.bridge.ActivityBridge;
import vkraevskiy.com.simpleweatherapp.db.model.DailyForecast;
import vkraevskiy.com.simpleweatherapp.db.model.Forecast;
import vkraevskiy.com.simpleweatherapp.ui.activities.MainActivity;

public class DailyForecastFragment extends Fragment {

    private static final String TRANSFER_KEY_EXTRA = "transfer";

    private ActivityBridge activityBridge;

    private TextView timeStampTv;
    private ImageView forecastIcon;
    private TextView temperatureTv;
    private TextView humidityTv;
    private TextView pressureTv;
    private TextView windSpeedTv;
    private ImageView windDirection;
    private TextView forecastDescription;
    private Button button;

    public static Bundle buildArguments(String transferKey) {
        Bundle bundle = new Bundle();

        bundle.putString(TRANSFER_KEY_EXTRA, transferKey);
        return bundle;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activityBridge = ((MainActivity) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.forecast_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String transferKey = getArguments().getString(TRANSFER_KEY_EXTRA);
        DailyForecast dailyForecast =
                activityBridge.getWApplication().getDbFacade().getTransferData(transferKey);

        String title = dailyForecast.getCity().getName() + ", " + dailyForecast.getCity().getCountry();
        activityBridge.setTitle(title);

        initViews(view);

        Forecast currentForecast = dailyForecast.getForecasts().get(0);

        setForecastData(currentForecast);

        initButton(dailyForecast);
    }

    private void initButton(final DailyForecast dailyForecast) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(dailyForecast.getForecasts().get(0).getTimestamp());

                final NumberPicker numberPicker = new NumberPicker(v.getContext());
                final int minValue = calendar.get(Calendar.HOUR_OF_DAY);
                numberPicker.setMinValue((minValue - 3) / 3);
                numberPicker.setMaxValue(6);
                numberPicker.setValue(minValue);

                int size = (21 - minValue) / 3 + 1;
                if (size > 0) {
                    String[] values = new String[size];

                    int index = 0;

                    while (index < size) {
                        Forecast forecast = dailyForecast.getForecasts().get(index);
                        calendar.setTimeInMillis(forecast.getTimestamp());

                        int value = calendar.get(Calendar.HOUR_OF_DAY);
                        String string = (value < 10 ? "0" : "") + value + ":00";
                        values[index++] = String.valueOf(string);
                    }

                    numberPicker.setDisplayedValues(values);
                }

                numberPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

                new AlertDialog.Builder(v.getContext())
                        .setView(numberPicker)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setForecastData(dailyForecast.getForecasts().get(numberPicker.getValue()));

                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });
    }

    private void setForecastData(Forecast currentForecast) {
        SimpleDateFormat format = new SimpleDateFormat("EE (yyyy.MM.dd) kk:mm", Locale.ENGLISH);
        timeStampTv.setText(format.format(new Date(currentForecast.getTimestamp())));

        Picasso.with(getContext())
                .load(BuildConfig.IMAGE_BASE_URL + currentForecast.getWeather().get(0).getIcon() + ".png")
                .into(forecastIcon);

        float temp = currentForecast.getMain().getTemp();
        temperatureTv.setText(String.format(Locale.ENGLISH, "%s %d", temp >= 0 ? "+" : "-", Math.round(Math.abs(temp))));

        humidityTv.setText(String.format(Locale.ENGLISH, "%.2f %%", currentForecast.getMain().getHumidity()));
        pressureTv.setText(String.valueOf(currentForecast.getMain().getPressure()));

        windSpeedTv.setText(String.format(Locale.ENGLISH, "%.2f m/s, ", currentForecast.getWind().getSpeed()));
        windDirection.setRotation(currentForecast.getWind().getDegree());
        windDirection.invalidate();

        forecastDescription.setText(currentForecast.getWeather().get(0).getDescription());

    }

    private void initViews(View view) {
        timeStampTv = (TextView) view.findViewById(R.id.ff_timestamp);
        forecastIcon = (ImageView) view.findViewById(R.id.ff_forecast_icon);
        temperatureTv = (TextView) view.findViewById(R.id.ff_temperature);
        humidityTv = (TextView) view.findViewById(R.id.ff_humidity);
        pressureTv = (TextView) view.findViewById(R.id.ff_pressure);
        windSpeedTv = (TextView) view.findViewById(R.id.ff_wind_speed);
        windDirection = (ImageView) view.findViewById(R.id.ff_wind_direction);
        forecastDescription = (TextView) view.findViewById(R.id.ff_forecast_description);
        button = (Button) view.findViewById(R.id.ff_time_seekbar);
    }
}
