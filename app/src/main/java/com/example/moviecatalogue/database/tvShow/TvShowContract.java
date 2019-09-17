package com.example.moviecatalogue.database.tvShow;

import android.provider.BaseColumns;

public final class TvShowContract {

    public static final String TABLE_TV = "tvShows";

    private TvShowContract() {
    }

    public static class TvShowColumns implements BaseColumns {
        public static final String TITLE = "title";
        public static final String RELEASE_DATE = "release_date";
        public static final String DESCRIPTION = "description";
        public static final String PHOTO = "photo";
    }

}
