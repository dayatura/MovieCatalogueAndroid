package com.example.moviecatalogue.widgets;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.moviecatalogue.database.movie.MovieContract;
import com.example.moviecatalogue.database.tvShow.TvShowContract;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DownloadImageTask extends AsyncTask<SQLiteOpenHelper, Void, List<Bitmap>> {
    @Override
    protected List<Bitmap> doInBackground(SQLiteOpenHelper... helpers) {

        SQLiteDatabase db = helpers[0].getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                MovieContract.MovieColumns.PHOTO
        };

        String tableName = helpers[0].getClass().getSimpleName() == "MoviesDbHelper" ?
                MovieContract.TABLE_MOVIE : TvShowContract.TABLE_TV;


        Cursor cursor = db.query(
                tableName, projection, null, null, null, null, null
        );

        List<Bitmap> buffPosters = new ArrayList<>();

        while (cursor.moveToNext()) {
            String urldisplay = cursor.getString(cursor.getColumnIndex(MovieContract.MovieColumns.PHOTO));
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            buffPosters.add(bmp);
        }

        return buffPosters;
    }
}
