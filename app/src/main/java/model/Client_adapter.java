package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tentative.R;

import java.util.ArrayList;
import java.util.List;

public class Client_adapter extends ArrayAdapter<String[]> {
    Context context;
    int m_ressource;
    ArrayList<String[]> prod;
    ArrayList<String[]> produits1;

    public Client_adapter(Context context,int ressource, ArrayList<String[]> admins)
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
        TextView numero=convertView.findViewById(R.id.id_item);
        TextView desc=convertView.findViewById(R.id.textView_list);
        TextView prix=convertView.findViewById(R.id.textView_list1);
        TextView date=convertView.findViewById(R.id.date);
        if(prod!=null)
        {String[] produit=prod.get(position);
        numero.setText(produit[0]+" .");
        desc.setText(produit[1]+" "+produit[2]);
        date.setText(produit[3] );
        prix.setText(produit[5]);}


        return convertView;
    }
    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<String[]> filteredItems = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredItems.addAll(produits1);
            } else {
                String filterpattern = constraint.toString().toLowerCase().trim();
                int j = 0;
                for (int i = 0; i < produits1.size(); i++) {
                    String[] item = produits1.get(i);
                    if (item[1].toLowerCase().contains(filterpattern) || item[2].toLowerCase().contains(filterpattern)) {
                        filteredItems.add(j, item);
                        j++;

                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredItems;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            prod.clear();
            prod.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}
