package model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.tentative.R;

import java.util.ArrayList;
import java.util.List;

public class facures_adapter extends ArrayAdapter<String[]> implements Filterable {
    Context context;
    int m_ressource;
    ArrayList<String[]> prod;
    ArrayList<String[]> produits1;
    public  facures_adapter(Context context,int ressource, ArrayList<String[]> produits)
    {
        super(context,ressource,produits);
        this.context=context;
        this.m_ressource=ressource;
        prod=produits;
        if(produits!=null)
        produits1=new ArrayList<>(produits);
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
        String[] produit=prod.get(position);
        numero.setText(produit[0]+" .");
        SQLiteDatabase database=new MySQLiteOpenHelper(getContext(),null).getReadableDatabase();
        Cursor curs=database.rawQuery("SELECT NOM,PRENOM FROM PROVIDER WHERE ID=?",new String[]{produit[1]});
        curs.moveToFirst();
        desc.setText("Fournisseur: "+curs.getString(0)+" "+curs.getString(1));
        curs.close();
        if(produit[3].equals("false"))
            prix.setText(" Non payée "  );
        else
            prix.setText(" payée "  );
        date.setText("Total: "+produit[2]);
        prix.setTextColor(ContextCompat.getColor(context, R.color.white));
        prix.setBackgroundColor(ContextCompat.getColor(context, R.color.test1));



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
                    Cursor curs=new MySQLiteOpenHelper(getContext(),null).getReadableDatabase().rawQuery("SELECT NOM,PRENOM FROM PROVIDER WHERE ID=?",new String[]{item[1]});
                    curs.moveToFirst();
                    if (curs.getString(0).toLowerCase().contains(filterpattern) || item[0].toLowerCase().contains(filterpattern)  || curs.getString(1).toLowerCase().contains(filterpattern)) {
                        filteredItems.add(j, item);
                        j++;

                    }
                    curs.close();
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
