package vkraevskiy.com.simpleweatherapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vkraevskiy.com.simpleweatherapp.BuildConfig;
import vkraevskiy.com.simpleweatherapp.R;
import vkraevskiy.com.simpleweatherapp.db.model.DailyForecast;
import vkraevskiy.com.simpleweatherapp.db.model.Forecast;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.VH> {

    private List<DailyForecast> forecasts;
    private int selectedPosition;

    private OnItemSelectedListener listener;

    public DaysAdapter(List<DailyForecast> forecasts) {
        this.forecasts = forecasts;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_list_item, parent, false);

        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Forecast forecast = forecasts.get(position).getForecasts().get(0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EE", Locale.getDefault());
        long timestamp = forecast.getTimestamp();
        holder.date.setText(dateFormat.format(new Date(timestamp)));

        Picasso.with(holder.itemView.getContext())
                .load(BuildConfig.IMAGE_BASE_URL + forecast.getWeather().get(0).getIcon() + ".png")
                .into(holder.icon);

        holder.background.setSelected(position == selectedPosition);

        holder.temp.setText(String.format("%s C", forecast.getMain().getTemp()));
        holder.humidity.setText(String.format("%s%%", forecast.getMain().getHumidity()));
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

    public void swapData(List<DailyForecast> dailyForecasts) {
        forecasts = dailyForecasts;
        notifyDataSetChanged();
    }

    public void selectItem(int adapterPosition) {
        boolean isSelected = selectedPosition == adapterPosition;

        if (isSelected) {
            return;
        }

        int previousSelectedPosition = selectedPosition;
        selectedPosition = adapterPosition;

        notifyItemChanged(previousSelectedPosition);
        notifyItemChanged(selectedPosition);

        if (listener != null) {
            listener.onItemSelected(forecasts.get(selectedPosition));
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public void removeOnItemSelectedListener() {
        listener = null;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(DailyForecast forecast);
    }

    public final class VH extends RecyclerView.ViewHolder {
        public TextView date;
        public ImageView icon;
        public TextView temp;
        public TextView humidity;

        public LinearLayout background;

        public VH(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.dli_date);
            icon = (ImageView) itemView.findViewById(R.id.dli_icon);
            temp = (TextView) itemView.findViewById(R.id.dli_temp);
            humidity = (TextView) itemView.findViewById(R.id.dli_humidity);
            background = (LinearLayout) itemView.findViewById(R.id.dli_background);

            background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectItem(getAdapterPosition());
                }
            });
        }
    }
}
