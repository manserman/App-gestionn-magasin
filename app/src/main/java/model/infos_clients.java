package model;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import controller.Ajout_client;

public class infos_clients extends AppCompatDialogFragment {

    TextView nom;
    TextView prenom;
    TextView ID;
    TextView Mail;
    TextView Adresse;
    TextView Telephone;
    TextView numpiece;
    Button supprimer;
    Button modifier;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String id1=getArguments().getString("id1");
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.infos_clients,null);
        nom=view.findViewById(R.id.nom_client);
        prenom=view.findViewById(R.id.prenom_client);
        ID=view.findViewById(R.id.id_client);
        Mail=view.findViewById(R.id.mail_client);
        Adresse=view.findViewById(R.id.adresse_client);
        Telephone=view.findViewById(R.id.tel_client);
        supprimer=view.findViewById(R.id.supprimer_client);
        modifier=view.findViewById(R.id.modifier_client);
        MySQLiteOpenHelper db=new MySQLiteOpenHelper(getActivity(),null);
        SQLiteDatabase database=db.getReadableDatabase();
        String str="SELECT ID,NOM,PRENOM,TELEPHONE,MAIL,ADRESSE FROM CLIENT WHERE ID=?";
        Cursor cursor=database.rawQuery(str,new String[] {id1});
        if(cursor!=null) {
            cursor.moveToFirst();
            ID.setText(cursor.getString(0));
            nom.setText(cursor.getString(1));
            prenom.setText(cursor.getString(2));
            Telephone.setText(cursor.getString(3));
            Mail.setText(cursor.getString(4));
            Adresse.setText(cursor.getString(5));


            cursor.close();


            builder.setView(view)
                    .setTitle("Infos Client");


    }
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
                        MySQLiteOpenHelper db=new MySQLiteOpenHelper(getActivity(),null);
                        SQLiteDatabase database=db.getWritableDatabase();
                        String str="DELETE FROM CLIENT WHERE ID="+" "+id1+";";
                        database.execSQL(str);
                        getActivity().finish();
                        startActivity(getActivity().getIntent());

                    }
                }).create().show();

            }
        });
        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt("type",1);
                bundle.putInt("id",Integer.valueOf(id1));
                Intent intent=new Intent(getContext(), Ajout_client.class);
                intent.putExtras(bundle);
                startActivity(intent);
               getActivity().finish();

            }
        });

        return builder.create();
    }
}
