package com.softwarebloat.themovieapp.DAO;


import android.os.Parcel;
import android.os.Parcelable;

public class ReviewDAO implements Parcelable {

    private final String author;
    private final String content;

    public ReviewDAO(String author, String content) {
        this.author = author;
        this.content = content;
    }

    private ReviewDAO(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public static final Creator<ReviewDAO> CREATOR = new Creator<ReviewDAO>() {
        @Override
        public ReviewDAO createFromParcel(Parcel in) {
            return new ReviewDAO(in);
        }

        @Override
        public ReviewDAO[] newArray(int size) {
            return new ReviewDAO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
    }
}
