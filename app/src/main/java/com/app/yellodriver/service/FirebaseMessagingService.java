package com.app.yellodriver.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.app.yellodriver.R;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.YLog;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import io.paperdb.Paper;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onNewToken(@NotNull String token) {
        super.onNewToken(token);
        YLog.e("Fcm Token", token);
        Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_FIREBASE_TOKEN, token);
    }

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        RemoteMessage.Notification remoteMessageNotification = remoteMessage.getNotification();

        if (remoteMessageNotification != null) {
            YLog.e("Notification", "Message Notification Title:" + remoteMessageNotification.getTitle());
            YLog.e("Notification", "Message Notification Body:" + remoteMessageNotification.getBody());

            generateNotification(remoteMessageNotification.getTitle(), remoteMessageNotification.getBody());
        }
    }

    public void generateNotification(String title, String message) {

        Random random = new Random();
        int randomNo = random.nextInt();

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, randomNo, intent, 0);

        String channelId = YelloDriverApp.getInstance().getString(R.string.app_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(getNotificationIcon())
                        .setContentTitle(title)
                        .setContentText(message)
                        .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    YelloDriverApp.getInstance().getString(R.string.notification_channel_other),
                    NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(randomNo/* ID of notification */, notificationBuilder.build());
        }
    }

    private int getNotificationIcon() {

        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
//        return useWhiteIcon ? R.mipmap.ic_notification_white : R.mipmap.ic_notification;
        return useWhiteIcon ? R.drawable.ic_notification : R.drawable.ic_notification;
    }
}