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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tentative.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import model.MySQLiteOpenHelper;

public class Ajouter_Admin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
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
    RadioButton type;
    RadioGroup radio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b=getIntent().getExtras();
        setContentView(R.layout.activity_ajouter__admin);
        layout=findViewById(R.id.layout_ajout_admin);
        annuler=layout.findViewById(R.id.annuler);
        effectuer=layout.findViewById(R.id.save);
        drawerlayout=(DrawerLayout)layout.findViewById(R.id.drawerlayout);
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
        toolbar.setTitle("Ajouter un administrateur");
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
         radio=layout.findViewById(R.id.type);



    }@Override
    protected void onResume()
    {super.onResume();
        Activity mai=this;
        effectuer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View t){
                if(!Nom.getText().toString().isEmpty() && !password.getText().toString().isEmpty()  && !username1.getText().toString().isEmpty() && !Prenom.getText().toString().isEmpty() && !numpiece.getText().toString().isEmpty() && !telephone.getText().toString().isEmpty() && !Mail.getText().toString().isEmpty() && !adresse.getText().toString().isEmpty() )
                {MySQLiteOpenHelper database = new MySQLiteOpenHelper(getApplicationContext(), null);
                SQLiteDatabase db = database.getReadableDatabase();
                Cursor ct=db.rawQuery("SELECT ID FROM ADMINISTRATEURS WHERE USERNAME=?",new String[]{username1.getText().toString()});
                if(ct.getCount()>0)
                    Toast.makeText(getApplicationContext(),"Ce mot d'utilisateur existe déjà veuillez en choisir un autre",Toast.LENGTH_LONG).show();
                   else {ct.close();
                        type = layout.findViewById(radio.getCheckedRadioButtonId());
                        int id;
                        Cursor cursor = db.rawQuery("SELECT MAX(ID) FROM ADMINISTRATEURS;", null);
                        cursor.moveToFirst();
                        id = cursor.getInt(0);
                        cursor.close();
                        id++;
                        String ex = "INSERT INTO ADMINISTRATEURS VALUES (" + id + ",'" + Nom.getText().toString() + "','" + Prenom.getText().toString() + "','" + username1.getText().toString() + "','" + password.getText().toString() + "','" + telephone.getText().toString() + "','" + type.getText().toString() + "','" + Mail.getText().toString() + "','" + adresse.getText().toString() + "','" + numpiece.getText().toString() + "');";
                        db.execSQL(ex);
                        Toast.makeText(getApplicationContext(), "Ajout effectué", Toast.LENGTH_LONG).show();
                        finish();
                        db.close();
                    }
            }
                else  Toast.makeText(getApplicationContext(),"Veuillez remplir tous les champs",Toast.LENGTH_LONG).show();}

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
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                finishAffinity();
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