package com.example.moviecatalogue.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.moviecatalogue.MainActivity;
import com.example.moviecatalogue.R;
import com.example.moviecatalogue.fragments.MoviesFragment;
import com.example.moviecatalogue.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.example.moviecatalogue.viewModel.MovieViewModel.API_KEY;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String TYPE_REPEATING = "Daily Alarm";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";
    public static final String TYPE_RELEASE = "New Release";

    private static final CharSequence CHANNEL_NAME = "dicoding channel";
    private final static String GROUP_KEY_RELEASES = "group_key_emails";


    // Siapkan 2 id untuk 2 macam alarm, onetime dna repeating
    private final int ID_REPEATING = 101;
    private final int ID_RELEASE = 102;

    private final int MAX_NOTIF = 2;

    private String DATE_FORMAT = "yyyy-MM-dd";
    private String TIME_FORMAT = "HH:mm";

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        if (type.equalsIgnoreCase(TYPE_REPEATING)) {
            showAlarmNotification(context, message);
        } else {
            showReleaseNotificaction(context, new stackNotifCallback() {
                @Override
                public void onSuccess(Context context, ArrayList<NotificationItem> stackNotif) {
                    releaseNotifiCallback(context, stackNotif);
                }
            });
        }

    }

    public void setRepeatingAlarm(Context context, String type, String time, String message) {
        if (isDateInvalid(time, TIME_FORMAT)) return;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);


//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void setReleaseAlarm(Context context, String type, String time) {


        if (isDateInvalid(time, TIME_FORMAT)) return;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_TYPE, type);

        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE, intent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }


    }

    private void showAlarmNotification(Context context, String message) {

        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_movie)
                .setContentTitle(AlarmReceiver.TYPE_REPEATING)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }
        Notification notification = builder.build();
        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(101, notification);
        }
    }

    private void showReleaseNotificaction(final Context context, final stackNotifCallback callback) {


        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<NotificationItem> stackNotif = new ArrayList<>();

        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY +
                "&primary_release_date.gte=" + todayDate +
                "&primary_release_date.lte=" + todayDate;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject weather = list.getJSONObject(i);
                        Movie movie = new Movie(weather, MoviesFragment.MOVIE_TYPE);
                        stackNotif.add(new NotificationItem(i, movie.getTitle(), movie.getTitle() + " is released"));

                    }
                    callback.onSuccess(context, stackNotif);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });

    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_REPEATING) ? ID_REPEATING : ID_RELEASE;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            if (type.equalsIgnoreCase(TYPE_REPEATING))
                Toast.makeText(context, "Repeating notification canceled", Toast.LENGTH_SHORT).show();
            if (type.equalsIgnoreCase(TYPE_RELEASE))
                Toast.makeText(context, "Release notification canceled", Toast.LENGTH_SHORT).show();
            alarmManager.cancel(pendingIntent);
        }

    }

    public boolean isDateInvalid(String date, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }


    public void releaseNotifiCallback(Context context, ArrayList<NotificationItem> stackNotif) {
        int idNotif = stackNotif.size() - 1;

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_movie);
        NotificationCompat.Builder mBuilder;
        String CHANNEL_ID = "channel_2";

        if (idNotif < MAX_NOTIF) {
            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("New Movie : " + stackNotif.get(idNotif).getTitle())
                    .setContentText(stackNotif.get(idNotif).getTitle() + " is released")
                    .setSmallIcon(R.drawable.ic_movie)
                    .setLargeIcon(largeIcon)
                    .setGroup(GROUP_KEY_RELEASES)
                    .setAutoCancel(true);
        } else {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .addLine("New movie : " + stackNotif.get(idNotif).getTitle())
                    .addLine("New movie : " + stackNotif.get(idNotif - 1).getTitle())
                    .setBigContentTitle(idNotif + " new Movies")
                    .setSummaryText("movie releases");
            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(idNotif + " new movies")
                    .setContentText("movie releases")
                    .setSmallIcon(R.drawable.ic_movie)
                    .setGroup(GROUP_KEY_RELEASES)
                    .setGroupSummary(true)
                    .setStyle(inboxStyle)
                    .setAutoCancel(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mBuilder.setChannelId(CHANNEL_ID);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = mBuilder.build();
        if (mNotificationManager != null) {
            mNotificationManager.notify(idNotif, notification);
        }
    }
}

