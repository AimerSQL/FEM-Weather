package es.upm.miw.weather;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lon")
    @Expose
    private double lon;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("current")
    @Expose
    private CurrentWeather current;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public CurrentWeather getCurrent() {
        return current;
    }

    public void setCurrent(CurrentWeather current) {
        this.current = current;
    }

    @NonNull
    @Override
    public String toString() {
        return "Weather:" +
                "latitud=" + lat +
                ", longitud=" + lon +
                ", zona horaria='" + timezone + '\'' +
                ", tiempo=" + current +
                '.' +
                '\n';
    }

    public static class CurrentWeather {
        @SerializedName("dt")
        @Expose
        private long dt;
        @SerializedName("sunrise")
        @Expose
        private long sunrise;
        @SerializedName("sunset")
        @Expose
        private long sunset;
        @SerializedName("temp")
        @Expose
        private double temp;
        @SerializedName("feels_like")
        @Expose
        private double feelsLike;
        @SerializedName("pressure")
        @Expose
        private int pressure;
        @SerializedName("humidity")
        @Expose
        private int humidity;
        @SerializedName("dew_point")
        @Expose
        private double dewPoint;
        @SerializedName("uvi")
        @Expose
        private double uvi;
        @SerializedName("clouds")
        @Expose
        private int clouds;
        @SerializedName("visibility")
        @Expose
        private int visibility;
        @SerializedName("wind_speed")
        @Expose
        private double windSpeed;
        @SerializedName("wind_deg")
        @Expose
        private int windDeg;
        @SerializedName("wind_gust")
        @Expose
        private double windGust;
        @SerializedName("weather")
        @Expose
        private List<WeatherInfo> weather;

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }


        @Override
        public String toString() {
            return "dt=" + dt +
                    ", sunrise=" + sunrise +
                    ", sunset=" + sunset +
                    ", temp=" + temp +
                    ", feelsLike=" + feelsLike +
                    ", pressure=" + pressure +
                    ", humidity=" + humidity +
                    ", dewPoint=" + dewPoint +
                    ", uvi=" + uvi +
                    ", clouds=" + clouds +
                    ", visibility=" + visibility +
                    ", windSpeed=" + windSpeed +
                    ", windDeg=" + windDeg +
                    ", windGust=" + windGust +
                    ", weather=" + weather +
                    '.' +
                    '\n';
        }
    }

    public static class WeatherInfo {
        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("main")
        @Expose
        private String main;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("icon")
        @Expose
        private String icon;


        @NonNull
        @Override
        public String toString() {
            return "detalle:" +
                    ", main='" + main + '\'' +
                    ", description='" + description + '\'' +
                    '.'+
                    '\n';
        }
    }
}
