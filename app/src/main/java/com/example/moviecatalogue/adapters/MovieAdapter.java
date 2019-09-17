package com.example.moviecatalogue.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviecatalogue.R;
import com.example.moviecatalogue.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private ArrayList<Movie> listMovies = new ArrayList<>();
    private Activity activity;
    private OnItemClickCallback onItemClickCallback;


    public MovieAdapter() {

    }

    public ArrayList<Movie> getListMovies() {
        return listMovies;
    }

    public void setListMovies(ArrayList<Movie> listMovies) {

        if (listMovies.size() > 0) {
            this.listMovies.clear();
        }
        this.listMovies.addAll(listMovies);

        notifyDataSetChanged();
    }

    public void addItem(Movie note) {
        this.listMovies.add(note);
        notifyItemInserted(listMovies.size() - 1);
    }

    public void updateItem(int position, Movie note) {
        this.listMovies.set(position, note);
        notifyItemChanged(position, note);
    }

    public void removeItem(int position) {
        this.listMovies.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listMovies.size());
    }


// late


    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

// late

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Movie movie = listMovies.get(i);

        viewHolder.txtTitle.setText(movie.getTitle());
        viewHolder.txtReleaseDate.setText(movie.getReleaseDate());
        Picasso.get().load(movie.getPhoto()).into(viewHolder.imgPhoto);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(listMovies.get(viewHolder.getAdapterPosition()));
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtReleaseDate;
        private ImageView imgPhoto;

        public ViewHolder(View view) {
            super(view);
            txtTitle = view.findViewById(R.id.txt_title);
            txtReleaseDate = view.findViewById(R.id.txt_release_date);
            imgPhoto = view.findViewById(R.id.img_photo);
        }

    }

}
