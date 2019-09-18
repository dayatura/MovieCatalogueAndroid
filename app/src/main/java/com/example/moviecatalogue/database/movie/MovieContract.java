package com.example.moviecatalogue.database.movie;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public final class MovieContract {

    public static final String AUTHORITY = "com.example.moviecatalogue.movie";
    public static final String TABLE_MOVIE = "movies";
    private static final String SCHEME = "content";
    public static final Uri CONTENT_URI_MOVIE = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_MOVIE)
            .build();


    private MovieContract() {
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    public static class MovieColumns implements BaseColumns {
        public static final String TITLE = "title";
        public static final String RELEASE_DATE = "release_date";
        public static final String DESCRIPTION = "description";
        public static final String PHOTO = "photo";

    }

}
