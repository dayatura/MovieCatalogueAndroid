package com.example.moviecatalogue.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.moviecatalogue.database.tvShow.TvShowHelper;

import static com.example.moviecatalogue.database.tvShow.TvShowContract.AUTHORITY;
import static com.example.moviecatalogue.database.tvShow.TvShowContract.CONTENT_URI_TV;
import static com.example.moviecatalogue.database.tvShow.TvShowContract.TABLE_TV;

public class TvShowPorvider extends ContentProvider {

    private static final int TV = 1;
    private static final int TV_ID = 2;
    private static final int TV_TITLE = 3;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://com.example.moviecatalogue/movie
        sUriMatcher.addURI(AUTHORITY, TABLE_TV, TV);
        // content://com.example.moviecatalogue/movie/id
        sUriMatcher.addURI(AUTHORITY, TABLE_TV + "/#", TV_ID);
        // content://com.example.moviecatalogue/movie/title
        sUriMatcher.addURI(AUTHORITY, TABLE_TV + "/#", TV_TITLE);


    }

    private TvShowHelper tvShowHelper;

    @Override
    public boolean onCreate() {
        tvShowHelper = TvShowHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        tvShowHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case TV:
                cursor = tvShowHelper.queryProvider();
                break;
            case TV_ID:
                cursor = tvShowHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            case TV_TITLE:
                cursor = tvShowHelper.queryByTitleProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        tvShowHelper.open();
        long added;
        switch (sUriMatcher.match(uri)) {
            case TV:
                added = tvShowHelper.insertProvider(contentValues);
                break;
            default:
                added = 0;
                break;
        }

//        getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE, new MainActivity.DataObserver(new Handler(), getContext()));

        return Uri.parse(CONTENT_URI_TV + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        tvShowHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case TV_ID:
                deleted = tvShowHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

//        getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE, new MainActivity.DataObserver(new Handler(), getContext()));
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        tvShowHelper.open();
        int updated;
        switch (sUriMatcher.match(uri)) {
            case TV_ID:
                updated = tvShowHelper.updateProvider(uri.getLastPathSegment(), contentValues);
                break;
            default:
                updated = 0;
                break;
        }
//        getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE, new MainActivity.DataObserver(new Handler(), getContext()));
        return updated;
    }
}
