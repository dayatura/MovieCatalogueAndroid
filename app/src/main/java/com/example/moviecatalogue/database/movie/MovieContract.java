package com.example.moviecatalogue.database.movie;

import android.provider.BaseColumns;

public final class MovieContract {

    public static final String TABLE_MOVIE = "movies";

    private MovieContract() {
    }

    public static class MovieColumns implements BaseColumns {
        public static final String TITLE = "title";
        public static final String RELEASE_DATE = "release_date";
        public static final String DESCRIPTION = "description";
        public static final String PHOTO = "photo";
    }
}
