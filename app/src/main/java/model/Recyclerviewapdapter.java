package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.tentative.R;

import java.util.ArrayList;
import java.util.List;

import model.MySQLiteOpenHelper;
import model.Recyclerviewapdapter;

import com.example.tentative.R;

import java.util.ArrayList;


public class Recyclerviewapdapter extends RecyclerView.Adapter<Recyclerviewapdapter.MyViewHolder>implements Filterable {



    private ArrayList<String[]> produits;
    private ArrayList<String[]> produits1;
    private Context context;
    public Recyclerviewapdapter(ArrayList<String[]> prod,Context context)
    {
        this.produits=prod;
        if(prod!=null)
        produits1=new ArrayList<String[]>(produits);
        this.context=context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
     private TextView nom;
     private TextView prix;
        private TextView id;
     private RelativeLayout layout;
    public MyViewHolder(final View view)
     {super(view);
     id=view.findViewById(R.id.id_item);
     nom= view.findViewById(R.id.textView_list);
     prix=view.findViewById(R.id.textView_list1);
     layout=view.findViewById(R.id.case1);

     }
    }
    @NonNull
    @Override
    public Recyclerviewapdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
    return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Recyclerviewapdapter.MyViewHolder holder, int position) {
        if(produits!=null)
        {String[] prod=produits.get(position);
        holder.id.setText(prod[0]+". ");
     holder.nom.setText(prod[1]);
     holder.prix.setText("Catégorie: " +prod[3]+" Prix: "+prod[2]+" DA                   expiration:"+prod[4]  );}



    }

    @Override
    public int getItemCount() {
        if(produits!=null)
        return produits.size();
        else return 0;
    }
    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<String[]> filteredItems=new ArrayList<>();
            if(constraint==null || constraint.length()==0)
            {
                filteredItems.addAll(produits1);
            }
            else
            {
                String filterpattern=constraint.toString().toLowerCase().trim();
                int j=0;
                for(int i=0;i<produits1.size();i++)
                { String[] item=produits1.get(i);
                    if(item[1].toLowerCase().contains(filterpattern) || item[0].toLowerCase().contains(filterpattern) )
                    {filteredItems.add(j,item);
                    j++;

                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredItems;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
                   produits.clear();
                   produits.addAll((List) results.values);
                   notifyDataSetChanged();

        }
    };
}
