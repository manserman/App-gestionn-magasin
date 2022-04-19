package controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toast;

import com.example.tentative.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import model.MySQLiteOpenHelper;

public class Ajout_fournisseur extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    Button effectuer;
    Button annuler;
    TextInputEditText Nom;
    TextInputEditText Prenom;
    TextInputEditText telephone;
    TextInputEditText Mail;
    TextInputEditText adresse;
    TextInputEditText numpiece;
    DrawerLayout drawerlayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView nav_view;
    int type;
    int id0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type=getIntent().getExtras().getInt("type");
        setContentView(R.layout.activity_ajout_fournisseur);
        annuler=findViewById(R.id.annuler_fournisseur);
        effectuer=findViewById(R.id.effectuer_fournisseur);
        drawerlayout=(DrawerLayout)findViewById(R.id.drawerlayout_ajout_fournisseur);
        Bundle b=getIntent().getExtras();
        NavigationView nav=(NavigationView)drawerlayout.findViewById(R.id.navigation_view);
        View vue=nav.getHeaderView(0);
        TextView nom=vue.findViewById(R.id.nom_header);
        TextView username=vue.findViewById(R.id.username_header);
        // Find the toolbar view inside the activity layout
        nom.setText(b.getString("nom")+" "+b.getString("prenom"));
        username.setText("@"+b.getString("username"));
        nav_view=drawerlayout.findViewById(R.id.navigation_view);
        nav_view.setNavigationItemSelectedListener(this);
        nav_view.bringToFront();
        Toolbar toolbar = findViewById(R.id.toolbar_ajoutfournisseur);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        if(type==1)
            toolbar.setTitle("Modifier les infos d'un fournisseur");
        else  toolbar.setTitle("Ajouter un fournisseur");
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
        Nom=findViewById(R.id.nom_ajout_fournisseur);
        Prenom=findViewById(R.id.prenom_ajout_fournisseur);
        telephone=findViewById(R.id.tel_ajout_fournissseur);
        Mail=findViewById(R.id.mail_ajout_fournisseur);
        adresse=findViewById(R.id.adresses_ajout_fournisseur);
        numpiece=findViewById(R.id.adresses_ajout_fournisseur);
        if(type==1)
        {id0=getIntent().getExtras().getInt("id");
            MySQLiteOpenHelper data=new MySQLiteOpenHelper(this,null);
            SQLiteDatabase db=data.getReadableDatabase();
            Cursor cursor=db.rawQuery("SELECT NOM,PRENOM,TELEPHONE,MAIL,ADRESSE,NUM_IDENTITE FROM PROVIDER WHERE ID="+id0+";",null);
            cursor.moveToFirst();
            Nom.setText(cursor.getString(0));
            Prenom.setText(cursor.getString(1));
            telephone.setText(cursor.getString(2));
            Mail.setText(cursor.getString(3));
            adresse.setText(cursor.getString(4));
            numpiece.setText(cursor.getString(5));


        }
    }@Override
    protected void onResume()
    {super.onResume();
        Activity mai=this;
        effectuer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View t){
                if(!Nom.getText().toString().isEmpty() && !Prenom.getText().toString().isEmpty() && !Mail.getText().toString().isEmpty() && !adresse.getText().toString().isEmpty() && !telephone.getText().toString().isEmpty() && !numpiece.getText().toString().isEmpty() )
                {MySQLiteOpenHelper database = new MySQLiteOpenHelper(getApplicationContext(), null);
                SQLiteDatabase db = database.getReadableDatabase();
                if(type==0) {


                    Log.i("Juste avant", "avant");
                    db.execSQL("INSERT INTO PROVIDER VALUES((SELECT MAX(ID)+1 FROM PROVIDER),'"+Nom.getText().toString()+"','"+Prenom.getText().toString()+"','"+telephone.getText().toString()+"','"+Mail.getText().toString()+"','"+adresse.getText().toString()+"','"+numpiece.getText().toString()+"');");
                }
                else
                {int i=getIntent().getExtras().getInt("id");
                    ContentValues cv=new ContentValues();
                    cv.put("NOM",Nom.getText().toString());
                    cv.put("PRENOM",Prenom.getText().toString());
                    cv.put("TELEPHONE",telephone.getText().toString());
                    cv.put("MAIL",Mail.getText().toString());
                    cv.put("ADRESSE",adresse.getText().toString());
                    cv.put("NUM_IDENTITE",numpiece.getText().toString());
                    db.update("PROVIDER",cv,"ID=?",new String[] {String.valueOf(id0)});

                }
                db.close();
                Toast.makeText(getApplicationContext(),"Ajout effectué",Toast.LENGTH_LONG).show();finish();}
               else   Toast.makeText(getApplicationContext(),"Veuillez remplir tous les champs svp",Toast.LENGTH_LONG).show();

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
