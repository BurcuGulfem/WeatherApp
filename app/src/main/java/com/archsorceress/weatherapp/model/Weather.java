package com.archsorceress.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by burcuarabaci on 24/02/17.
 */

@JsonObject
public class Weather implements Parcelable {
    @JsonField
    private int id;
    @JsonField  (name = "main")
    private String title;
    @JsonField
    private String description;
    @JsonField  (name = "icon")
    private String iconId;


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIconId() {
        return iconId;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.iconId);
    }

    public Weather() {
    }

    protected Weather(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.iconId = in.readString();
    }

    public static final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel source) {
            return new Weather(source);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    //Setters are being used by logansquare

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }
}
