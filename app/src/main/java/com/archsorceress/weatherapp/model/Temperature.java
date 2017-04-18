package com.archsorceress.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by burcuarabaci on 25/02/17.
 */

@JsonObject
public class Temperature implements Parcelable {
    @JsonField
    private double temp;
    @JsonField
    private int pressure;
    @JsonField
    private int humidity;

    public double getTemp() {
        return temp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.temp);
        dest.writeInt(this.pressure);
        dest.writeInt(this.humidity);
    }

    public Temperature() {
    }

    protected Temperature(Parcel in) {
        this.temp = in.readDouble();
        this.pressure = in.readInt();
        this.humidity = in.readInt();
    }

    public static final Parcelable.Creator<Temperature> CREATOR = new Parcelable.Creator<Temperature>() {
        @Override
        public Temperature createFromParcel(Parcel source) {
            return new Temperature(source);
        }

        @Override
        public Temperature[] newArray(int size) {
            return new Temperature[size];
        }
    };

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
