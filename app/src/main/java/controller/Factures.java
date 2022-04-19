package controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.tentative.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import model.Commandes_adapter;
import model.MySQLiteOpenHelper;
import model.fournisseurs_adapter;
import model.infos_commande;
import model.facures_adapter;
import model.infos_factures;

public class Factures extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerlayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FloatingActionButton ajouter;
    NavigationView nav_view;
    ListView lv;
    facures_adapter adapter;
    ArrayList<String[]> admins;
    SearchView recherche;
    LinearLayoutCompat linear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factures);
        linear=findViewById(R.id.layout_facture);
        drawerlayout=(DrawerLayout)linear.findViewById(R.id.drawerlayout);

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
        toolbar.setTitle("Factures");
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerlayout.addDrawerListener(actionBarDrawerToggle);
        MySQLiteOpenHelper bd=new MySQLiteOpenHelper(getBaseContext(),null);
        admins=bd.admins();
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
        ajouter=(FloatingActionButton)findViewById(R.id.ajouter);
        nav_view=drawerlayout.findViewById(R.id.navigation_view);
        nav_view.setNavigationItemSelectedListener(this);
        nav_view.bringToFront();
        lv=findViewById(R.id.listview);
        if(bd.facture()!=null)
        {adapter=new facures_adapter(this,R.layout.list_items,bd.facture());
            lv.setAdapter(adapter);}

    }


    @Override
    protected void onResume()
    {super.onResume();
        MySQLiteOpenHelper bd=new MySQLiteOpenHelper(getBaseContext(),null);
        admins=bd.facture();
        if(admins!=null)
        {adapter=new facures_adapter(this,R.layout.list_items,admins);
            lv.setAdapter(adapter);}
        ajouter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View t){
                String type1=new String();
                Cursor cursor=bd.getReadableDatabase().rawQuery("SELECT CATEGORIE FROM ADMINISTRATEURS WHERE USERNAME=?",new String[]{getIntent().getExtras().getString("username")});
                cursor.moveToFirst();
                type1=cursor.getString(0);
                cursor.close();

                if(bd.produits()!=null)
                {
                    if(bd.fournisseurs()!=null)
                    {Intent intent=new Intent(getApplicationContext(),Ajout_factures.class);
                intent.putExtras(getIntent().getExtras());
                startActivity(intent);}
                else
                    {
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Factures.this);;
                        dlgAlert.setMessage("Votre liste de fournisseurs est vide. Veuillez ajouter au moins un fournisseur svp").setTitle("Aucun fournisseur dans la base").show();
                    }
                    }

            else
                {
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Factures.this);;
                    dlgAlert.setMessage("Votre stock vide. Veuillez ajouter au moins un produit svp").setTitle("Aucun produit dans la base").show();

                }}


        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] prod=admins.get(position);
                String  id1=prod[0];
                Bundle args = new Bundle();
                args.putString("id1", id1);
                infos_factures dialog=new infos_factures();
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(),"Infos");

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_facture_commande, menu);
        MenuItem search=menu.findItem(R.id.recherche);
        recherche=(SearchView)search.getActionView();
        recherche.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {if(actionBarDrawerToggle.onOptionsItemSelected(item))
    {return true;}
    else
        switch (item.getItemId())
        { case R.id.recherche:
            return true;
            case R.id.tri_id_croissant:
                item.setChecked(true);
                ArrayList<String[]>prods=new ArrayList<>();
                String sql= "select ID,IDPROVIDER,PRIXTOTAL,PAYEE,IDADMIN,DATE from FACTURE ORDER BY ID ASC;";
                Cursor cursor=new  MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase().rawQuery(sql,null);
                int n=cursor.getCount();
                if(n<=0)
                    prods=null;
                else
                {int i=0;
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast())
                    {String[] prod=new String[6];
                        prod[0]= cursor.getString(0);
                        prod[1]= cursor.getString(1);
                        prod[2]= cursor.getString(2);
                        prod[3]= cursor.getString(3);
                        prod[4]= cursor.getString(4);
                        prod[5]= cursor.getString(5);
                        prods.add(i,prod);
                        i++;
                        cursor.moveToNext();
                    }

                    cursor.close();

                    admins=prods;
                }
                adapter=new facures_adapter(this,R.layout.list_items,admins);
                lv.setAdapter(adapter);
                return true;
            case R.id.tri_id_decroissant:
                item.setChecked(true);
                prods=new ArrayList<>();
                 sql= "select ID,IDPROVIDER,PRIXTOTAL,PAYEE,IDADMIN,DATE from FACTURE ORDER BY ID DESC;";
                 cursor=new  MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase().rawQuery(sql,null);
                 n=cursor.getCount();
                if(n<=0)
                    prods=null;
                else
                {int i=0;
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast())
                    {String[] prod=new String[6];
                        prod[0]= cursor.getString(0);
                        prod[1]= cursor.getString(1);
                        prod[2]= cursor.getString(2);
                        prod[3]= cursor.getString(3);
                        prod[4]= cursor.getString(4);
                        prod[5]= cursor.getString(5);
                        prods.add(i,prod);
                        i++;
                        cursor.moveToNext();
                    }

                    cursor.close();

                    admins=prods;
                }
                adapter=new facures_adapter(this,R.layout.list_items,admins);
                lv.setAdapter(adapter);
                return true;
            case R.id.tri_prix_croissant:
                item.setChecked(true);
                prods=new ArrayList<>();
                sql= "select ID,IDPROVIDER,PRIXTOTAL,PAYEE,IDADMIN,DATE from FACTURE ORDER BY PRIXTOTAL ASC;";
                cursor=new  MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase().rawQuery(sql,null);
                n=cursor.getCount();
                if(n<=0)
                    prods=null;
                else
                {int i=0;
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast())
                    {String[] prod=new String[6];
                        prod[0]= cursor.getString(0);
                        prod[1]= cursor.getString(1);
                        prod[2]= cursor.getString(2);
                        prod[3]= cursor.getString(3);
                        prod[4]= cursor.getString(4);
                        prod[5]= cursor.getString(5);
                        prods.add(i,prod);
                        i++;
                        cursor.moveToNext();
                    }

                    cursor.close();

                    admins=prods;
                }
                adapter=new facures_adapter(this,R.layout.list_items,admins);
                lv.setAdapter(adapter);
                return true;
            case R.id.tri_prix_decroissant:
                item.setChecked(true);
                prods=new ArrayList<>();
                sql= "select ID,IDPROVIDER,PRIXTOTAL,PAYEE,IDADMIN,DATE from FACTURE ORDER BY PRIXTOTAL DESC;";
                cursor=new  MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase().rawQuery(sql,null);
                n=cursor.getCount();
                if(n<=0)
                    prods=null;
                else
                {int i=0;
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast())
                    {String[] prod=new String[6];
                        prod[0]= cursor.getString(0);
                        prod[1]= cursor.getString(1);
                        prod[2]= cursor.getString(2);
                        prod[3]= cursor.getString(3);
                        prod[4]= cursor.getString(4);
                        prod[5]= cursor.getString(5);
                        prods.add(i,prod);
                        i++;
                        cursor.moveToNext();
                    }

                    cursor.close();

                    admins=prods;
                }
                adapter=new facures_adapter(this,R.layout.list_items,admins);
                lv.setAdapter(adapter);
                return true;
            case R.id.tri_date_croissant:
                item.setChecked(true);
                prods=new ArrayList<>();
                sql= "select ID,IDPROVIDER,PRIXTOTAL,PAYEE,IDADMIN,DATE from FACTURE ORDER BY DATE ASC;";
                cursor=new  MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase().rawQuery(sql,null);
                n=cursor.getCount();
                if(n<=0)
                    prods=null;
                else
                {int i=0;
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast())
                    {String[] prod=new String[6];
                        prod[0]= cursor.getString(0);
                        prod[1]= cursor.getString(1);
                        prod[2]= cursor.getString(2);
                        prod[3]= cursor.getString(3);
                        prod[4]= cursor.getString(4);
                        prod[5]= cursor.getString(5);
                        prods.add(i,prod);
                        i++;
                        cursor.moveToNext();
                    }

                    cursor.close();

                    admins=prods;
                }
                adapter=new facures_adapter(this,R.layout.list_items,admins);
                lv.setAdapter(adapter);
                return true;
            case R.id.tri_date_decroissant:
                item.setChecked(true);
                prods=new ArrayList<>();
                sql= "select ID,IDPROVIDER,PRIXTOTAL,PAYEE,IDADMIN,DATE from FACTURE ORDER BY DATE DESC;";
                cursor=new  MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase().rawQuery(sql,null);
                n=cursor.getCount();
                if(n<=0)
                    prods=null;
                else
                {int i=0;
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast())
                    {String[] prod=new String[6];
                        prod[0]= cursor.getString(0);
                        prod[1]= cursor.getString(1);
                        prod[2]= cursor.getString(2);
                        prod[3]= cursor.getString(3);
                        prod[4]= cursor.getString(4);
                        prod[5]= cursor.getString(5);
                        prods.add(i,prod);
                        i++;
                        cursor.moveToNext();
                    }

                    cursor.close();

                    admins=prods;
                }
                adapter=new facures_adapter(this,R.layout.list_items,admins);
                lv.setAdapter(adapter);
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
    @Override
    public void onBackPressed()
    {
        if(drawerlayout.isDrawerOpen(GravityCompat.START))
            drawerlayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();


    }

}



