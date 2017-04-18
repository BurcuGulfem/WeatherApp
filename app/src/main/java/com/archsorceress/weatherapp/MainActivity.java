package com.archsorceress.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.archsorceress.weatherapp.model.CityWeather;
import com.archsorceress.weatherapp.model.CityWeatherGroupWrapper;
import com.archsorceress.weatherapp.utils.SharedPreferencesHelper;
import com.archsorceress.weatherapp.data.WeatherAPIClient;
import com.bluelinelabs.logansquare.LoganSquare;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * App keeps a list of user selected cities and show their weather data
 * from openweathermap.org
 *
 * TODO : Add weather for current location
 * TODO : Add internet connection change listener
 */
public class MainActivity extends AppCompatActivity {

    protected static final String WEATHER_LIST = "WeatherList";
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 123;
    private static final String TAG = "MainActivity";

    RecyclerView recyclerViewCityWeather;
    CityWeatherAdapter cityWeatherAdapter;
    EditText cityNameEdit;
    LinearLayout loadingLayout;
    private boolean writePermissionGranted;
    SharedPreferencesHelper sharedPreferencesHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //To stop edittext opening the keyboard immediately.
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        cityNameEdit = (EditText) findViewById(R.id.editText_cityName);
        loadingLayout = (LinearLayout) findViewById(R.id.layout_loading);

        sharedPreferencesHelper = new SharedPreferencesHelper(getApplicationContext());
        restoreCityWeatherList(savedInstanceState);
        checkPermissions();

        recyclerViewCityWeather = (RecyclerView) findViewById(R.id.recyclerView_cityWeather);
        recyclerViewCityWeather.setAdapter(cityWeatherAdapter);
        recyclerViewCityWeather.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton refreshButton = (FloatingActionButton) findViewById(R.id.fab);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherDataForSavedCities();
            }
        });
    }

    /**
     * Function called by content_main.xml
     * Runs the Asynctask with the city name entered in cityNameEdit.
     * Clears EditText so the user can enter multiple cities easily
     *
     * @param view button
     */
    public void addButtonClick(View view) {
        String cityName = cityNameEdit.getText().toString();
        if (!cityName.trim().isEmpty()) {
           addCityByName(cityName);
            cityNameEdit.getText().clear();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(cityNameEdit.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    /*
     * SAVE & RESTORE STATE
     */

    private void restoreCityWeatherList(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(WEATHER_LIST)) {
            ArrayList<CityWeather> cityWeatherList = savedInstanceState.getParcelableArrayList(WEATHER_LIST);
            cityWeatherAdapter = new CityWeatherAdapter(cityWeatherList);
        } else {
            cityWeatherAdapter = new CityWeatherAdapter();
            getWeatherDataForSavedCities();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<CityWeather> cityWeatherList = savedInstanceState.getParcelableArrayList("weatherList");
        cityWeatherAdapter = new CityWeatherAdapter(cityWeatherList);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (writePermissionGranted) {
            outState.putParcelableArrayList(WEATHER_LIST, cityWeatherAdapter.getCityWeatherList());
        }
        super.onSaveInstanceState(outState);
    }

    /*
     * PERMISSIONS
     */

    private void checkPermissions() {

        //Let's request write permissions at the beginning, because we'll use it when app goes to background
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                writePermissionGranted = true;
            } else {
                writePermissionGranted = false;
                requestPermission();
            }
        } else {
            writePermissionGranted = true;
        }
    }

    protected void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, getString(R.string.alert_permission_write_external), Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],@NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE:
                writePermissionGranted = (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
                break;
        }
    }

    /*
     * DATA OPERATIONS
     */

    //TODO: Handle this data operations inside API Client or a presenter

    public void getWeatherDataForSavedCities() {
        loadingLayout.setVisibility(View.VISIBLE);
        String savedCityIds = sharedPreferencesHelper.getSavedCityIds();
        try {
            WeatherAPIClient.getInstance().getCityWeathersByIdCall(savedCityIds, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG,"Failure in getWeatherDataForSavedCities call "+ e.getLocalizedMessage());
                    loadingLayout.setVisibility(View.GONE);
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        String responseString = response.body().string();
                        CityWeatherGroupWrapper wrapper = LoganSquare.parse(responseString, CityWeatherGroupWrapper.class);
                        if(wrapper!=null && !wrapper.getCityWeatherList().isEmpty()) {
                            handleWeatherDataForSavedCitiesResponse(wrapper);
                        }
                        else {
                            handleErrorResponse(R.string.alert_error_city_list_empty);
                        }
                    }
                    else {
                        Log.e(TAG,"Response is not successful "+ response.toString());
                        handleErrorResponse(R.string.alert_error_getting_list);
                    }
                    loadingLayout.setVisibility(View.GONE);
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "IOException in weather for saved cities call "+e.getLocalizedMessage());
            handleErrorResponse(R.string.alert_error_getting_list);
        }
    }

    /**
     * Handles weather data for saved cities call response.
     * Runs on UI thread to update UI without crashing
     * @param wrapper result of the call. may be empty.
     */
    public void handleWeatherDataForSavedCitiesResponse(final CityWeatherGroupWrapper wrapper){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setCityWeatherList( new ArrayList<>(wrapper.getCityWeatherList()));
                loadingLayout.setVisibility(View.GONE);
            }
        });
    }

    /**
     *
     * @param cityWeather
     */
    public void handleAddCityWeatherResponse(final CityWeather cityWeather){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addCityWeatherToList(cityWeather);
                }
            });
    }

    public void handleErrorResponse(final int errorStringResource){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,getString(errorStringResource), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addCityByName(final String cityName){
        try {
            WeatherAPIClient.getInstance().getAddCityCall(cityName, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseString = response.body().string();
                    final CityWeather cityWeather = LoganSquare.parse(responseString, CityWeather.class);
                    if(cityWeather!=null) {
                        handleAddCityWeatherResponse(cityWeather);
                    }
                    else {
                        Log.v(TAG,"CityWeather is object is null "+responseString);
                        handleErrorResponse(R.string.alert_city_not_found);
                    }

                }
            });
        } catch (IOException e) {
            Log.e(TAG,getString(R.string.alert_city_not_found) + e.getLocalizedMessage());
        }
    }

    public void addCityWeatherToList(CityWeather cityWeather) {
        boolean success = cityWeatherAdapter.addCityWeather(cityWeather);
        if (!success) {
            Toast.makeText(getApplicationContext(), getString(R.string.alert_city_already_added), Toast.LENGTH_SHORT).show();
        } else {
            cityWeatherAdapter.notifyDataSetChanged();
            recyclerViewCityWeather.smoothScrollToPosition(cityWeatherAdapter.getItemCount() - 1);
            //write updated list to saved preferences
            sharedPreferencesHelper.writeSavedCityIds(cityWeatherAdapter.getCityWeatherIdString());
        }
    }

    public void setCityWeatherList(ArrayList<CityWeather> cityWeathers) {
        if (cityWeathers != null) {
            cityWeatherAdapter.setData(cityWeathers);
            cityWeatherAdapter.notifyDataSetChanged();
        }
    }

}
