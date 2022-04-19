package controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.tentative.R;

import model.MySQLiteOpenHelper;

public class Mon_Compte extends AppCompatActivity {
   TextView nom;
   TextView prenom;
   TextView ID;
   TextView username;
   TextView password;
   TextView mail;
   TextView tel;
   TextView numpiece;
   TextView categorie;
   TextView adresse;
   Button retour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id=getIntent().getExtras().getString("username");
        setContentView(R.layout.activity_mon__compte);
        Toolbar toolbar=findViewById(R.id.toobar_mcpte);
        toolbar.setTitle("Mon compte");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        retour=findViewById(R.id.retour_moncompte);
        nom=findViewById(R.id.nom_nomcpte);
        prenom=findViewById(R.id.prenom_nomcpte);
        username=findViewById(R.id.username_nomcpte);
        password=findViewById(R.id.password_nomcpte);
        mail=findViewById(R.id.mail_nomcpte);
        tel=findViewById(R.id.tel_nomcpte);
        ID=findViewById(R.id.id_nomcpte);
        numpiece=findViewById(R.id.numpiece_nomcpte);
        categorie=findViewById(R.id.type_nomcpte);
        adresse=findViewById(R.id.adresse_nomcpte);
        MySQLiteOpenHelper data=new MySQLiteOpenHelper(this,null);
        SQLiteDatabase base=data.getReadableDatabase();
        Cursor cursor=base.rawQuery("SELECT ID,NOM,PRENOM,USERNAME,PASSWORD,MAIL,TELEPHONE,NUM_PIECE,ADRESSE,CATEGORIE FROM ADMINISTRATEURS WHERE USERNAME=?", new String[] {id});
      cursor.moveToFirst();
      ID.setText(cursor.getString(0));
      nom.setText(cursor.getString(1));
      prenom.setText(cursor.getString(2));
      username.setText(cursor.getString(3));
      password.setText(cursor.getString(4));
      mail.setText(cursor.getString(5));
      tel.setText(cursor.getString(6));
      numpiece.setText(cursor.getString(7));
      adresse.setText(cursor.getString(8));
      categorie.setText(cursor.getString(9));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.supprimer_modifier, menu);
        return true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        { case R.id.supprimer_menu:
        {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Voulez-vous vraiment  supprimer votre compte ?").setTitle("Supprimer?").setNegativeButton("Non", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MySQLiteOpenHelper db=new MySQLiteOpenHelper(getApplicationContext(),null);
                    SQLiteDatabase database=db.getWritableDatabase();
                    database.delete("ADMINISTRATEURS"," USERNAME=?",new String [] {username.getText().toString()});
                    finishAffinity();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);

                }
            }).create().show();}

            return true;
            case R.id.modifier_menu:
                Intent intent=new Intent(this,Modifier_infos_admin.class);
                intent.putExtras(getIntent().getExtras());
                startActivity(intent);
                return true;
            default:return false;


        }


    }
}