package controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tentative.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.Infos_prods;
import model.MySQLiteOpenHelper;
import model.Recyclerviewapdapter;
import model.produit_adapter;


public class Produits extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
   private ArrayList<String[]> produits;
   public static int pro=0;
    DrawerLayout drawerlayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FloatingActionButton ajouter;
    NavigationView nav_view;
    ListView lv;
    produit_adapter adapter;
    SearchView recherche;
    String id1;
    String lot1;
    BottomNavigationView bottom_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produits);
        drawerlayout=(DrawerLayout)findViewById(R.id.drawerlayout_produits);
        Bundle b=getIntent().getExtras();
        NavigationView nav=(NavigationView)drawerlayout.findViewById(R.id.navigation_view);
        View vue=nav.getHeaderView(0);
        TextView nom=vue.findViewById(R.id.nom_header);
        TextView username=vue.findViewById(R.id.username_header);
        // Find the toolbar view inside the activity layout
        nom.setText(b.getString("nom")+" "+b.getString("prenom"));
        username.setText("@"+b.getString("username"));
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Produits");
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerlayout.addDrawerListener(actionBarDrawerToggle);
        MySQLiteOpenHelper bd=new MySQLiteOpenHelper(getBaseContext(),null);
        produits=bd.produits();
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
        ajouter=(FloatingActionButton)findViewById(R.id.ajouter);
        bottom_view=drawerlayout.findViewById(R.id.bottom_navigation);
        bottom_view.setOnNavigationItemSelectedListener(navlistenner);
        nav_view=drawerlayout.findViewById(R.id.navigation_view);

        nav_view.bringToFront();
        nav_view.setNavigationItemSelectedListener(this);
         lv=findViewById(R.id.list_produits);
         adapter=new produit_adapter(this,R.layout.list_items,bd.produits());
         if(bd.produits()!=null)
         lv.setAdapter(adapter);

    }


    @Override
    protected void onResume()
    {super.onResume();
        MySQLiteOpenHelper bd=new MySQLiteOpenHelper(getBaseContext(),null);
        produits=bd.produits();
        adapter=new produit_adapter(this,R.layout.list_items,produits);
        if(produits!=null)
        lv.setAdapter(adapter);
        ajouter.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View t){
               Intent intent=new Intent(getApplicationContext(),Ajout_produit.class);
               Bundle b=getIntent().getExtras();
               intent.putExtras(b);
               startActivity(intent);
           }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] prod=produits.get(position);
                id1=prod[0];
                lot1=prod[5];
                Bundle args = new Bundle();
                args.putString("id1", id1);
                args.putString("lot1", lot1);
                Infos_prods dialog=new Infos_prods();
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(),"Infos");

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.files_menu, menu);
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
                 if(pro==0)
                 {ArrayList<String[]> prods = new ArrayList<>();
                     SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                     String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY ID ASC;";
                     Cursor curseur=db.rawQuery(sql,null);
                     Log.i("Selection ", " Réussit ");
                     int n=curseur.getCount();
                     if(n<=0)
                         prods=null;
                     else
                     {int i=0;
                         curseur.moveToFirst();
                         while (!curseur.isAfterLast())
                         {
                             SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                             String t1=format1.format(Calendar.getInstance().getTime());
                             if(t1.compareTo(curseur.getString(4))<0)
                             {
                                 String[] prod=new String[9];
                                 prod[0]= curseur.getString(0);
                                 prod[1]= curseur.getString(1);
                                 prod[2]= curseur.getString(2);
                                 prod[3]= curseur.getString(3);
                                 prod[4]= curseur.getString(4);
                                 prod[5]=curseur.getString(5);
                                 prod[6]=curseur.getString(6);
                                 prod[7]=curseur.getString(7);
                                 prod[8]=curseur.getString(8);
                                 prods.add(i,prod);
                                 i++;}
                             curseur.moveToNext();
                         }

                 }
                     produits=prods;
                     adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                     lv.setAdapter(adapter);
                     



        }
                 else

                     {ArrayList<String[]> prods = new ArrayList<>();
                         SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                         String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY ID ASC;";
                         Cursor curseur=db.rawQuery(sql,null);
                         Log.i("Selection ", " Réussit ");
                         int n=curseur.getCount();
                         if(n<=0)
                             prods=null;
                         else
                         {int i=0;
                             curseur.moveToFirst();
                             while (!curseur.isAfterLast())
                             {
                                 SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                                 String t1=format1.format(Calendar.getInstance().getTime());
                                 if(t1.compareTo(curseur.getString(4))>=0)
                                 {
                                     String[] prod=new String[9];
                                     prod[0]= curseur.getString(0);
                                     prod[1]= curseur.getString(1);
                                     prod[2]= curseur.getString(2);
                                     prod[3]= curseur.getString(3);
                                     prod[4]= curseur.getString(4);
                                     prod[5]=curseur.getString(5);
                                     prod[6]=curseur.getString(6);
                                     prod[7]=curseur.getString(7);
                                     prod[8]=curseur.getString(8);
                                     prods.add(i,prod);
                                     i++;}
                                 curseur.moveToNext();
                             }

                         }
                         produits=prods;
                         adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                         lv.setAdapter(adapter);

                     }return true;
                     case R.id.tri_id_decroissant:
                         item.setChecked(true);
                         if(pro==0)
                         {ArrayList<String[]> prods = new ArrayList<>();
                             SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                             String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY ID DESC;";
                             Cursor curseur=db.rawQuery(sql,null);
                             Log.i("Selection ", " Réussit ");
                             int n=curseur.getCount();
                             if(n<=0)
                                 prods=null;
                             else
                             {int i=0;
                                 curseur.moveToFirst();
                                 while (!curseur.isAfterLast())
                                 {
                                     SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                                     String t1=format1.format(Calendar.getInstance().getTime());
                                     if(t1.compareTo(curseur.getString(4))<0)
                                     {
                                         String[] prod=new String[9];
                                         prod[0]= curseur.getString(0);
                                         prod[1]= curseur.getString(1);
                                         prod[2]= curseur.getString(2);
                                         prod[3]= curseur.getString(3);
                                         prod[4]= curseur.getString(4);
                                         prod[5]=curseur.getString(5);
                                         prod[6]=curseur.getString(6);
                                         prod[7]=curseur.getString(7);
                                         prod[8]=curseur.getString(8);
                                         prods.add(i,prod);
                                         i++;}
                                     curseur.moveToNext();
                                 }

                             }
                             produits=prods;
                             adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                             lv.setAdapter(adapter);




                         }
                         else
                         {
                             {ArrayList<String[]> prods = new ArrayList<>();
                                 SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                                 String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY ID DESC;";
                                 Cursor curseur=db.rawQuery(sql,null);
                                 Log.i("Selection ", " Réussit ");
                                 int n=curseur.getCount();
                                 if(n<=0)
                                     prods=null;
                                 else
                                 {int i=0;
                                     curseur.moveToFirst();
                                     while (!curseur.isAfterLast())
                                     {
                                         SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                                         String t1=format1.format(Calendar.getInstance().getTime());
                                         if(t1.compareTo(curseur.getString(4))>=0)
                                         {
                                             String[] prod=new String[9];
                                             prod[0]= curseur.getString(0);
                                             prod[1]= curseur.getString(1);
                                             prod[2]= curseur.getString(2);
                                             prod[3]= curseur.getString(3);
                                             prod[4]= curseur.getString(4);
                                             prod[5]=curseur.getString(5);
                                             prod[6]=curseur.getString(6);
                                             prod[7]=curseur.getString(7);
                                             prod[8]=curseur.getString(8);
                                             prods.add(i,prod);
                                             i++;}
                                         curseur.moveToNext();
                                     }

                                 }
                                 produits=prods;
                                 adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                                 lv.setAdapter(adapter);

                             }





                }return true;
            case R.id.tri_label_croissant:
                item.setChecked(true);
                if(pro==0)
                {ArrayList<String[]> prods = new ArrayList<>();
                    SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                    String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY LABEL ASC;";
                    Cursor curseur=db.rawQuery(sql,null);
                    Log.i("Selection ", " Réussit ");
                    int n=curseur.getCount();
                    if(n<=0)
                        prods=null;
                    else
                    {int i=0;
                        curseur.moveToFirst();
                        while (!curseur.isAfterLast())
                        {
                            SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                            String t1=format1.format(Calendar.getInstance().getTime());
                            if(t1.compareTo(curseur.getString(4))<0)
                            {
                                String[] prod=new String[9];
                                prod[0]= curseur.getString(0);
                                prod[1]= curseur.getString(1);
                                prod[2]= curseur.getString(2);
                                prod[3]= curseur.getString(3);
                                prod[4]= curseur.getString(4);
                                prod[5]=curseur.getString(5);
                                prod[6]=curseur.getString(6);
                                prod[7]=curseur.getString(7);
                                prod[8]=curseur.getString(8);
                                prods.add(i,prod);
                                i++;}
                            curseur.moveToNext();
                        }

                    }
                    produits=prods;
                    adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                    lv.setAdapter(adapter);




                }
                else

                {ArrayList<String[]> prods = new ArrayList<>();
                    SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                    String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY LABEL ASC;";
                    Cursor curseur=db.rawQuery(sql,null);
                    Log.i("Selection ", " Réussit ");
                    int n=curseur.getCount();
                    if(n<=0)
                        prods=null;
                    else
                    {int i=0;
                        curseur.moveToFirst();
                        while (!curseur.isAfterLast())
                        {
                            SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                            String t1=format1.format(Calendar.getInstance().getTime());
                            if(t1.compareTo(curseur.getString(4))>=0)
                            {
                                String[] prod=new String[9];
                                prod[0]= curseur.getString(0);
                                prod[1]= curseur.getString(1);
                                prod[2]= curseur.getString(2);
                                prod[3]= curseur.getString(3);
                                prod[4]= curseur.getString(4);
                                prod[5]=curseur.getString(5);
                                prod[6]=curseur.getString(6);
                                prod[7]=curseur.getString(7);
                                prod[8]=curseur.getString(8);
                                prods.add(i,prod);
                                i++;}
                            curseur.moveToNext();
                        }

                    }
                    produits=prods;
                    adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                    lv.setAdapter(adapter);

                }return true;
            case R.id.tri_label_decroissant:
                item.setChecked(true);
                if(pro==0)
                {ArrayList<String[]> prods = new ArrayList<>();
                    SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                    String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY LABEL DESC;";
                    Cursor curseur=db.rawQuery(sql,null);
                    Log.i("Selection ", " Réussit ");
                    int n=curseur.getCount();
                    if(n<=0)
                        prods=null;
                    else
                    {int i=0;
                        curseur.moveToFirst();
                        while (!curseur.isAfterLast())
                        {
                            SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                            String t1=format1.format(Calendar.getInstance().getTime());
                            if(t1.compareTo(curseur.getString(4))<0)
                            {
                                String[] prod=new String[9];
                                prod[0]= curseur.getString(0);
                                prod[1]= curseur.getString(1);
                                prod[2]= curseur.getString(2);
                                prod[3]= curseur.getString(3);
                                prod[4]= curseur.getString(4);
                                prod[5]=curseur.getString(5);
                                prod[6]=curseur.getString(6);
                                prod[7]=curseur.getString(7);
                                prod[8]=curseur.getString(8);
                                prods.add(i,prod);
                                i++;}
                            curseur.moveToNext();
                        }

                    }
                    produits=prods;
                    adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                    lv.setAdapter(adapter);




                }
                else
                {
                    {ArrayList<String[]> prods = new ArrayList<>();
                        SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                        String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY LABEL DESC;";
                        Cursor curseur=db.rawQuery(sql,null);
                        Log.i("Selection ", " Réussit ");
                        int n=curseur.getCount();
                        if(n<=0)
                            prods=null;
                        else
                        {int i=0;
                            curseur.moveToFirst();
                            while (!curseur.isAfterLast())
                            {
                                SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                                String t1=format1.format(Calendar.getInstance().getTime());
                                if(t1.compareTo(curseur.getString(4))>=0)
                                {
                                    String[] prod=new String[9];
                                    prod[0]= curseur.getString(0);
                                    prod[1]= curseur.getString(1);
                                    prod[2]= curseur.getString(2);
                                    prod[3]= curseur.getString(3);
                                    prod[4]= curseur.getString(4);
                                    prod[5]=curseur.getString(5);
                                    prod[6]=curseur.getString(6);
                                    prod[7]=curseur.getString(7);
                                    prod[8]=curseur.getString(8);
                                    prods.add(i,prod);
                                    i++;}
                                curseur.moveToNext();
                            }

                        }
                        produits=prods;
                        adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                        lv.setAdapter(adapter);

                    }





                }return true;
            case R.id.tri_prix_croissant:
                item.setChecked(true);
                if(pro==0)
                {ArrayList<String[]> prods = new ArrayList<>();
                    SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                    String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY PRIX_ACHAT ASC;";
                    Cursor curseur=db.rawQuery(sql,null);
                    Log.i("Selection ", " Réussit ");
                    int n=curseur.getCount();
                    if(n<=0)
                        prods=null;
                    else
                    {int i=0;
                        curseur.moveToFirst();
                        while (!curseur.isAfterLast())
                        {
                            SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                            String t1=format1.format(Calendar.getInstance().getTime());
                            if(t1.compareTo(curseur.getString(4))<0)
                            {
                                String[] prod=new String[9];
                                prod[0]= curseur.getString(0);
                                prod[1]= curseur.getString(1);
                                prod[2]= curseur.getString(2);
                                prod[3]= curseur.getString(3);
                                prod[4]= curseur.getString(4);
                                prod[5]=curseur.getString(5);
                                prod[6]=curseur.getString(6);
                                prod[7]=curseur.getString(7);
                                prod[8]=curseur.getString(8);
                                prods.add(i,prod);
                                i++;}
                            curseur.moveToNext();
                        }

                    }
                    produits=prods;
                    adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                    lv.setAdapter(adapter);




                }
                else

                {ArrayList<String[]> prods = new ArrayList<>();
                    SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                    String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY PRIX_ACHAT ASC;";
                    Cursor curseur=db.rawQuery(sql,null);
                    Log.i("Selection ", " Réussit ");
                    int n=curseur.getCount();
                    if(n<=0)
                        prods=null;
                    else
                    {int i=0;
                        curseur.moveToFirst();
                        while (!curseur.isAfterLast())
                        {
                            SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                            String t1=format1.format(Calendar.getInstance().getTime());
                            if(t1.compareTo(curseur.getString(4))>=0)
                            {
                                String[] prod=new String[9];
                                prod[0]= curseur.getString(0);
                                prod[1]= curseur.getString(1);
                                prod[2]= curseur.getString(2);
                                prod[3]= curseur.getString(3);
                                prod[4]= curseur.getString(4);
                                prod[5]=curseur.getString(5);
                                prod[6]=curseur.getString(6);
                                prod[7]=curseur.getString(7);
                                prod[8]=curseur.getString(8);
                                prods.add(i,prod);
                                i++;}
                            curseur.moveToNext();
                        }

                    }
                    produits=prods;
                    adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                    lv.setAdapter(adapter);

                }return true;
            case R.id.tri_prix_decroissant:
                item.setChecked(true);
                if(pro==0)
                {ArrayList<String[]> prods = new ArrayList<>();
                    SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                    String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY PRIX_ACHAT DESC;";
                    Cursor curseur=db.rawQuery(sql,null);
                    Log.i("Selection ", " Réussit ");
                    int n=curseur.getCount();
                    if(n<=0)
                        prods=null;
                    else
                    {int i=0;
                        curseur.moveToFirst();
                        while (!curseur.isAfterLast())
                        {
                            SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                            String t1=format1.format(Calendar.getInstance().getTime());
                            if(t1.compareTo(curseur.getString(4))<0)
                            {
                                String[] prod=new String[9];
                                prod[0]= curseur.getString(0);
                                prod[1]= curseur.getString(1);
                                prod[2]= curseur.getString(2);
                                prod[3]= curseur.getString(3);
                                prod[4]= curseur.getString(4);
                                prod[5]=curseur.getString(5);
                                prod[6]=curseur.getString(6);
                                prod[7]=curseur.getString(7);
                                prod[8]=curseur.getString(8);
                                prods.add(i,prod);
                                i++;}
                            curseur.moveToNext();
                        }

                    }
                    produits=prods;
                    adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                    lv.setAdapter(adapter);




                }
                else
                {
                    {ArrayList<String[]> prods = new ArrayList<>();
                        SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                        String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY PRIX_ACHAT DESC;";
                        Cursor curseur=db.rawQuery(sql,null);
                        Log.i("Selection ", " Réussit ");
                        int n=curseur.getCount();
                        if(n<=0)
                            prods=null;
                        else
                        {int i=0;
                            curseur.moveToFirst();
                            while (!curseur.isAfterLast())
                            {
                                SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                                String t1=format1.format(Calendar.getInstance().getTime());
                                if(t1.compareTo(curseur.getString(4))>=0)
                                {
                                    String[] prod=new String[9];
                                    prod[0]= curseur.getString(0);
                                    prod[1]= curseur.getString(1);
                                    prod[2]= curseur.getString(2);
                                    prod[3]= curseur.getString(3);
                                    prod[4]= curseur.getString(4);
                                    prod[5]=curseur.getString(5);
                                    prod[6]=curseur.getString(6);
                                    prod[7]=curseur.getString(7);
                                    prod[8]=curseur.getString(8);
                                    prods.add(i,prod);
                                    i++;}
                                curseur.moveToNext();
                            }

                        }
                        produits=prods;
                        adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                        lv.setAdapter(adapter);

                    }





                }return true;
            case R.id.tri_date_croissant:
                item.setChecked(true);
                if(pro==0)
                {ArrayList<String[]> prods = new ArrayList<>();
                    SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                    String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY DATEACHAT ASC;";
                    Cursor curseur=db.rawQuery(sql,null);
                    Log.i("Selection ", " Réussit ");
                    int n=curseur.getCount();
                    if(n<=0)
                        prods=null;
                    else
                    {int i=0;
                        curseur.moveToFirst();
                        while (!curseur.isAfterLast())
                        {
                            SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                            String t1=format1.format(Calendar.getInstance().getTime());
                            if(t1.compareTo(curseur.getString(4))<0)
                            {
                                String[] prod=new String[9];
                                prod[0]= curseur.getString(0);
                                prod[1]= curseur.getString(1);
                                prod[2]= curseur.getString(2);
                                prod[3]= curseur.getString(3);
                                prod[4]= curseur.getString(4);
                                prod[5]=curseur.getString(5);
                                prod[6]=curseur.getString(6);
                                prod[7]=curseur.getString(7);
                                prod[8]=curseur.getString(8);
                                prods.add(i,prod);
                                i++;}
                            curseur.moveToNext();
                        }

                    }
                    produits=prods;
                    adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                    lv.setAdapter(adapter);




                }
                else

                {ArrayList<String[]> prods = new ArrayList<>();
                    SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                    String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY DATEACHAT ASC;";
                    Cursor curseur=db.rawQuery(sql,null);
                    Log.i("Selection ", " Réussit ");
                    int n=curseur.getCount();
                    if(n<=0)
                        prods=null;
                    else
                    {int i=0;
                        curseur.moveToFirst();
                        while (!curseur.isAfterLast())
                        {
                            SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                            String t1=format1.format(Calendar.getInstance().getTime());
                            if(t1.compareTo(curseur.getString(4))>=0)
                            {
                                String[] prod=new String[9];
                                prod[0]= curseur.getString(0);
                                prod[1]= curseur.getString(1);
                                prod[2]= curseur.getString(2);
                                prod[3]= curseur.getString(3);
                                prod[4]= curseur.getString(4);
                                prod[5]=curseur.getString(5);
                                prod[6]=curseur.getString(6);
                                prod[7]=curseur.getString(7);
                                prod[8]=curseur.getString(8);
                                prods.add(i,prod);
                                i++;}
                            curseur.moveToNext();
                        }

                    }
                    produits=prods;
                    adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                    lv.setAdapter(adapter);

                }return true;
            case R.id.tri_date_decroissant:
                item.setChecked(true);
                if(pro==0)
                {ArrayList<String[]> prods = new ArrayList<>();
                    SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                    String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY DATEACHAT DESC;";
                    Cursor curseur=db.rawQuery(sql,null);
                    Log.i("Selection ", " Réussit ");
                    int n=curseur.getCount();
                    if(n<=0)
                        prods=null;
                    else
                    {int i=0;
                        curseur.moveToFirst();
                        while (!curseur.isAfterLast())
                        {
                            SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                            String t1=format1.format(Calendar.getInstance().getTime());
                            if(t1.compareTo(curseur.getString(4))<0)
                            {
                                String[] prod=new String[9];
                                prod[0]= curseur.getString(0);
                                prod[1]= curseur.getString(1);
                                prod[2]= curseur.getString(2);
                                prod[3]= curseur.getString(3);
                                prod[4]= curseur.getString(4);
                                prod[5]=curseur.getString(5);
                                prod[6]=curseur.getString(6);
                                prod[7]=curseur.getString(7);
                                prod[8]=curseur.getString(8);
                                prods.add(i,prod);
                                i++;}
                            curseur.moveToNext();
                        }

                    }
                    produits=prods;
                    adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                    lv.setAdapter(adapter);




                }
                else
                {
                    {ArrayList<String[]> prods = new ArrayList<>();
                        SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                        String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY DATEACHAT DESC;";
                        Cursor curseur=db.rawQuery(sql,null);
                        Log.i("Selection ", " Réussit ");
                        int n=curseur.getCount();
                        if(n<=0)
                            prods=null;
                        else
                        {int i=0;
                            curseur.moveToFirst();
                            while (!curseur.isAfterLast())
                            {
                                SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                                String t1=format1.format(Calendar.getInstance().getTime());
                                if(t1.compareTo(curseur.getString(4))>=0)
                                {
                                    String[] prod=new String[9];
                                    prod[0]= curseur.getString(0);
                                    prod[1]= curseur.getString(1);
                                    prod[2]= curseur.getString(2);
                                    prod[3]= curseur.getString(3);
                                    prod[4]= curseur.getString(4);
                                    prod[5]=curseur.getString(5);
                                    prod[6]=curseur.getString(6);
                                    prod[7]=curseur.getString(7);
                                    prod[8]=curseur.getString(8);
                                    prods.add(i,prod);
                                    i++;}
                                curseur.moveToNext();
                            }

                        }
                        produits=prods;
                        adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                        lv.setAdapter(adapter);

                    }





                }return true;
            case R.id.tri_dateperemp_croissant:
                item.setChecked(true);
                if(pro==0)
                {ArrayList<String[]> prods = new ArrayList<>();
                    SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                    String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY DATE ASC;";
                    Cursor curseur=db.rawQuery(sql,null);
                    Log.i("Selection ", " Réussit ");
                    int n=curseur.getCount();
                    if(n<=0)
                        prods=null;
                    else
                    {int i=0;
                        curseur.moveToFirst();
                        while (!curseur.isAfterLast())
                        {
                            SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                            String t1=format1.format(Calendar.getInstance().getTime());
                            if(t1.compareTo(curseur.getString(4))<0)
                            {
                                String[] prod=new String[9];
                                prod[0]= curseur.getString(0);
                                prod[1]= curseur.getString(1);
                                prod[2]= curseur.getString(2);
                                prod[3]= curseur.getString(3);
                                prod[4]= curseur.getString(4);
                                prod[5]=curseur.getString(5);
                                prod[6]=curseur.getString(6);
                                prod[7]=curseur.getString(7);
                                prod[8]=curseur.getString(8);
                                prods.add(i,prod);
                                i++;}
                            curseur.moveToNext();
                        }

                    }
                    produits=prods;
                    adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                    lv.setAdapter(adapter);




                }
                else

                {ArrayList<String[]> prods = new ArrayList<>();
                    SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                    String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY DATE ASC;";
                    Cursor curseur=db.rawQuery(sql,null);
                    Log.i("Selection ", " Réussit ");
                    int n=curseur.getCount();
                    if(n<=0)
                        prods=null;
                    else
                    {int i=0;
                        curseur.moveToFirst();
                        while (!curseur.isAfterLast())
                        {
                            SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                            String t1=format1.format(Calendar.getInstance().getTime());
                            if(t1.compareTo(curseur.getString(4))>=0)
                            {
                                String[] prod=new String[9];
                                prod[0]= curseur.getString(0);
                                prod[1]= curseur.getString(1);
                                prod[2]= curseur.getString(2);
                                prod[3]= curseur.getString(3);
                                prod[4]= curseur.getString(4);
                                prod[5]=curseur.getString(5);
                                prod[6]=curseur.getString(6);
                                prod[7]=curseur.getString(7);
                                prod[8]=curseur.getString(8);
                                prods.add(i,prod);
                                i++;}
                            curseur.moveToNext();
                        }

                    }
                    produits=prods;
                    adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                    lv.setAdapter(adapter);

                }return true;
            case R.id.tri_dateperemp_decroissant:
                item.setChecked(true);
                if(pro==0)
                {ArrayList<String[]> prods = new ArrayList<>();
                    SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                    String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY DATE DESC;";
                    Cursor curseur=db.rawQuery(sql,null);
                    Log.i("Selection ", " Réussit ");
                    int n=curseur.getCount();
                    if(n<=0)
                        prods=null;
                    else
                    {int i=0;
                        curseur.moveToFirst();
                        while (!curseur.isAfterLast())
                        {
                            SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                            String t1=format1.format(Calendar.getInstance().getTime());
                            if(t1.compareTo(curseur.getString(4))<0)
                            {
                                String[] prod=new String[9];
                                prod[0]= curseur.getString(0);
                                prod[1]= curseur.getString(1);
                                prod[2]= curseur.getString(2);
                                prod[3]= curseur.getString(3);
                                prod[4]= curseur.getString(4);
                                prod[5]=curseur.getString(5);
                                prod[6]=curseur.getString(6);
                                prod[7]=curseur.getString(7);
                                prod[8]=curseur.getString(8);
                                prods.add(i,prod);
                                i++;}
                            curseur.moveToNext();
                        }

                    }
                    produits=prods;
                    adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                    lv.setAdapter(adapter);




                }
                else
                {
                    {ArrayList<String[]> prods = new ArrayList<>();
                        SQLiteDatabase db=new MySQLiteOpenHelper(getBaseContext(),null).getReadableDatabase();
                        String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY DATE DESC;";
                        Cursor curseur=db.rawQuery(sql,null);
                        Log.i("Selection ", " Réussit ");
                        int n=curseur.getCount();
                        if(n<=0)
                            prods=null;
                        else
                        {int i=0;
                            curseur.moveToFirst();
                            while (!curseur.isAfterLast())
                            {
                                SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
                                String t1=format1.format(Calendar.getInstance().getTime());
                                if(t1.compareTo(curseur.getString(4))>=0)
                                {
                                    String[] prod=new String[9];
                                    prod[0]= curseur.getString(0);
                                    prod[1]= curseur.getString(1);
                                    prod[2]= curseur.getString(2);
                                    prod[3]= curseur.getString(3);
                                    prod[4]= curseur.getString(4);
                                    prod[5]=curseur.getString(5);
                                    prod[6]=curseur.getString(6);
                                    prod[7]=curseur.getString(7);
                                    prod[8]=curseur.getString(8);
                                    prods.add(i,prod);
                                    i++;}
                                curseur.moveToNext();
                            }

                        }
                        produits=prods;
                        adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                        lv.setAdapter(adapter);

                    }





                }return true;
            default:return true;

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
    private BottomNavigationView.OnNavigationItemSelectedListener navlistenner=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.perime:
                            MySQLiteOpenHelper bd=new MySQLiteOpenHelper(getBaseContext(),null);
                            produits=bd.produits_perimes();
                            adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                            lv.setAdapter(adapter);
                            pro=1;
                            break;
                            case R.id.valable:
                                MySQLiteOpenHelper bd1=new MySQLiteOpenHelper(getBaseContext(),null);
                                produits=bd1.produits();
                                adapter=new produit_adapter(getApplicationContext(),R.layout.list_items,produits);
                                lv.setAdapter(adapter);
                                pro=0;
                                break;
                                default: return false;

                    }
                    return true;
                }
            };

}