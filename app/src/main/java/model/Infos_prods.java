package model;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.tentative.R;

import static android.content.Intent.getIntent;

public class Infos_prods extends AppCompatDialogFragment {

    TextView label;
    TextView idet;
    TextView lot;
    TextView date;
    TextView prixAchat;
    TextView prixVente;
    TextView categorie;
    TextView quantite;
    TextView dateachat;
    Button supprimer;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String id1=getArguments().getString("id1");
        String Lot1=getArguments().getString("lot1");
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_produits,null);
        label=view.findViewById(R.id.label_dialog);
        lot=view.findViewById(R.id.lot_dailog);
        idet=view.findViewById(R.id.id_produit_dialog);
        date=view.findViewById(R.id.date_p_dialog);
        dateachat=view.findViewById(R.id.dateachat_dialog);
        prixAchat=view.findViewById(R.id.prix_achat_dialog);
        prixVente=view.findViewById(R.id.prix_vente_dialog);
        categorie=view.findViewById(R.id.categorie_dialog);
        quantite=view.findViewById(R.id.quantite_dialog);
        supprimer=view.findViewById(R.id.supprimer_produit);
        MySQLiteOpenHelper db=new MySQLiteOpenHelper(getActivity(),null);
        SQLiteDatabase database=db.getReadableDatabase();
        String str="SELECT LOT,LABEL,CATEGORIE,PRIX_ACHAT,PRIX_VENTE,QUANTITE,DATE,DATEACHAT,ID FROM PRODUCTS WHERE ID= "+Integer.valueOf(id1)+" AND LOT= "+Integer.valueOf(Lot1)+" ;";
        Cursor cursor=database.rawQuery(str,null);
        if(cursor!=null)
        { cursor.moveToFirst();
            lot.setText(cursor.getString(0));
            label.setText(cursor.getString(1));
            categorie.setText(cursor.getString(2));
            prixAchat.setText(cursor.getString(3));
            prixVente.setText(cursor.getString(4));
            quantite.setText(cursor.getString(5));
            date.setText(cursor.getString(6));
            dateachat.setText(cursor.getString(7));
            idet.setText(cursor.getString(8));}
        cursor.close();
        builder.setView(view)
                .setTitle("Infos produits");
        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
                    dlgAlert.setMessage("Voulez-vous vraiment rétirer ce client de la liste?").setTitle("Supprimer?").setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int d= database.delete("PRODUCTS","LOT=? AND ID=?", new String[]{lot.getText().toString(),idet.getText().toString()});
                            database.close();
                            getActivity().finish();
                            startActivity(getActivity().getIntent());

                        }
                    }).create().show();

                }


        });

        return builder.create();



    }


}
