package es.upm.miw.weather.realtime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.List;

import es.upm.miw.weather.Weather;

@IgnoreExtraProperties
public class WeatherHistory {
    private Double mLat;
    private Double mLon;
    private String mTimezone;
    private String mUid;
    private Weather.CurrentWeather mWeather;

    public WeatherHistory() {

    }

    public WeatherHistory(@Nullable Double lat, @Nullable Double lon, @NonNull String timezone, @NonNull String uid, @Nullable Weather.CurrentWeather weather) {
        this.mLat = lat;
        this.mLon = lon;
        this.mTimezone = timezone;
        this.mUid = uid;
        this.mWeather = weather;
    }

    @Nullable
    public Double getLat() {
        return mLat;
    }

    public void setLat(@Nullable Double lat) {
        mLat = lat;
    }

    @Nullable
    public Double getLon() {
        return mLon;
    }

    public void setLon(@Nullable Double lon) {
        mLon = lon;
    }

    @NonNull
    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(@NonNull String timezone) {
        mTimezone = timezone;
    }

    @NonNull
    public String getUid() {
        return mUid;
    }

    public void setUid(@NonNull String uid) {
        mUid = uid;
    }

    @Nullable
    public Weather.CurrentWeather getWeather() {
        return mWeather;
    }

    public void setWeather(@Nullable Weather.CurrentWeather weather) {
        mWeather = weather;
    }

    @Override
    public String toString() {
        return "WeatherHistory:" +
                "Longitud=" + mLon +
                ", Latitud=" + mLat +
                ", zona horaria='" + mTimezone + '\'' +
                ", tiempos=" + mWeather +
                '.';
    }
}
