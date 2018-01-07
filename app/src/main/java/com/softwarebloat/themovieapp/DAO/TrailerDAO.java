package com.softwarebloat.themovieapp.DAO;

import android.os.Parcel;
import android.os.Parcelable;

public class TrailerDAO implements Parcelable {

    private final String trailerId;

    public TrailerDAO(String trailerId) {
        this.trailerId = trailerId;
    }

    public String getTrailerId() {
        return trailerId;
    }

    private TrailerDAO(Parcel in) {
        trailerId = in.readString();
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
    }
}
