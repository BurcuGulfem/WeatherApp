package com.archsorceress.weatherapp.data;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Client to communicate with Weather API
 * Created by burcuarabaci on 25/02/17.
 */

public class WeatherAPIClient {
    private static WeatherAPIClient weatherAPIClient;
    private OkHttpClient httpClient;
    private static final String WEATHER_API_APP_ID = "3a094a340d5631977dfc775f762130b5";

    /**
     * @return Singleton WeatherAPIClient
     */
    public static WeatherAPIClient getInstance(){
        if(weatherAPIClient == null){
            weatherAPIClient = new WeatherAPIClient();
        }
        return  weatherAPIClient;
    }

    private WeatherAPIClient (){
        httpClient =  new OkHttpClient();
    }

    /**
     * returns okhttp3 call to get a cityweather object
     * uses "accurate" parameter for returning the city with the exact same name
     * uses "metric" parameter for returning temperature values in Celcius
     * @param cityName full or partial city name
     * @param callback okhttp3 callback
     * @return okhttp3 call
     * @throws IOException
     */
    public Call getAddCityCall(String cityName, Callback callback)throws IOException {
        String url = new HttpUrl.Builder()
                .scheme("http")
                .host("api.openweathermap.org")
                .addPathSegment("/data/2.5/weather")
                .addQueryParameter("q", cityName)
                .addQueryParameter("units","metric")
                .addQueryParameter("type","accurate")//We don't want random cities in our list
                .addQueryParameter("appid", WEATHER_API_APP_ID)
                .build().toString();

        return createWeatherAPICall(url,callback);
    }

    /**
     * Returns call to get cityWeather list for given city id's
     * @param cityIdsString takes a String consisting of city ids seperated by comma
     * @param callback okhttp3 callback
     * @return okhttp3 call
     * @throws IOException
     */
    public Call getCityWeathersByIdCall(String cityIdsString, Callback callback)throws IOException{
        String url = new HttpUrl.Builder()
                .scheme("http")
                .host("api.openweathermap.org")
                .addPathSegment("/data/2.5/group")
                .addQueryParameter("id", cityIdsString)
                .addQueryParameter("units","metric")
                .addQueryParameter("appid", WEATHER_API_APP_ID)
                .build().toString();
        return createWeatherAPICall(url,callback);
    }


    /**
     * Creates an async http GET call with given url and sends result to provided callback
     * @param url http url to create GET call
     * @param callback provide callback to return results
     * @return async GET call
     * @throws IOException
     */
    private Call createWeatherAPICall(String url, Callback callback)throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }


    /**
     * Get weather icon image url for given icon id
     * @param iconId id of weather icon. ie: D10
     * @return icon image download url
     */
    public String getWeatherIconById(String iconId){
        return new HttpUrl.Builder()
                .scheme("http")
                .host("api.openweathermap.org")
                .addPathSegment("/img/w/"+iconId+".png")
                .build().toString();
    }
}
