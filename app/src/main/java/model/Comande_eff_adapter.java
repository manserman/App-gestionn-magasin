package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tentative.R;

import java.util.ArrayList;

public class Comande_eff_adapter extends ArrayAdapter<String[]> {
        Context context;
        int m_ressource;
        ArrayList<String[]> prod;
        ArrayList<String[]> produits1;

public Comande_eff_adapter(Context context,int ressource, ArrayList<String[]> admins)
        {
        super(context,ressource,admins);
        this.context=context;
        this.m_ressource=ressource;
        prod=admins;
        if(admins!=null)
        produits1=new ArrayList<>(admins);
        }



@NonNull
@Override
public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        convertView=layoutInflater.inflate(m_ressource,parent,false);
        TextView numero=convertView.findViewById(R.id.ordre);
        TextView desc=convertView.findViewById(R.id.libelle);
        TextView prix=convertView.findViewById(R.id.prixu);
        TextView Quantite=convertView.findViewById(R.id.qte1);
        TextView soustotale=convertView.findViewById(R.id.Sous_Total);
        String[] produit=prod.get(position);
        numero.setText(produit[0]+" .");
        desc.setText(produit[1]);
        prix.setText(produit[3]);
       Quantite.setText(produit[2] );
        soustotale.setText(produit[4]);


        return convertView;
        }


        }

