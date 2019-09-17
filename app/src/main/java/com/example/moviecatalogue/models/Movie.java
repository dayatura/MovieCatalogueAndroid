package com.example.moviecatalogue.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;


public class Movie implements Parcelable {
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private int id;
    private String photo;
    private String title;
    private String releaseDate;
    private String description;


    public Movie(JSONObject object, String movieType) {

        try {

            String title;
            String releaseData;

            int id = object.getInt("id");

            if (movieType.equals("movie")) {
                title = object.getString("title");
                releaseData = object.getString("release_date");
            } else {
                title = object.getString("name");
                releaseData = object.getString("first_air_date");
            }

            String description = object.getString("overview");
            String photo = object.getString("poster_path");

            String posterRoot = "https://image.tmdb.org/t/p/";
            String posterSize = "w154";

            this.id = id;
            this.title = title;
            this.releaseDate = releaseData;
            this.description = description;
            this.photo = posterRoot + posterSize + photo;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.photo = in.readString();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.description = in.readString();
    }


    public Movie() {

    }

    public Movie(int id, String title, String description, String releaseData, String photo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseData;
        this.photo = photo;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.photo);
        dest.writeString(this.title);
        dest.writeString(this.releaseDate);
        dest.writeString(this.description);
    }


}
