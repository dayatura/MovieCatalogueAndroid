package com.example.moviecatalogue.providers;

import com.example.moviecatalogue.models.Movie;

import java.util.ArrayList;

public interface LoadNotesCallback {
    void preExecute();

    void postExecute(ArrayList<Movie> Movies);
}