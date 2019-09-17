package com.example.moviecatalogue.database.movie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.moviecatalogue.database.movie.MovieContract.MovieColumns;


public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_MOVIE =
            String.format("CREATE TABLE %s"
                            + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL)",
                    MovieContract.TABLE_MOVIE,
                    MovieColumns._ID,
                    MovieColumns.TITLE,
                    MovieColumns.RELEASE_DATE,
                    MovieColumns.DESCRIPTION,
                    MovieColumns.PHOTO
            );
    private static final String SQL_DELETE_TABLE_MOVIE =
            "DROP TABLE IF EXISTS " + MovieContract.TABLE_MOVIE;
    public static String DATABASE_NAME = "dbMovieApp";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_MOVIE);
        onCreate(sqLiteDatabase);
    }
}
