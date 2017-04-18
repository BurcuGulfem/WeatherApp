package com.archsorceress.weatherapp;

import com.archsorceress.weatherapp.model.CityWeather;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Testing {@link CityWeatherAdapter}
 */
public class CityWeatherAdapterUnitTest {

    @Test
    public void emptyConstructorTest() throws Exception {
        CityWeatherAdapter adapter = new CityWeatherAdapter();
        assertNotNull(adapter.getCityWeatherList());
    }

    @Test
    public void parameterizedConstructorTest() throws Exception {
        CityWeatherAdapter adapter = getFilledAdapter();
        assertEquals(1,adapter.getItemCount());
    }

    @Test
    public void addCityTest() throws Exception {
        CityWeatherAdapter adapter = getFilledAdapter();
        adapter.addCityWeather(getDummyCityWeatherLondon());
        assertEquals(2,adapter.getItemCount());
    }

    @Test
    public void addExistingCityTest() throws Exception {
        CityWeatherAdapter adapter = getFilledAdapter();
        adapter.addCityWeather(getDummyCityWeatherAmsterdam());
        assertEquals(1,adapter.getItemCount());
    }

    @Test
    public void getCityWeatherIdStringTest() throws Exception {
        CityWeatherAdapter adapter = getFilledAdapter();
        adapter.addCityWeather(getDummyCityWeatherLondon());
        String idString = adapter.getCityWeatherIdString();
        String regex = "[0-9, /,]+";
        assertTrue(idString.matches(regex));
        assertTrue(idString.contains(","));//2 ids must be seperated by a comma.
    }


    private CityWeatherAdapter getFilledAdapter(){
        ArrayList<CityWeather> weathers = new ArrayList<>();
        weathers.add(getDummyCityWeatherAmsterdam());
        return new CityWeatherAdapter(weathers);
    }

    private CityWeather getDummyCityWeatherAmsterdam(){
        return new CityWeather(10,"Amsterdam");
    }
    private CityWeather getDummyCityWeatherLondon(){
        return new CityWeather(1,"London");
    }
}