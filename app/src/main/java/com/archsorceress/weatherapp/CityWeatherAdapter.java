package com.archsorceress.weatherapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.archsorceress.weatherapp.model.CityWeather;
import com.archsorceress.weatherapp.data.WeatherAPIClient;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * RecyclerView adapter for CityWeather list
 *
 * Created by burcuarabaci on 24/02/17.
 */

 class CityWeatherAdapter extends RecyclerView.Adapter<CityWeatherAdapter.CityWeatherViewHolder>  {

    //TODO consider using a set here to prevent duplicates
    private ArrayList<CityWeather> cityWeatherList;
    private WeatherAPIClient weatherAPIClient; //Singleton

    /**
     * Parameterized constructor
     * Used if there is already a cityWeatherList saved
     * @param cityWeatherList Arraylist consisting of cityWeather objects
     */
     CityWeatherAdapter(ArrayList<CityWeather> cityWeatherList) {
        this.cityWeatherList = cityWeatherList;
         weatherAPIClient = WeatherAPIClient.getInstance();
    }

    /**
     * Overloading parameterized constructor
     * Creates an empty arraylist of cityWeather objects
     */
    CityWeatherAdapter() {
        this(new ArrayList<CityWeather>());
    }

    public void setData(ArrayList<CityWeather> cityWeatherList){
        this.cityWeatherList = cityWeatherList;
    }

     boolean addCityWeather(CityWeather cityWeather){
        if(cityWeatherList.contains(cityWeather)){
            return false;
        }
        else {
            cityWeatherList.add(cityWeather);
            return true;
        }
    }

    @Override
    public int getItemCount() {
        return cityWeatherList.size();
    }

     ArrayList<CityWeather> getCityWeatherList() {
        return cityWeatherList;
    }

    String getCityWeatherIdString(){
        String cityIds="";
        if(cityWeatherList.size() == 0) return "";
        for (CityWeather cityweather:
             cityWeatherList) {
            cityIds+=cityweather.getCityId()+",";
        }
        cityIds = cityIds.substring(0,cityIds.length()-1);
        return cityIds;
    }

    @Override
    public CityWeatherAdapter.CityWeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View cityWeatherView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_city_weather, parent, false);
                return new CityWeatherViewHolder(cityWeatherView);
    }

    @Override
    public void onBindViewHolder(CityWeatherAdapter.CityWeatherViewHolder holder, int position) {
        holder.cityName.setText(cityWeatherList.get(position).getCityName());
        holder.temperature.setText(String.valueOf(cityWeatherList.get(position).getTemperature().getTemp()));
        String iconId = cityWeatherList.get(position).getWeatherIconId();

        if (iconId!= null) {
            //using glide library to download, lazy load and cache the weather icons
            Glide.with(holder.icon.getContext())
                    .load(weatherAPIClient.getWeatherIconById(iconId))
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.icon);
        } else {
            // clear previous images from glide
            Glide.clear(holder.icon);
            holder.icon.setImageDrawable(null);
        }
    }

    class CityWeatherViewHolder extends RecyclerView.ViewHolder {

        TextView cityName;
        TextView temperature;
        ImageView icon;

        CityWeatherViewHolder(View itemView) {
            super(itemView);
            cityName = (TextView) itemView.findViewById(R.id.textView_cityName);
            temperature = (TextView) itemView.findViewById(R.id.textView_temperature);
            icon = (ImageView) itemView.findViewById(R.id.imageView_weatherIcon);
        }
    }

}



