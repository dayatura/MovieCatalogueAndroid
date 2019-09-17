package com.example.moviecatalogue.notifications;

import android.os.AsyncTask;
import android.util.Log;

import com.example.moviecatalogue.fragments.MoviesFragment;
import com.example.moviecatalogue.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.example.moviecatalogue.viewModel.MovieViewModel.API_KEY;

public class ConsumeReleaseTask extends AsyncTask<Void, Void, ArrayList<NotificationItem>> {


    @Override
    protected ArrayList<NotificationItem> doInBackground(Void... voids) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<NotificationItem> listItems = new ArrayList<>();

        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
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
                        listItems.add(new NotificationItem(i + 1, movie.getTitle(), movie.getTitle() + " is released"));
                    }
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
