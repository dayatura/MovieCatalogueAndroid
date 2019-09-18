package com.example.moviecatalogue.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.moviecatalogue.models.Movie;

import java.util.ArrayList;

import static com.example.moviecatalogue.database.movie.MovieContract.CONTENT_URI_MOVIE;
import static com.example.moviecatalogue.database.tvShow.TvShowContract.CONTENT_URI_TV;
import static com.example.moviecatalogue.helper.MappingHelper.mapCursorToArrayList;

public class DbViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();

    public LiveData<ArrayList<Movie>> getMovies() {
        return listMovies;
    }

    public void setMovies(String movieType, final Context context) {

        if (movieType.equals("movie")) {

            // load database fav
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Cursor cursor = context.getContentResolver().query(CONTENT_URI_MOVIE, null, null, null, null);
                    if (cursor.getCount() > 0) listMovies.postValue(mapCursorToArrayList(cursor));
                }
            });

//            MovieHelper movieHelper = MovieHelper.getInstance(context);
//            movieHelper.open();
//            listMovies.postValue(movieHelper.getAllMovies());
//            movieHelper.close();

        } else {

            // load database fan tv
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Cursor cursor = context.getContentResolver().query(CONTENT_URI_TV, null, null, null, null);
                    if (cursor != null) listMovies.postValue(mapCursorToArrayList(cursor));
                }
            });

//            TvShowHelper tvShowHelper = TvShowHelper.getInstance(context);
//            tvShowHelper.open();
//            listMovies.postValue(tvShowHelper.getAllMovies());
//            tvShowHelper.close();
        }

    }

}
