package controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.tentative.R;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.time.format.DateTimeFormatter.*;
import model.MySQLiteOpenHelper;
import java.util.*;

import static controller.Gestion.CHANNEL_1;

public class MyDateReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        MySQLiteOpenHelper hd = new MySQLiteOpenHelper(context, null);
        ArrayList<String[]> produits=hd.produits_perimes();
        for(int i=0;i<produits.size();i++)
        {
        {
            Notification notification=new NotificationCompat.Builder(context,CHANNEL_1)
                    .setContentTitle("Notification")
                    .setContentText("Le lot "+produits.get(i)[5]+" du produit: "+produits.get(i)[1]+" est périmé")
                    .setPriority(NotificationManager.IMPORTANCE_HIGH).setSmallIcon(R.drawable.peremption_foreground).setSound(Uri.parse("android.resource://"
                            + context.getPackageName() + "/" + R.raw.to_the_point_568))
                    .build();

            NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(i,notification);

        }


        }
    }
}