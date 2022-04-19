package controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tentative.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import model.Comande_eff_adapter;
import model.MySQLiteOpenHelper;
import model.Produit_spinneradapter;

public class Ajout_factures extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<String[]> produits;
    private RecyclerView recyclerView;
    DrawerLayout drawerlayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView nav_view;
    Spinner prod;
    Spinner clients;
    String id1;
    String lot1;
    Button effect;
    Produit_spinneradapter adapter;
    Produit_spinneradapter adapter2;
    LinearLayoutCompat linear;
    TextView total;
    EditText qte;
    LinearLayout lay;
    Button choix_prod;
    ListView lv;
    ArrayList<String[]> prods=new ArrayList<String[]>();
    ArrayList<String[]> clienteles=new ArrayList<String[]>();
    Comande_eff_adapter adapter3;
    Integer j=0;
    Button effetuer;
    Button annuler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_factures);
        linear=findViewById(R.id.layout_ajout_facture);
        drawerlayout=linear.findViewById(R.id.drawerlayout);
        lay=linear.findViewById(R.id.layout_qte);
        choix_prod=linear.findViewById(R.id.ajout);
        Bundle b=getIntent().getExtras();
        NavigationView nav=(NavigationView)drawerlayout.findViewById(R.id.navigation_view);
        View vue=nav.getHeaderView(0);
        TextView nom=vue.findViewById(R.id.nom_header);
        TextView username=vue.findViewById(R.id.username_header);
        // Find the toolbar view inside the activity layout
        nom.setText(b.getString("nom")+" "+b.getString("prenom"));
        username.setText("@"+b.getString("username"));
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Ajouter une Facture");
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerlayout.addDrawerListener(actionBarDrawerToggle);
        MySQLiteOpenHelper bd=new MySQLiteOpenHelper(getBaseContext(),null);
        produits=bd.produits();
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
        nav_view=drawerlayout.findViewById(R.id.navigation_view);
        nav_view.bringToFront();
        effect=linear.findViewById(R.id.effect);
        nav_view.setNavigationItemSelectedListener(this);
        prod=linear.findViewById(R.id.spinner_produis);
        clients=linear.findViewById(R.id.spinner_clients);
        adapter=new Produit_spinneradapter(this,R.layout.list_produit,bd.produits(),1);
        clienteles=bd.fournisseurs();
        adapter2=new Produit_spinneradapter(this,R.layout.list_produit,clienteles,0);
        if(clienteles!=null)
        clients.setAdapter(adapter2);
        if(bd.produits()!=null)
        prod.setAdapter(adapter);
        lay.setVisibility(View.GONE);
        lv=linear.findViewById(R.id.lv);
        qte=linear.findViewById(R.id.qte);
        total=linear.findViewById(R.id.total);
        effetuer=linear.findViewById(R.id.effectuer_);
        annuler=linear.findViewById(R.id.annuler);
        effetuer.setVisibility(View.GONE);



    }


    @Override
    protected void onResume()
    {super.onResume();
        MySQLiteOpenHelper bd=new MySQLiteOpenHelper(getBaseContext(),null);
        produits=bd.produits();
        if(produits!=null)
        {choix_prod.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lay.setVisibility(View.VISIBLE);
            }
        });
        effect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clienteles.size()>0)
                {lay.setVisibility(View.GONE);
                int pos=prod.getSelectedItemPosition();
                if(!qte.getText().toString().isEmpty())
                {
                String[] infos=new String[7];
                    infos[0]=j.toString();
                    j++;
                    infos[1]=produits.get(pos)[1];
                    infos[2]=qte.getText().toString();
                    infos[3]=produits.get(pos)[8];
                    Integer total1=Integer.valueOf(infos[2])*Integer.valueOf(infos[3]);
                    infos[4]=total1.toString();
                    infos[5]=produits.get(pos)[0];
                    infos[6]=produits.get(pos)[5];
                    prods.add(infos);
                    produits.remove(pos);
                    adapter=new Produit_spinneradapter(getApplicationContext(),R.layout.list_produit,produits,1);
                    prod.setAdapter(adapter);
                    adapter3=new Comande_eff_adapter(getApplicationContext(),R.layout.produit_liste,prods);
                    lv.setAdapter(adapter3);
                    Integer tot1=0;
                    for (String[] n : prods) {
                        tot1 = tot1 + Integer.valueOf(n[4]);
                    }
                    total.setText(tot1.toString());
                    effetuer.setVisibility(View.VISIBLE);

                }
                else
                    Toast.makeText(getApplicationContext(),"Veuillez saisir une quantité svp",Toast.LENGTH_LONG).show();}
                else
                {
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getApplicationContext());
                    dlgAlert.setMessage("Veuillez ajouter au moins un fournisseur svp").setTitle("Aucun fournisseur dans la base").show();
                }




        }});
        effetuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=new String();
                int pos1=clients.getSelectedItemPosition();
                SQLiteDatabase database=bd.getWritableDatabase();
                Cursor cursor=database.rawQuery("SELECT * FROM FACTURE;",null);
                if(cursor.getCount()==0)
                {
                    Cursor curs=database.rawQuery("SELECT ID FROM ADMINISTRATEURS WHERE USERNAME=?",new String[]{getIntent().getExtras().getString("username")});
                    curs.moveToFirst();
                    str="INSERT INTO FACTURE VALUES ("+0+","+clienteles.get(pos1)[0]+","+ curs.getString(0)+",(DATE()),"+total.getText().toString()+",'false');";
                    database.execSQL(str);
                }
                else
                { Cursor curs=database.rawQuery("SELECT ID FROM ADMINISTRATEURS WHERE USERNAME=?",new String[]{getIntent().getExtras().getString("username")});
                    curs.moveToFirst();
                    Cursor curs1=database.rawQuery("SELECT MAX (ID) FROM FACTURE;",null);
                    curs1.moveToFirst();
                    int id=Integer.valueOf(curs1.getString(0))+1;
                    str="INSERT INTO FACTURE VALUES ("+id+","+clienteles.get(pos1)[0]+","+curs.getString(0)+",(DATE()),"+total.getText().toString()+",'false');";
                    database.execSQL(str);
                    }


                for (String[] n : prods) {
                    ContentValues cv=new ContentValues();
                    Cursor curs1=database.rawQuery("SELECT MAX (ID) FROM FACTURE;",null);
                    curs1.moveToFirst();
                    String ex="INSERT INTO PROVIDE VALUES ("+curs1.getString(0)+","+n[5]+","+n[6]+",DATE(),"+n[2]+","+n[3]+");";
                    database.execSQL(ex);
                    curs1.close();


                }
                Toast.makeText(getApplicationContext(),"Ajout effectué",Toast.LENGTH_LONG).show();finish();
                finish();
            }
        });




    }
    else {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getApplicationContext());
            dlgAlert.setMessage("Veuillez ajouter au moins un produit svp").setTitle("Aucun produit dans la base").show();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {if(actionBarDrawerToggle.onOptionsItemSelected(item))
    {return true;}
    else
        switch (item.getItemId())
        { case R.id.recherche:
            return true;
            default:return false;


        }


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

    public void onBackPressed()
    {
        if(drawerlayout.isDrawerOpen(GravityCompat.START))
            drawerlayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();


    }
}