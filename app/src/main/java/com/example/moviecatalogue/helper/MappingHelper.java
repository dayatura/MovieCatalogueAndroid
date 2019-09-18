package com.example.moviecatalogue.helper;

import android.database.Cursor;

import com.example.moviecatalogue.models.Movie;

import java.util.ArrayList;

import static com.example.moviecatalogue.database.movie.MovieContract.MovieColumns.DESCRIPTION;
import static com.example.moviecatalogue.database.movie.MovieContract.MovieColumns.PHOTO;
import static com.example.moviecatalogue.database.movie.MovieContract.MovieColumns.RELEASE_DATE;
import static com.example.moviecatalogue.database.movie.MovieContract.MovieColumns.TITLE;
import static com.example.moviecatalogue.database.movie.MovieContract.MovieColumns._ID;

public class MappingHelper {

    public static ArrayList<Movie> mapCursorToArrayList(Cursor notesCursor) {
        ArrayList<Movie> movieList = new ArrayList<>();
        while (notesCursor.moveToNext()) {
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(_ID));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE));
            String description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DESCRIPTION));
            String releaseDate = notesCursor.getString(notesCursor.getColumnIndexOrThrow(RELEASE_DATE));
            String photo = notesCursor.getString(notesCursor.getColumnIndexOrThrow(PHOTO));
            movieList.add(new Movie(id, title, description, releaseDate, photo));
        }
        return movieList;
    }

}
