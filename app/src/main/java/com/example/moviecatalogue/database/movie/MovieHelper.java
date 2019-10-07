package com.example.moviecatalogue.database.movie;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.moviecatalogue.database.InstascesHelper;
import com.example.moviecatalogue.models.Movie;

import java.util.ArrayList;

import static com.example.moviecatalogue.database.movie.MovieContract.MovieColumns.DESCRIPTION;
import static com.example.moviecatalogue.database.movie.MovieContract.MovieColumns.PHOTO;
import static com.example.moviecatalogue.database.movie.MovieContract.MovieColumns.RELEASE_DATE;
import static com.example.moviecatalogue.database.movie.MovieContract.MovieColumns.TITLE;
import static com.example.moviecatalogue.database.movie.MovieContract.MovieColumns._ID;

public class MovieHelper implements InstascesHelper {
    private static final String DATABASE_TABLE = MovieContract.TABLE_MOVIE;
    private static MovieDbHelper dataBaseHelper;
    private static MovieHelper INSTANCE;

    private static SQLiteDatabase database;

    private MovieHelper(Context context) {
        dataBaseHelper = new MovieDbHelper(context);
    }

    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    @Override
    public void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }


    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                movie.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(PHOTO)));

                arrayList.add(movie);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    @Override
    public boolean isAny(Movie movie) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE title = \"" + movie.getTitle() + "\"", null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }

    }

    public long insert(Movie movie) {
        ContentValues args = new ContentValues();
        args.put(TITLE, movie.getTitle());
        args.put(DESCRIPTION, movie.getDescription());
        args.put(RELEASE_DATE, movie.getReleaseDate());
        args.put(PHOTO, movie.getPhoto());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int update(Movie movie) {
        ContentValues args = new ContentValues();
        args.put(TITLE, movie.getTitle());
        args.put(DESCRIPTION, movie.getDescription());
        args.put(RELEASE_DATE, movie.getReleaseDate());
        args.put(PHOTO, movie.getPhoto());
        return database.update(DATABASE_TABLE, args, _ID + "= '" + movie.getId() + "'", null);
    }

    public int delete(Movie movie) {
        return database.delete(DATABASE_TABLE, TITLE + " = '" + movie.getTitle() + "'", null);
    }

    // for content provider purpose

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryByTitleProvider(String title) {
        return database.query(DATABASE_TABLE, null
                , TITLE + " = ?"
                , new String[]{title}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String title) {
        return database.delete(DATABASE_TABLE, TITLE + " = ?", new String[]{title});
    }


}
