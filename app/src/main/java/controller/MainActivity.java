package controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.example.tentative.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.*;

import static controller.Gestion.CHANNEL_1;

public class MainActivity extends AppCompatActivity {
  public static Activity Main;
  private  MySQLiteOpenHelper db;
    private EditText m_nom;
    private TextInputEditText m_mdp;
    private Button m_sub;
    private user util=new user("","");
    private String mdp;
    @BindView(R.id.connect) Button m_sub1;
    @BindView(R.id.retour) Button m_cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Main=this;
        setContentView(R.layout.activity_main);
        db = new MySQLiteOpenHelper(this, null);
         ButterKnife.bind(this);

        m_nom=(EditText) findViewById(R.id.username);
        m_mdp=(TextInputEditText) findViewById(R.id.password);
        m_sub=(Button) findViewById(R.id.connect);
        m_mdp.setText("");
     MySQLiteOpenHelper hd = new MySQLiteOpenHelper(getBaseContext(), null);
        ArrayList<String[]> produits=hd.produits_perimes();
        int i=0;
        while (i<produits.size())
        {
        {
            Uri alarmSound = RingtoneManager.getDefaultUri(R.raw.notification);

            NotificationCompat.Builder notification=new NotificationCompat.Builder(getApplicationContext(),CHANNEL_1)
                    .setContentTitle("Notification").setAutoCancel(true).setSound(alarmSound)
                    .setContentText("Le lot "+produits.get(i)[5]+" du produit " +produits.get(i)[1]+" est périmé ")
                    .setPriority(NotificationManager.IMPORTANCE_HIGH).setSmallIcon(R.drawable.peremption_foreground);
            notification.setSound(Uri.parse("android.resource://"
                            + getApplicationContext().getPackageName() + "/" + R.raw.to_the_point_568));
            NotificationManager notificationManager=(NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(i,notification.build());

        }
            i++;
    }
        }


    @Override
    protected void onResume()
    {super.onResume();


    }
    @OnClick (R.id.connect)
    public void connect(){
       if(!m_nom.getText().toString().isEmpty() && !m_mdp.getText().toString().isEmpty())
        // The user just clicked
       {
           String nom=new String();
        nom=m_nom.getText().toString();
        String [] infos=new String[3];
        infos=db.infos(nom);

        if(infos==null)
        {
            Toast.makeText(getApplicationContext(),"Le nom d'utilisateur n'existe pas",Toast.LENGTH_LONG).show();
        }
        else
        {mdp=infos[2];
            if(mdp.equals(m_mdp.getText().toString()))

        {
            util.setMnom(nom);
            util.setMmotdepasse(mdp);
            Intent intent=new Intent(getApplicationContext(), Principale.class);
            Bundle b=new Bundle();
            b.putString("username",nom);
            b.putString("nom",infos[0]);
            b.putString("prenom",infos[1]);
            intent.putExtras(b);
            finishAffinity();
            startActivity(intent);


        }
        else
        {Toast.makeText(getApplicationContext(),"Le mot de passe est érroné",Toast.LENGTH_LONG).show();

        }
        }}
       else Toast.makeText(getApplicationContext(),"Veuillez donner un mmot de passe et un nom d'utilisateur",Toast.LENGTH_LONG).show();

    }
    @OnClick (R.id.retour)
    public void retour(){
      finish();
    }
}