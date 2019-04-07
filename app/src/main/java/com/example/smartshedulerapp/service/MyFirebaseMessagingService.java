package com.example.smartshedulerapp.service;

import static android.app.Notification.DEFAULT_ALL;
import static android.content.ContentValues.TAG;
import static com.example.smartshedulerapp.util.Constants.DEVICE_ID_KEY;
import static com.example.smartshedulerapp.util.Constants.FCM_TOKEN;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Builder;
import com.example.smartshedulerapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

  public MyFirebaseMessagingService() {

  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);

    showNotifcation(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
  }

  private void showNotifcation(String title, String body) {
    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    String notificationChannelId = "com.example.smartshedulerapp";

    if (VERSION.SDK_INT >= VERSION_CODES.O) {

      NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, "Notification",
          NotificationManager.IMPORTANCE_DEFAULT);

      notificationChannel.setDescription("Description");
      notificationChannel.enableLights(true);
      notificationChannel.setLightColor(Color.BLUE);
      notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
      notificationChannel.enableLights(true);
      notificationManager.createNotificationChannel(notificationChannel);

    }

    NotificationCompat.Builder notificationBuilder = new Builder(this, notificationChannelId);

    Notification notification = notificationBuilder.setAutoCancel(true)
        .setDefaults(DEFAULT_ALL)
        .setWhen(System.currentTimeMillis())
        .setSmallIcon(R.drawable.calendar_logo)
        .setContentTitle(title)
        .setContentText(body)
        .setContentInfo("info")
        .build();

    notificationManager.notify(new Random().nextInt(), notification);

  }

  @Override
  public void onNewToken(String token) {
    super.onNewToken(token);

    Log.d(TAG, "Firebase token: " + token);

    String deviceId = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);

    Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
    editor.putString(DEVICE_ID_KEY, deviceId);
    editor.putString(FCM_TOKEN, token);
    editor.apply();
  }
}
