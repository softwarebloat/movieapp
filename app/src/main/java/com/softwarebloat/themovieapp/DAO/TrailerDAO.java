package com.softwarebloat.themovieapp.DAO;

import android.os.Parcel;
import android.os.Parcelable;

public class TrailerDAO implements Parcelable {

    private final String trailerId;
    private final String type;

    public TrailerDAO(String trailerId, String type) {
        this.trailerId = trailerId;
        this.type = type;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public String getType() {
        return type;
    }

    private TrailerDAO(Parcel in) {
        trailerId = in.readString();
        type = in.readString();
    }

    public static final Creator<TrailerDAO> CREATOR = new Creator<TrailerDAO>() {
        @Override
        public TrailerDAO createFromParcel(Parcel in) {
            return new TrailerDAO(in);
        }

        @Override
        public TrailerDAO[] newArray(int size) {
            return new TrailerDAO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailerId);
        dest.writeString(type);
    }
}
