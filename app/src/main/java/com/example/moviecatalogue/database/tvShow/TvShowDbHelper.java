package com.example.moviecatalogue.database.tvShow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.moviecatalogue.database.tvShow.TvShowContract.TvShowColumns;

public class TvShowDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_TV =
            String.format("CREATE TABLE %s"
                            + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT NOT NULL)",
                    TvShowContract.TABLE_TV,
                    TvShowColumns._ID,
                    TvShowColumns.TITLE,
                    TvShowColumns.RELEASE_DATE,
                    TvShowColumns.DESCRIPTION,
                    TvShowColumns.PHOTO
            );
    private static final String SQL_DELETE_TABLE_MOVIE =
            "DROP TABLE IF EXISTS " + TvShowContract.TABLE_TV;
    public static String DATABASE_NAME = "dbTvShow";

    public TvShowDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_TV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_TV);
    }
}
