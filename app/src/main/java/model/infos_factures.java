package model;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.tentative.R;

import java.util.ArrayList;

public class infos_factures extends AppCompatDialogFragment {

    TextView id;
    TextView nomadmin;
    TextView nomclient;
    TextView date;
    TextView prixtoatl;
    TextView Etat;
    ListView lv;
    LinearLayout layout;
    ArrayList<String[]> prods=new ArrayList<String[]>();
    Button  payer;
    Button imprimer;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String id1=getArguments().getString("id1");
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.infos_factures,null);
        id=view.findViewById(R.id.idcommande);
        nomadmin=view.findViewById(R.id.nomadminprenom);
        nomclient=view.findViewById(R.id.nomclient);
        date=view.findViewById(R.id.datecommande);
        prixtoatl=view.findViewById(R.id.prixtot);
        lv=view.findViewById(R.id.listeview);
        Etat=view.findViewById(R.id.etat);
        payer=view.findViewById(R.id.payer);
        layout=view.findViewById(R.id.payee_oupas);
        payer.setVisibility(View.GONE);
        MySQLiteOpenHelper db=new MySQLiteOpenHelper(getActivity(),null);
        SQLiteDatabase database=db.getReadableDatabase();
        String str="SELECT ID,IDADMIN,IDPROVIDER,PRIXTOTAL,DATE,PAYEE FROM FACTURE WHERE ID= "+Integer.valueOf(id1)+" ;";
        Cursor cursor=database.rawQuery(str,null);
        if(cursor!=null)
        { cursor.moveToFirst();
            id.setText(cursor.getString(0));
            Cursor nomadmin0=database.rawQuery("SELECT NOM,PRENOM FROM ADMINISTRATEURS WHERE ID= "+cursor.getString(1)+";",null);
            nomadmin0.moveToFirst();
            nomadmin.setText(nomadmin0.getString(0) +"  "+nomadmin0.getString(1));
            nomadmin0.close();
            Etat.setText(cursor.getString(5));
            if(Etat.getText().equals("false"))
            {Etat.setText("Non payée");

                payer.setVisibility(View.VISIBLE);
            }
            else
                Etat.setText("Payée");
            nomadmin0=database.rawQuery("SELECT NOM,PRENOM FROM PROVIDER WHERE ID= "+cursor.getString(2)+";",null);
            nomadmin0.moveToFirst();
            nomclient.setText(nomadmin0.getString(0) +"  "+nomadmin0.getString(1));
            date.setText(cursor.getString(4));
            prixtoatl.setText(cursor.getString(3));
            String st="SELECT IDPRODUCT,QUANTITE,PRIXU FROM PROVIDE WHERE IDFACTURE="+cursor.getString(0)+";";
            Cursor info_command=database.rawQuery(st,null);
            info_command.moveToFirst();
            int i=1;
            while (!info_command.isAfterLast())
            {String quantite=info_command.getString(1);
                String prixU=info_command.getString(2);
                Cursor in=database.rawQuery("SELECT LABEL FROM PRODUCTS WHERE ID=?",new String[]{info_command.getString(0)});
                in.moveToFirst();
                String label=in.getString(0);
                in.close();
                String[] inf=new String[5];
                inf[0]=String.valueOf(i);
                i++;
                inf[1]=label;
                inf[2]=quantite;
                inf[3]=prixU;
                inf[4]=String.valueOf(Integer.valueOf(quantite)*Integer.valueOf(prixU));

                prods.add(inf);
                info_command.moveToNext();

            }
            cursor.close();

            lv.setAdapter(new Comande_eff_adapter(getContext(),R.layout.produit_liste,prods));



        }
        cursor.close();

        payer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySQLiteOpenHelper db=new MySQLiteOpenHelper(getActivity(),null);
                SQLiteDatabase database=db.getWritableDatabase();
                ContentValues cv=new ContentValues();
                cv.put("PAYEE","true");
                database.update("FACTURE",cv,"ID=?",new String[]{id1});
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });
        builder.setView(view)
                .setTitle("FACTURE");

        return builder.create();


    }
}

