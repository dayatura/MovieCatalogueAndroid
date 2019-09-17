package com.example.moviecatalogue;

import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.moviecatalogue.fragments.FavoriteFragment;
import com.example.moviecatalogue.fragments.MoviesFragment;
import com.example.moviecatalogue.fragments.SearchFragment;
import com.example.moviecatalogue.fragments.TvShowsFragment;
import com.example.moviecatalogue.notifications.AlarmReceiver;


public class MainActivity extends AppCompatActivity {

    public static final int NOTIFICATION_ID = 1;
    public static String CHANNEL_ID = "channel_01";
    public static CharSequence CHANNEL_NAME = "dicoding channel";

    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;

    private int currFragment;
    private AlarmReceiver alarmReceiver;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.navigation_movies:
                    currFragment = R.id.navigation_movies;
                    fragment = new MoviesFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();

                    return true;
                case R.id.navigation_tv_shows:
                    currFragment = R.id.navigation_tv_shows;
                    fragment = new TvShowsFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
                case R.id.navigation_favorites:
                    currFragment = R.id.navigation_favorites;
                    fragment = new FavoriteFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
            }
            return false;
        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_movies);
            currFragment = R.id.navigation_movies;

            alarmReceiver = new AlarmReceiver();
            SharedPreferences sharedPref = this.getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);

            boolean notificationDefault = getResources().getBoolean(R.bool.notification_default);
            boolean daily_notif = sharedPref.getBoolean(NotificationActivity.DAILY_PREF_KEY, notificationDefault);
            boolean release_notif = sharedPref.getBoolean(NotificationActivity.RELEASE_PREF_KEY, notificationDefault);


            if (daily_notif)
                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING, "07:00", "Daily Update");
            if (release_notif)
                alarmReceiver.setReleaseAlarm(this, AlarmReceiver.TYPE_RELEASE, "08:00");

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    Fragment fragment = new SearchFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("query", query);
                    bundle.putInt("currFragment", currFragment);
                    fragment.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_change_setting:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                return true;

            case R.id.notification_setting:
                Intent notifSettingIntent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(notifSettingIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

}
