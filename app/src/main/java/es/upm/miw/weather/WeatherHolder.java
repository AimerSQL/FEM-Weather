package es.upm.miw.weather;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import es.upm.miw.weather.realtime.WeatherHistory;


public class WeatherHolder extends RecyclerView.ViewHolder {

    private final TextView tvIp;

    public WeatherHolder(@NonNull View itemView) {
        super(itemView);
        tvIp = itemView.findViewById(R.id.tvWeatherInfo);
    }

    public void bind(@NonNull WeatherHistory weather) {
        setWeather(weather.toString());
    }

    private void setWeather(@NonNull String weather){
        tvIp.setText(weather);
    }
}
