package controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aldoapps.autoformatedittext.AutoFormatEditText;
import com.example.tentative.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Date;

import model.MySQLiteOpenHelper;

public class Ajout_produit extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Button effectuer;
    Button annuler;
    AutoFormatEditText lot;
    TextInputEditText date;
    TextInputEditText dateachat;
    AutoFormatEditText prix_a;
    AutoFormatEditText prix_v;
    AutoFormatEditText qte;
    TextInputEditText label;
    TextInputEditText categorie;
    DrawerLayout drawerlayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_produit);
        annuler=findViewById(R.id.annuler);
        effectuer=findViewById(R.id.effectuer);
        drawerlayout=(DrawerLayout)findViewById(R.id.drawerlayout_ajout_prod);
        Bundle b=getIntent().getExtras();
        NavigationView nav=(NavigationView)drawerlayout.findViewById(R.id.navigation_view);
        View vue=nav.getHeaderView(0);
        TextView nom=vue.findViewById(R.id.nom_header);
        TextView username=vue.findViewById(R.id.username_header);
        // Find the toolbar view inside the activity layout
        nom.setText(b.getString("nom")+" "+b.getString("prenom"));
        username.setText("@"+b.getString("username"));
        Toolbar toolbar = findViewById(R.id.toolbar_ajoutprod);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Ajouter un produit");
        setSupportActionBar(toolbar);
        nav_view=drawerlayout.findViewById(R.id.navigation_view);
        nav_view.setNavigationItemSelectedListener(this);
        nav_view.bringToFront();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
        prix_a=findViewById(R.id.prix_achat_produit);
        prix_v=findViewById(R.id.prix_vente_produit);
        dateachat=findViewById(R.id.dateachat_produit);
        label=findViewById(R.id.label);
        qte=findViewById(R.id.quantite_produit);
       categorie=findViewById(R.id.categorie_produit);
       lot=findViewById(R.id.lot);
       date=findViewById(R.id.date);

    }@Override
    protected void onResume()
    {super.onResume();
        Activity mai=this;
    effectuer.setOnClickListener(new View.OnClickListener(){
       @Override
       public void onClick(View t){
           if(!prix_a.getText().toString().isEmpty() && !prix_v.getText().toString().isEmpty() && !dateachat.getText().toString().isEmpty() && !label.getText().toString().isEmpty() && !lot.getText().toString().isEmpty() &&!date.getText().toString().isEmpty() && !categorie.getText().toString().isEmpty() && !qte.getText().toString().isEmpty()) {
               int id;
               MySQLiteOpenHelper database = new MySQLiteOpenHelper(getApplicationContext(), null);
               SQLiteDatabase db=database.getReadableDatabase();
               Log.i("Juste avant", "avant0");
               String lab = label.getText().toString();
               Cursor cursor =db.rawQuery("SELECT ID FROM PRODUCTS WHERE LABEL=?", new String[]{lab});
               Log.i("Juste avant", "avant1");
               if (cursor.getCount()==0) {
                   cursor = db.rawQuery("SELECT MAX (ID) FROM PRODUCTS ;", null);
                   if (cursor == null)
                       id = 1;
                   else {
                       cursor.moveToFirst();
                       id = Integer.valueOf(cursor.getString(0)) + 1;
                   }
               } else {
                   cursor.moveToFirst();
                   id = Integer.valueOf(cursor.getString(0));
               }
               cursor.close();
               Log.i("Juste avant", "avant");
               database.ajout_produit(id, Integer.valueOf(lot.getText().toString()), label.getText().toString(), categorie.getText().toString(), Integer.valueOf(qte.getText().toString()), Double.valueOf(prix_a.getText().toString()), Double.valueOf(prix_v.getText().toString()), date.getText().toString(), dateachat.getText().toString());
                 db.close();
               Toast.makeText(getApplicationContext(), "Ajout effectué", Toast.LENGTH_LONG).show();
               finish();
           }
           else Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();

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