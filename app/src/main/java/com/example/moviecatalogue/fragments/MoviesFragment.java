package com.example.moviecatalogue.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.moviecatalogue.MovieDetailActivity;
import com.example.moviecatalogue.R;
import com.example.moviecatalogue.adapters.MovieAdapter;
import com.example.moviecatalogue.models.Movie;
import com.example.moviecatalogue.viewModel.MovieViewModel;

import java.util.ArrayList;

public class MoviesFragment extends Fragment {

    public static final String MOVIE_TYPE = "movie";

    private MovieAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private MovieViewModel movieViewModel;
    private Observer<ArrayList<Movie>> getMovies = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(ArrayList<Movie> movies) {
            if (movies != null) {
                adapter.setListMovies(movies);
                showLoading(false);
            }
        }
    };

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(this, getMovies);

        adapter = new MovieAdapter();
        adapter.notifyDataSetChanged();

        recyclerView = view.findViewById(R.id.rv_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie data) {
                Intent movieIntent = new Intent(recyclerView.getContext(), MovieDetailActivity.class);
                movieIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE, data);
                movieIntent.putExtra(MovieDetailActivity.EXTRA_TYPE, MOVIE_TYPE);
                startActivity(movieIntent);
            }
        });

        progressBar = view.findViewById(R.id.progressBar);

        movieViewModel.setMovies(MOVIE_TYPE);
        showLoading(true);


    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }


}
