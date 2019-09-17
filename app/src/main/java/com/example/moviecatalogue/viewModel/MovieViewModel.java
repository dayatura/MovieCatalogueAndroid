package com.example.moviecatalogue.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.moviecatalogue.fragments.MoviesFragment;
import com.example.moviecatalogue.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class MovieViewModel extends ViewModel {
    public static final String API_KEY = "00c8962cabba4afe505ab677ff25bf88";
    private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();

    public LiveData<ArrayList<Movie>> getMovies() {
        return listMovies;
    }

    public void setMovies(final String movieType) {
        // request API
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/" + movieType + "?api_key=" + API_KEY + "&language=en-US";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("MOVIE VIEW MODEL", "on started: " + movieType + " " + listItems.size());
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject weather = list.getJSONObject(i);
                        Movie movie = new Movie(weather, movieType);
                        listItems.add(movie);
                    }
                    listMovies.postValue(listItems);
                    Log.d("MOVIE VIEW MODEL", "onSuccess: " + movieType + " " + listItems.size());
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });

    }

    public void searchMovies(final String movieType, String query) {
        // request API
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();
//        https://api.themoviedb.org/3/search/movie?api_key={API KEY}&language=en-US&query={MOVIE NAME}
        String url = "https://api.themoviedb.org/3/search/" + movieType + "?api_key=" + API_KEY + "&language=en-US&query=" + query;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject weather = list.getJSONObject(i);
                        Movie movie = new Movie(weather, movieType);
                        listItems.add(movie);
                    }
                    listMovies.postValue(listItems);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    public ArrayList<Movie> releaseMovies(String todayDate) {
        // request API
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY +
                "&primary_release_date.gte=" + todayDate +
                "&primary_release_date.lte=" + todayDate;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject weather = list.getJSONObject(i);
                        Movie movie = new Movie(weather, MoviesFragment.MOVIE_TYPE);
                        listItems.add(movie);
                    }
                    listMovies.postValue(listItems);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });

        return listItems;
    }
}
