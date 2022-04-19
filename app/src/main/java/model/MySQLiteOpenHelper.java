package model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MySQLiteOpenHelper extends SQLiteOpenHelper {// Nom de la base de données SQLite
private static final String DATABASE_NAME = "GESTION.db"; // Nom de la table
    // RequeteSQL pour la création de la table private static final


             //Constructeur de la sous classe MySQLiteOpenHelper qui hérite de SQLiteOpenHelper
             public MySQLiteOpenHelper(Context context , SQLiteDatabase.CursorFactory factory)
             { super(context, DATABASE_NAME, factory, 1); // Factory est généralement null
                 }
                 @Override
                 public void onCreate(SQLiteDatabase db)
                 {Log.i("ONCREATE", " Invoqué ");
                     String CREATE_TABLE1 = "CREATE TABLE ADMINISTRATEURS ( ID INTEGER PRIMARY KEY AUTOINCREMENT,NOM TEXT,PRENOM TEXT,USERNAME TEXT NOT NULL UNIQUE,PASSWORD TEXT,TELEPHONE TEXT UNIQUE,CATEGORIE TEXT,MAIL TEXT UNIQUE, ADRESSE TEXT,NUM_PIECE TEXT);";
                     String CREATE_TABLE3="CREATE TABLE PRODUCTS (ID INTEGER NOT NULL  ,LOT INTEGER,LABEL TEXT,CATEGORIE TEXT,QUANTITE INTEGER,PRIX_VENTE REAL,PRIX_ACHAT REAL,DATE DATE,DATEACHAT DATE ,PRIMARY KEY(ID,LOT,DATEACHAT));";
                     String CREATE_TABLE4="CREATE TABLE PROVIDER (ID INTEGER PRIMARY KEY AUTOINCREMENT,NOM TEXT,PRENOM TEXT,TELEPHONE TEXT NOT NULL UNIQUE,ADRESSE TEXT,MAIL TEXT NOT NULL,NUM_IDENTITE TEXT NOT NULL UNIQUE);";
                     String CREATE_TABLE5="CREATE TABLE PROVIDE (IDFACTURE INTEGER,IDPRODUCT INTEGER,LOT INTEGER,DATE DATE,QUANTITE INTEGER,PRIXU REAL,FOREIGN KEY (IDFACTURE) REFERENCES FACTURE(ID),FOREIGN KEY (IDPRODUCT) REFERENCES PRODUCTS(ID),FOREIGN KEY (LOT) REFERENCES PRODUCTS (LOT),PRIMARY KEY (IDFACTURE,IDPRODUCT,DATE,QUANTITE));";
                     String CREATE_TABLE6="CREATE TABLE CLIENT (ID INTEGER PRIMARY KEY AUTOINCREMENT,NOM TEXT,PRENOM TEXT,TELEPHONE TEXT NOT NULL UNIQUE,MAIL TEXT,ADRESSE TEXT NOT NULL );";
                     String CREATE_TABLE7="CREATE TABLE COMMANDE (ID INTEGER PRIMARY KEY AUTOINCREMENT,IDCLIENT INTEGER,IDADMIN INTEGER,DATE DATE,PRIXTOTAL REAL,PAYEE BOOLEAN,FOREIGN KEY (IDCLIENT) REFERENCES CLIENT(ID),FOREIGN KEY (IDADMIN) REFERENCES ADMINISTRATUERS(ID));";
                     String CREATE_TABLE9="CREATE TABLE FACTURE (ID INTEGER PRIMARY KEY AUTOINCREMENT,IDPROVIDER INTEGER,IDADMIN INTEGER,DATE DATE,PRIXTOTAL REAL,PAYEE BOOLEAN,FOREIGN KEY (IDPROVIDER) REFERENCES PROVIDER(ID),FOREIGN KEY (IDADMIN) REFERENCES ADMINISTRATUERS(ID));";
                     String CREATE_TABLE8="CREATE TABLE LIGNECOMMANDE (IDCOMMANDE INTEGER,IDPRODUCT INTEGER,LOT INTEGER,QUANTITE INTEGER,PRIX REAL,FOREIGN KEY (IDPRODUCT) REFERENCES PRODUCTS (ID),FOREIGN KEY (LOT) REFERENCES PRODUCTS (LOT),FOREIGN KEY (IDCOMMANDE) REFERENCES COMMANDE(ID), PRIMARY KEY(IDCOMMANDE,IDPRODUCT));";
                     db.execSQL(CREATE_TABLE1);
                     Log.i("DATABASE", " TABLE1:créé ");
                     db.execSQL(CREATE_TABLE3);
                     Log.i("DATABASE", " TABLE2:créé ");
                     db.execSQL(CREATE_TABLE4);
                     Log.i("DATABASE", " TABLE4:créé ");
                     db.execSQL(CREATE_TABLE5);
                     Log.i("DATABASE", " TABLE5:créé ");
                     db.execSQL(CREATE_TABLE6);
                     Log.i("DATABASE", " TABLE6:créé ");
                     db.execSQL(CREATE_TABLE7);
                     Log.i("DATABASE", " TABLE7:créé ");
                     db.execSQL(CREATE_TABLE8);
                     db.execSQL(CREATE_TABLE9);
                     Log.i("DATABASE", " TABLE1:créé ");

                     String strl="INSERT INTO ADMINISTRATEURS VALUES(1,'Principal','Administrateur','admin','admin1','0792105518','PRINCIPAL','mansourh923@gmail.com','Biskra','10PC4524');";
                     db.execSQL(strl);
                     Log.i("Insert ", " Réussit ");
                     strl="INSERT INTO PRODUCTS VALUES(1,1,'Nescafé','CAFE',30,350,300,'2021-12-31','2021-05-29');";
                     db.execSQL(strl);
                     strl="INSERT INTO PRODUCTS VALUES(2,1,'Pringles','Chips',30,400,350,'2021-12-31','2021-05-29');";
                     db.execSQL(strl);
                     strl="INSERT INTO PRODUCTS VALUES(3,1,'coca cola ','Soda',30,120,90,'2021-12-31','2021-05-29');";
                     db.execSQL(strl);

                     strl="INSERT INTO PRODUCTS VALUES(4,1,'Pepsi ','Soda',30,120,90,'2021-12-31','2021-05-29');";
                     db.execSQL(strl);

                     strl="INSERT INTO PRODUCTS VALUES(5,1,'Sprite ','Soda',30,120,90,'2021-12-31','2021-05-29');";
                     db.execSQL(strl);

                     strl="INSERT INTO PRODUCTS VALUES(6,1,'Signal ','Dentifrice',30,120,90,'2021-12-31','2021-05-29');";
                     db.execSQL(strl);

                     strl="INSERT INTO PRODUCTS VALUES(7,1,'Sensodine ','Dentifrice',30,120,90,'2021-12-31','2021-05-29');";
                     db.execSQL(strl);

                     strl="INSERT INTO PRODUCTS VALUES(8,1,'Calvé ','Mayonnaise',30,120,90,'2021-12-31','2021-05-29');";
                     db.execSQL(strl);

                     strl="INSERT INTO PRODUCTS VALUES(10,1,'Loya ','Lait en poudre',30,120,90,'2021-05-31','2021-05-29');";
                     db.execSQL(strl);
                     strl="INSERT INTO PRODUCTS VALUES(11,1,'Nido ','Lait en poudre',30,120,90,'2021-05-31','2021-05-29');";
                     db.execSQL(strl);
                     strl="INSERT INTO PRODUCTS VALUES(12,1,'Dove ','savon',30,120,90,'2021-12-31','2021-05-29');";
                     db.execSQL(strl);
                    strl="INSERT INTO CLIENT VALUES(1,'HABIBOU HAMANI','Mansour','0792188518','mansourh923@gmail.com','Biskra');";
                     db.execSQL(strl);
                     db.execSQL("INSERT INTO CLIENT VALUES(2,'Dounia ','Chebana','0792188517','mohamadoumansourgmail.com','Biskra');");


                     strl="INSERT INTO PROVIDER VALUES(1,'HAMANI','Mansour','0792188518','mansourh923@gmail.com','Biskra','123');";
                     db.execSQL(strl);

                     strl="INSERT INTO PROVIDER VALUES(2,'CHEBANA ','Dounia','0792188519','moansourh923@gmail.com','Biskra','23');";
                     db.execSQL(strl);
                 }

                 public void ajout_admin (int id,String nom,String prenom,String username,String password,String CATEGORIE,String telephone)
                 {

                     String strl="insert into ADMINISTRATEURS values ("+id+",'"+nom+"','"+prenom+"','"+username+"','"+ password+"','"+ telephone+"','"+ CATEGORIE +"');";
                     this.getWritableDatabase().execSQL(strl);
                 }

                 @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL ("DROP TABLE IF EXISTS "+"PRODUCT");
        onCreate(db);
    }
    public String[] infos(String username)

    {String[] infos=new String[3];
        String strl="SELECT NOM, PRENOM,PASSWORD FROM ADMINISTRATEURS WHERE USERNAME = ?;";
        Cursor curseur= this.getReadableDatabase().rawQuery(strl,new String[]{username});
        int n=curseur.getCount();
        if(n==0)
            return null;
        else
        {curseur.moveToFirst();
            infos[0]=curseur.getString(0);
            infos[1]=curseur.getString(1);
            infos[2]=curseur.getString(2);
            curseur.close();
            return infos;

        }
    }
    public ArrayList<String[]> produits()
    { ArrayList<String[]> produits=new ArrayList();
        String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY LABEL;";
        Cursor curseur=this.getReadableDatabase().rawQuery(sql,null);
        Log.i("Selection ", " Réussit ");
        int n=curseur.getCount();
        if(n<=0)
            return null;
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
                produits.add(i,prod);
                i++;}
                curseur.moveToNext();
            }

            curseur.close();
            return produits;
        }

    }
  public ArrayList<String[]> produits_perimes()
  { ArrayList<String[]> produits=new ArrayList();
  String sql= "select ID,LABEL ,PRIX_VENTE,CATEGORIE,DATE,LOT,DATEACHAT,QUANTITE,PRIX_ACHAT from products WHERE QUANTITE>0 ORDER BY LABEL;";
Cursor curseur=this.getReadableDatabase().rawQuery(sql,null);
      Log.i("Selection ", " Réussit ");
      int n=curseur.getCount();
      if(n<=0)
          return null;
      else
      {int i=0;
          curseur.moveToFirst();
          while (!curseur.isAfterLast())
          {SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd");
              String t1=  format1.format(Calendar.getInstance().getTime());
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
                  produits.add(i,prod);
                  i++;}
              curseur.moveToNext();
      }
        
          curseur.close();
          return produits;
      }

  }
    public ArrayList<String[]> admins()
    {String produit[]=new String[5];
        ArrayList<String[]> produits=new ArrayList();
        String sql= "select ID,NOM,PRENOM,TELEPHONE,CATEGORIE from ADMINISTRATEURS ORDER BY ID;";
        Cursor curseur=this.getReadableDatabase().rawQuery(sql,null);
        Log.i("Selection ", " Réussit ");
        int n=curseur.getCount();
        if(n<=0)
            return null;
        else
        {int i=0;
            curseur.moveToFirst();
            while (!curseur.isAfterLast())
            {String[] prod=new String[5];
                prod[0]= curseur.getString(0);
                prod[1]= curseur.getString(1);
                prod[2]= curseur.getString(2);
                prod[3]= curseur.getString(3);
                prod[4]= curseur.getString(4);
                produits.add(i,prod);
                i++;
                curseur.moveToNext();
            }

            curseur.close();
            return produits;
        }

    }
    public ArrayList<String[]> clients()
    {
        ArrayList<String[]> produits=new ArrayList();
        String sql= "select ID,NOM,PRENOM,TELEPHONE,ADRESSE,MAIL from CLIENT ORDER BY ID;";
        Cursor curseur=this.getReadableDatabase().rawQuery(sql,null);
        Log.i("Selection ", " Réussit ");
        int n=curseur.getCount();
        if(n<=0)
            return null;
        else
        {int i=0;
            curseur.moveToFirst();
            while (!curseur.isAfterLast())
            {String[] prod=new String[6];
                prod[0]= curseur.getString(0);
                prod[1]= curseur.getString(1);
                prod[2]= curseur.getString(2);
                prod[3]= curseur.getString(3);
                prod[4]= curseur.getString(4);
                prod[5]= curseur.getString(5);
                produits.add(i,prod);
                i++;
                curseur.moveToNext();
            }

            curseur.close();
            return produits;
        }

    }
    public ArrayList<String[]> fournisseurs()
    {
        ArrayList<String[]> produits=new ArrayList();
        String sql= "select ID,NOM,PRENOM,TELEPHONE,ADRESSE,MAIL,NUM_IDENTITE from PROVIDER ORDER BY ID;";
        Cursor curseur=this.getReadableDatabase().rawQuery(sql,null);
        Log.i("Selection ", " Réussit ");
        int n=curseur.getCount();
        if(n<=0)
            return null;
        else
        {int i=0;
            curseur.moveToFirst();
            while (!curseur.isAfterLast())
            {String[] prod=new String[7];
                prod[0]= curseur.getString(0);
                prod[1]= curseur.getString(1);
                prod[2]= curseur.getString(2);
                prod[3]= curseur.getString(3);
                prod[4]= curseur.getString(4);
                prod[5]= curseur.getString(5);
                prod[6]=curseur.getString(6);
                produits.add(i,prod);
                i++;
                curseur.moveToNext();
            }

            curseur.close();
            return produits;
        }

    }

    public ArrayList<String[]> commande()
    {
        ArrayList<String[]> produits=new ArrayList();
        String sql= "select ID,IDCLIENT,PRIXTOTAL,PAYEE,IDADMIN,DATE from COMMANDE ORDER BY ID;";
        Cursor curseur=this.getReadableDatabase().rawQuery(sql,null);
        Log.i("Selection ", " Réussit ");
        int n=curseur.getCount();
        if(n<=0)
            return null;
        else
        {int i=0;
            curseur.moveToFirst();
            while (!curseur.isAfterLast())
            {String[] prod=new String[6];
                prod[0]= curseur.getString(0);
                prod[1]= curseur.getString(1);
                prod[2]= curseur.getString(2);
                prod[3]= curseur.getString(3);
                prod[4]= curseur.getString(4);
                prod[5]= curseur.getString(5);
                produits.add(i,prod);
                i++;
                curseur.moveToNext();
            }

            curseur.close();
            return produits;
        }

    }

    public ArrayList<String[]> facture()
    {
        ArrayList<String[]> produits=new ArrayList();
        String sql= "select ID,IDPROVIDER,PRIXTOTAL,PAYEE,IDADMIN,DATE from FACTURE ORDER BY ID;";
        Cursor curseur=this.getReadableDatabase().rawQuery(sql,null);
        Log.i("Selection ", " Réussit ");
        int n=curseur.getCount();
        if(n<=0)
            return null;
        else
        {int i=0;
            curseur.moveToFirst();
            while (!curseur.isAfterLast())
            {String[] prod=new String[6];
                prod[0]= curseur.getString(0);
                prod[1]= curseur.getString(1);
                prod[2]= curseur.getString(2);
                prod[3]= curseur.getString(3);
                prod[4]= curseur.getString(4);
                prod[5]= curseur.getString(5);
                produits.add(i,prod);
                i++;
                curseur.moveToNext();
            }

            curseur.close();
            return produits;
        }

    }


    public void ajout_produit(int id,Integer lot, String Label, String categorie, Integer quantite, Double prix_a, Double prix_v, String date,String dateachat)
  { SQLiteDatabase db=getWritableDatabase();

     String strl1="INSERT INTO PRODUCTS(ID,LOT,CATEGORIE,LABEL,PRIX_ACHAT,PRIX_VENTE,QUANTITE,DATE,DATEACHAT) VALUES("+id+","+lot+",'"+categorie+"','"+Label+"',"+prix_a+","+prix_v+","+quantite+",'"+date+"','"+dateachat+"');";
      Log.i("DATABASE", " Avant ajout");
      db.execSQL(strl1);
      Log.i("DATABASE", " Ajout réussit");
  }
  }




