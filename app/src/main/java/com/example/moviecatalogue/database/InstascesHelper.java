package com.example.moviecatalogue.database;

import android.content.Context;
import android.database.SQLException;

import com.example.moviecatalogue.models.Movie;

public interface InstascesHelper {
    void open() throws SQLException;
    void close();
    boolean isAny(Movie movie);
}
