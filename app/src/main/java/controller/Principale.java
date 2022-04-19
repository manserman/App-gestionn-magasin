package controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.tentative.R;
import com.google.android.material.navigation.NavigationView;

public class Principale extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerlayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    CardView produit;
    CardView client;
    CardView admins;
    CardView commandes;
    CardView fournisseurs;
    CardView factures;
   NavigationView nav_view;

    Bundle b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principale);
         b=getIntent().getExtras();

        drawerlayout=(DrawerLayout)findViewById(R.id.drawerlayout_principale);
        NavigationView nav=(NavigationView)drawerlayout.findViewById(R.id.navigation_view);
        View vue=nav.getHeaderView(0);
        TextView nom=vue.findViewById(R.id.nom_header);
        TextView username=vue.findViewById(R.id.username_header);
        // Find the toolbar view inside the activity layout
        nom.setText(b.getString("nom")+" "+b.getString("prenom"));
        username.setText("@"+b.getString("username"));
        nav_view=drawerlayout.findViewById(R.id.navigation_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Gérer mon stock");
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
        produit=findViewById(R.id.button_produits);
        client=findViewById(R.id.button_client);
        admins=findViewById(R.id.button_users);
        fournisseurs=findViewById(R.id.button_fournisseur);
        factures=findViewById(R.id.button_invoice);
        commandes=findViewById(R.id.button_orders);
        nav_view.bringToFront();
        nav_view.setNavigationItemSelectedListener(this);

    }
    @Override
    protected void onResume(){
        super.onResume();

        produit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View t) {
                Intent intent=new Intent(getApplicationContext(),Produits.class);
                Bundle b=getIntent().getExtras();
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Clients.class);
                Bundle b=getIntent().getExtras();
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        admins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Administrateurs.class);
                Bundle b=getIntent().getExtras();
                intent.putExtras(b);
                startActivity(intent);
            }
        });
     fournisseurs.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent=new Intent(Principale.this,Fournisseurs.class);
             Bundle b=getIntent().getExtras();
             intent.putExtras(b);
             startActivity(intent);


         }
     });
     commandes.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

            Intent intent=new Intent(getApplicationContext(),Commandes.class);
             intent.putExtras(b);
             startActivity(intent);
         }
     });
     factures.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent=new Intent(getApplicationContext(),Factures.class);
             intent.putExtras(b);
             startActivity(intent);
         }
     });
    }

    @Override
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
                intent.putExtras(b);
                startActivity(intent);
                break; case R.id.clients_menu:
            intent=new Intent(this,Clients.class);
            intent.putExtras(b);
            startActivity(intent);
            break; case R.id.fournisseurs_menu:
            intent=new Intent(this,Fournisseurs.class);
            intent.putExtras(b);
            startActivity(intent);
            break; case R.id.utilisateurs_menu:
            intent=new Intent(this,Administrateurs.class);
            intent.putExtras(b);
            startActivity(intent);
            break; case R.id. commandes_menu:
            intent=new Intent(this,Commandes.class);
            intent.putExtras(b);
            startActivity(intent);
            break; case R.id.factures_menu:
            intent=new Intent(this,Factures.class);
            intent.putExtras(b);
            startActivity(intent);
            break;
            default: return false;
        }
        drawerlayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed()
    {
    if(drawerlayout.isDrawerOpen(GravityCompat.START))
        drawerlayout.closeDrawer(GravityCompat.START);
    else
        super.onBackPressed();


    }


}