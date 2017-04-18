package com.archsorceress.weatherapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Helper class to keep all shared preferences keys and functions in one place
 *
 * Created by burcuarabaci on 26/02/17.
 */

public class SharedPreferencesHelper {
    public static final String WEATHER_LIST_KEY = "WeatherList";
    private static final String CITY_IDS_KEY = "CityIds";

    SharedPreferences sharedpreferences;

    public SharedPreferencesHelper(Context context) {
        sharedpreferences = context.getSharedPreferences(WEATHER_LIST_KEY, Context.MODE_PRIVATE);
    }

    public String getSavedCityIds(){
        if(sharedpreferences.contains(CITY_IDS_KEY)){
            String cityIds = sharedpreferences.getString(CITY_IDS_KEY,"");
            if(!cityIds.isEmpty()){
                return cityIds;
            }
        }
        return null;
    }

    public void writeSavedCityIds(String cityIds){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(CITY_IDS_KEY,cityIds);
        editor.apply();
    }

    public void clearPreferences(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
    }
}
