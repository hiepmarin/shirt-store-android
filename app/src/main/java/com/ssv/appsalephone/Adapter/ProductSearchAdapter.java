package com.ssv.appsalephone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ssv.appsalephone.Class.Product;
import com.ssv.appsalephone.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductSearchAdapter extends ArrayAdapter<Product> {

    private DecimalFormat formatPrice = new DecimalFormat("###,###,###");
    private List<Product> listSearchProduct;

    public ProductSearchAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        listSearchProduct= new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search,parent,false);
        }

        ImageView imgSearch = convertView.findViewById(R.id.img_search);
        TextView tvSearchName = convertView.findViewById(R.id.tv_search_name);
        TextView tvSearchPrice = convertView.findViewById(R.id.tv_search_price);

        Product product = getItem(position);

        Glide.with(imgSearch.getContext()).load(product.getUrlImg()).into(imgSearch);
        tvSearchName.setText(product.getProductName());
        tvSearchPrice.setText(formatPrice.format(product.getProductPrice()) + " VNĐ");

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Product> listSuggest = new ArrayList<>();

                if (constraint == null || constraint.length() == 0){
                    listSuggest.addAll(listSearchProduct);
                }
                else {
                    String filter = constraint.toString().toLowerCase().trim();
                    for (Product p : listSearchProduct){
                        if (p.getProductName().toLowerCase().contains(filter)){
                            listSuggest.add(p);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listSuggest;
                filterResults.count = listSuggest.size();

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll( (List<Product>) results.values);
                notifyDataSetInvalidated();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ( (Product) resultValue).getProductName();
            }
        };
    }
}
