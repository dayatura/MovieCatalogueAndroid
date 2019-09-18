package com.example.moviecatalogue.database;

import android.database.SQLException;

import com.example.moviecatalogue.models.Movie;

public interface InstascesHelper {
    void open() throws SQLException;

    void close();

    boolean isAny(Movie movie);

    long insert(Movie movie);

    int delete(Movie movie);
}
