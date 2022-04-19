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

public class Infos_admins extends AppCompatDialogFragment {

    TextView nom;
    TextView prenom;
    TextView ID;
    TextView Mail;
    TextView Adresse;
    TextView Telephone;
    TextView Type;
    TextView num_piece;
    Button supprimer;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String id1=getArguments().getString("id1");
        String id2=getArguments().getString("id2");
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.infos_admins,null);
        nom=view.findViewById(R.id.nom_admin);
        prenom=view.findViewById(R.id.prenom_admin);
        ID=view.findViewById(R.id.id_admin);
        Mail=view.findViewById(R.id.mail_admin);
        Adresse=view.findViewById(R.id.adresse_admin);
        Telephone=view.findViewById(R.id.tel_admin);
        Type=view.findViewById(R.id.type_admin);
        num_piece=view.findViewById(R.id.num_piece_admin);
        supprimer=view.findViewById(R.id.supprimer_admin);
        MySQLiteOpenHelper db=new MySQLiteOpenHelper(getActivity(),null);
        SQLiteDatabase database=db.getReadableDatabase();
        Cursor curso=database.rawQuery("SELECT CATEGORIE FROM ADMINISTRATEURS WHERE USERNAME=?",new String[]{id1});
        curso.moveToFirst();
        if(curso.getString(0).equals("PRINCIPAL"))
            supprimer.setVisibility(View.VISIBLE);
        String str="SELECT ID,NOM,PRENOM,CATEGORIE,TELEPHONE,MAIL,ADRESSE,NUM_PIECE FROM ADMINISTRATEURS WHERE ID=?";
        Cursor cursor=database.rawQuery(str,new String[] {id2});
        if(cursor!=null) {
            cursor.moveToFirst();
            ID.setText(cursor.getString(0));
            nom.setText(cursor.getString(1));
            prenom.setText(cursor.getString(2));
            Type.setText(cursor.getString(3));
            Telephone.setText(cursor.getString(4));
            Mail.setText(cursor.getString(5));
            Adresse.setText(cursor.getString(6));
            num_piece.setText(cursor.getString(7));

            cursor.close();


            builder.setView(view)
                    .setTitle("Infos Administrateurs");


           supprimer.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
                   dlgAlert.setMessage("Voulez-vous vraiment rétirer ce administrateur de la liste?").setTitle("Supprimer?").setNegativeButton("Non", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {

                       }
                   }).setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           MySQLiteOpenHelper db=new MySQLiteOpenHelper(getActivity(),null);
                           SQLiteDatabase database=db.getWritableDatabase();
                           String str="DELETE FROM ADMINISTRATEURS WHERE ID="+" "+id2+";";
                           database.execSQL(str);
                           getActivity().finish();
                           startActivity(getActivity().getIntent());

                       }
                   }).create().show();

               }
           });


        }
        return builder.create();

}}
