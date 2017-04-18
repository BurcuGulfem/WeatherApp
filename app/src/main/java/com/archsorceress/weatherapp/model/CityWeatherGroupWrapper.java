package com.archsorceress.weatherapp.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Wrapper class is needed because the API is sending the list as a json object
 * Created by burcuarabaci on 26/02/17.
 */
@JsonObject
public class CityWeatherGroupWrapper {
    @JsonField (name = "list")
    private List<CityWeather> cityWeatherList;

    public List<CityWeather> getCityWeatherList() {
        return cityWeatherList;
    }


    public void setCityWeatherList(List<CityWeather> cityWeatherList) {
        this.cityWeatherList = cityWeatherList;
    }
}
