package com.archsorceress.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by burcuarabaci on 24/02/17.
 */
@JsonObject
public class CityWeather implements Parcelable {
    @JsonField  (name = "id")
    private int cityId;
    @JsonField  (name = "name")
    private String cityName;
    @JsonField
    private List<Weather> weather;

    @JsonField (name = "main")
    private Temperature temperature;

    public CityWeather(int cityId, String cityName) {
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public String getWeatherIconId(){
        if(weather!=null){
            return weather.get(0).getIconId();
        }
        else return null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CityWeather){
            CityWeather comparedCity = (CityWeather) obj;
            return (this.cityId == comparedCity.cityId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityId,cityName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cityId);
        dest.writeString(this.cityName);
        dest.writeList(this.weather);
        dest.writeParcelable(this.temperature, flags);
    }

    public CityWeather() {
    }

    protected CityWeather(Parcel in) {
        this.cityId = in.readInt();
        this.cityName = in.readString();
        this.weather = new ArrayList<Weather>();
        in.readList(this.weather, Weather.class.getClassLoader());
        this.temperature = in.readParcelable(Temperature.class.getClassLoader());
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public static final Parcelable.Creator<CityWeather> CREATOR = new Parcelable.Creator<CityWeather>() {
        @Override
        public CityWeather createFromParcel(Parcel source) {
            return new CityWeather(source);
        }

        @Override
        public CityWeather[] newArray(int size) {
            return new CityWeather[size];
        }
    };

    //Setters are being used by logansquare

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }
}
