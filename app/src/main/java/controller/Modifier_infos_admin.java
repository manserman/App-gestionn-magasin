package controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tentative.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import model.MySQLiteOpenHelper;

public class Modifier_infos_admin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Button effectuer;
    Button annuler;
    TextInputEditText Nom;
    TextInputEditText Prenom;
    TextInputEditText telephone;
    TextInputEditText Mail;
    TextInputEditText adresse;
    TextInputEditText username1;
    TextInputEditText password;
    TextInputEditText numpiece;
    LinearLayoutCompat layout;
    DrawerLayout drawerlayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView nav_view;
    int type;
    int id0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b=getIntent().getExtras();
        type=getIntent().getExtras().getInt("type");
        setContentView(R.layout.activity_modifier_infos_admin);
        layout=findViewById(R.id.layout_modif);
        annuler=layout.findViewById(R.id.annuler);
        effectuer=layout.findViewById(R.id.save);
        drawerlayout=(DrawerLayout)layout.findViewById(R.id.drawerlayout_modif_admin);
        NavigationView nav=(NavigationView)drawerlayout.findViewById(R.id.navigation_view);
        View vue=nav.getHeaderView(0);
        nav_view=drawerlayout.findViewById(R.id.navigation_view);
        nav_view.setNavigationItemSelectedListener(this);
        nav_view.bringToFront();
        TextView nom=vue.findViewById(R.id.nom_header);
        TextView username=vue.findViewById(R.id.username_header);
        // Find the toolbar view inside the activity layout
        nom.setText(b.getString("nom")+" "+b.getString("prenom"));
        username.setText("@"+b.getString("username"));
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            toolbar.setTitle("Modifier  mes informations");
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
        Nom=layout.findViewById(R.id.nom);
        Prenom=layout.findViewById(R.id.prenom);
        telephone=layout.findViewById(R.id.tel);
        Mail=layout.findViewById(R.id.mail);
        adresse=layout.findViewById(R.id.adresse);
        numpiece=layout.findViewById(R.id.numpiece);
        password=layout.findViewById(R.id.password);
        username1=layout.findViewById(R.id.username_mo);
        MySQLiteOpenHelper data=new MySQLiteOpenHelper(this,null);
            SQLiteDatabase db=data.getReadableDatabase();
            Cursor cursor=db.rawQuery("SELECT NOM,PRENOM,TELEPHONE,MAIL,ADRESSE,USERNAME,PASSWORD,NUM_PIECE FROM ADMINISTRATEURS WHERE USERNAME=?",new String[] {getIntent().getExtras().getString("username")});
            cursor.moveToFirst();
            Nom.setText(cursor.getString(0));
            Prenom.setText(cursor.getString(1));
            telephone.setText(cursor.getString(2));
            Mail.setText(cursor.getString(3));
            username1.setText(cursor.getString(5));
            password.setText(cursor.getString(6));
            adresse.setText(cursor.getString(4));
           numpiece.setText(cursor.getString(7));



    }@Override
    protected void onResume()
    {super.onResume();
        Activity mai=this;
        effectuer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View t){
                MySQLiteOpenHelper database = new MySQLiteOpenHelper(getApplicationContext(), null);
                SQLiteDatabase db = database.getReadableDatabase();

                    ContentValues cv=new ContentValues();
                    cv.put("NOM",Nom.getText().toString());
                    cv.put("PRENOM",Prenom.getText().toString());
                    cv.put("TELEPHONE",telephone.getText().toString());
                    cv.put("MAIL",Mail.getText().toString());
                    cv.put("ADRESSE",adresse.getText().toString());
                    cv.put("PASSWORD",password.getText().toString());
                    cv.put("USERNAME",username1.getText().toString());
                    cv.put("NUM_PIECE",numpiece.getText().toString());
                   int i= db.update("ADMINISTRATEURS",cv,"USERNAME=?",new String[] {getIntent().getExtras().getString("username")});
                    finishAffinity();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }

        });
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {if(actionBarDrawerToggle.onOptionsItemSelected(item))
    {return true;}
    else return false;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(drawerlayout.isDrawerOpen(GravityCompat.START))
            drawerlayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId())
        { case R.id.produits_menu:
            Intent intent=new Intent(getApplicationContext(),Produits.class);
            intent.putExtras(getIntent().getExtras());
            startActivity(intent);
            break;
            case R.id.deconnexion_header:
                finishAffinity();
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.mon_compte_menu:
                intent=new Intent(this,Mon_Compte.class);
                intent.putExtras(getIntent().getExtras());
                startActivity(intent);
                break; case R.id.clients_menu:
            intent=new Intent(this,Clients.class);
            intent.putExtras(getIntent().getExtras());
            startActivity(intent);
            break; case R.id.fournisseurs_menu:
            intent=new Intent(this,Fournisseurs.class);
            intent.putExtras(getIntent().getExtras());
            startActivity(intent);
            break; case R.id.utilisateurs_menu:
            intent=new Intent(this,Administrateurs.class);
            intent.putExtras(getIntent().getExtras());
            startActivity(intent);
            break; case R.id. commandes_menu:
            intent=new Intent(this,Commandes.class);
            intent.putExtras(getIntent().getExtras());
            startActivity(intent);
            break; case R.id.factures_menu:
            intent=new Intent(this,Factures.class);
            intent.putExtras(getIntent().getExtras());
            startActivity(intent);
            break;
            default: return false;
        } return true;
    }

}