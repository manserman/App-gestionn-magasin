package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.tentative.R;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.material.internal.ContextUtils.getActivity;

public class Produit_spinneradapter extends BaseAdapter {
    Context context;
    int m_ressource;
    ArrayList<String[]> prod;
    ArrayList<String[]> produits1;
    int t;
    public Produit_spinneradapter(Context context,int ressource, ArrayList<String[]> produits,int t)
    {
        this.t=t;
        this.context=context;
        this.m_ressource=ressource;
        this.prod=produits;
        if(produits!=null)
        produits1=new ArrayList<>(produits);
    }


    @Override
    public int getCount() {
       return prod.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        convertView=layoutInflater.inflate(m_ressource,parent,false);
        TextView desc=convertView.findViewById(R.id.le_seul);
        String[] produit=prod.get(position);
        if(t==1)
        desc.setText("  "+produit[1] +"(lot:"+produit[5]+")"  );
        else
            desc.setText("  "+produit[0]+". "+produit[1] +" " +produit[2]);

        return convertView;}

}
