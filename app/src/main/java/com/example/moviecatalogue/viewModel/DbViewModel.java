package com.example.moviecatalogue.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.moviecatalogue.database.movie.MovieHelper;
import com.example.moviecatalogue.database.tvShow.TvShowHelper;
import com.example.moviecatalogue.models.Movie;

import java.util.ArrayList;

public class DbViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();

    public LiveData<ArrayList<Movie>> getMovies() {
        return listMovies;
    }

    public void setMovies(final String movieType, Context context) {

        if (movieType.equals("movie")) {
            // load database fav movies
            MovieHelper movieHelper = MovieHelper.getInstance(context);
            movieHelper.open();
            listMovies.postValue(movieHelper.getAllMovies());
            movieHelper.close();

        } else {
            // load database fan tv
            TvShowHelper tvShowHelper = TvShowHelper.getInstance(context);
            tvShowHelper.open();
            listMovies.postValue(tvShowHelper.getAllMovies());
            tvShowHelper.close();
        }

    }

}
