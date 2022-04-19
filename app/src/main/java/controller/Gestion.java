package controller;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.tentative.R;

public class Gestion extends Application {
    public static final String CHANNEL_1="channel";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(CHANNEL_1,"channel",NotificationManager.IMPORTANCE_HIGH);
            channel.setLightColor(getResources().getColor(
                    R.color.test1));
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
