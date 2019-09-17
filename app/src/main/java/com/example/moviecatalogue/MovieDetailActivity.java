package com.example.moviecatalogue;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviecatalogue.database.InstascesHelper;
import com.example.moviecatalogue.database.movie.MovieContract;
import com.example.moviecatalogue.database.movie.MovieContract.MovieColumns;
import com.example.moviecatalogue.database.movie.MovieDbHelper;
import com.example.moviecatalogue.database.movie.MovieHelper;
import com.example.moviecatalogue.database.tvShow.TvShowContract;
import com.example.moviecatalogue.database.tvShow.TvShowDbHelper;
import com.example.moviecatalogue.database.tvShow.TvShowHelper;
import com.example.moviecatalogue.models.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_TYPE = "extra_type";
    public String tableName;
    SQLiteOpenHelper dbHelper;
    private Movie movie;
    private boolean liked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        InstascesHelper instascesHelper;

        if (getIntent().getStringExtra(EXTRA_TYPE).equals("movie")) {
            instascesHelper = MovieHelper.getInstance(this);
        } else {
            instascesHelper = TvShowHelper.getInstance(this);
        }

        ImageView ivPhoto = findViewById(R.id.img_photo);
        TextView tvTitle = findViewById(R.id.txt_title);
        TextView tvReleaseDate = findViewById(R.id.txt_release_date);
        TextView tvDescription = findViewById(R.id.txt_description);

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        Picasso.get().load(movie.getPhoto()).into(ivPhoto);
        tvTitle.setText(movie.getTitle());
        tvReleaseDate.setText(movie.getReleaseDate());
        tvDescription.setText(movie.getDescription());

        liked = instascesHelper.isAny(movie);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (liked) inflater.inflate(R.menu.detail_movie_menu_liked, menu);
        else inflater.inflate(R.menu.detail_movie_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorite:

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                if (liked) {
                    unlike(db);
                    item.setIcon(R.drawable.ic_favorite_white_24dp);
                    liked = false;
                } else {
                    like(db);
                    item.setIcon(R.drawable.ic_favorite_red_24dp);
                    liked = true;
                }

                return true;

            default:
                return true;

        }
    }

    private void like(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(MovieColumns.TITLE, movie.getTitle());
        values.put(MovieColumns.RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieColumns.DESCRIPTION, movie.getDescription());
        values.put(MovieColumns.PHOTO, movie.getPhoto());

        long newRowId = db.insert(tableName, null, values);
        db.close();

        Toast.makeText(getApplicationContext(), getString(R.string.favorite_added), Toast.LENGTH_SHORT).show();

    }

    private void unlike(SQLiteDatabase db) {
        String selection = MovieColumns.TITLE + " LIKE ?";
        String[] selectionArgs = {movie.getTitle()};
        db.delete(tableName, selection, selectionArgs);

        Toast.makeText(getApplicationContext(), getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show();
    }
}
