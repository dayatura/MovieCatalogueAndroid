package com.example.moviecatalogue.notifications;

import android.content.Context;

import java.util.ArrayList;

public interface stackNotifCallback {
    void onSuccess(Context context, ArrayList<NotificationItem> stackNotif);
}
