package com.example.todoappapi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    // 🔁 Bildirim alınca tetiklenir
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

       //furkan: firebaseden gelen bildirim varsa gösteriyor burası title , message 2 parametre var .
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            showNotification(title, message);
        }

        // 🔧 Ek veri ile gelen özel veri mesajı varsa (isteğe bağlı)
        if (remoteMessage.getData().size() > 0) {
            Log.d("FCM_DATA", "Data Payload: " + remoteMessage.getData());
        }
    }

    // 🔑 Uygulama ilk kurulduğunda veya token değiştiğinde tetiklenir
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("FCM_TOKEN", "Yeni Token: " + token);
        //furkan:token sunucuya kaydedilebilir istersen.

    }

    //furkan:bildirim gösterme metodu burada, 2 parametresi var , title , message.
    private void showNotification(String title, String message) {
        String channelId = "todo_channel";

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Android 8+ için kanal tanımla
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "ToDo Bildirimleri",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(channel);
        }

        // Bildirime tıklanınca açılacak ekran
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pi = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

    //furkan: bildirim ayarları burada

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //furkan:bildirim oluşturmak için gereken kısım burası.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pi);

        // Bildirimi göster
        manager.notify(0, builder.build());
    }
}
