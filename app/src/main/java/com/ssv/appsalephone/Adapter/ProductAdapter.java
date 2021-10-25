package com.ssv.appsalephone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ssv.appsalephone.Class.Product;
import com.ssv.appsalephone.Home;
import com.ssv.appsalephone.R;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private DecimalFormat formatPrice = new DecimalFormat("###,###,###");
    private List<Product> mListProduct;
    private Home home;

    // Khai báo Interface giúp cho việc click vào phần tử của recycleview
    public interface IClickItemProductListener{
        void onClickItemProduct(Product product);
    }

    public void setData(List<Product> mList, Home home) {
        this.mListProduct = mList;
        this.home = home;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product product = mListProduct.get(position);

        if (product == null) {
            return;
        }
        else {
            Glide.with(holder.imgPhotoProduct.getContext()).load(product.getUrlImg()).into(holder.imgPhotoProduct);
            holder.tvProductName.setText(product.getProductName());
            holder.tvProductPrice.setText(formatPrice.format(product.getProductPrice()) + " VNĐ");

            holder.setItemClickListener(new IClickItemProductListener() {
                @Override
                public void onClickItemProduct(Product product) {
                    home.toDetailProductFragment(product);
                }
            });
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgPhotoProduct;
        TextView tvProductName,tvProductPrice;
        IClickItemProductListener iClickItemProductListener;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPhotoProduct = itemView.findViewById(R.id.img_photo_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(IClickItemProductListener itemClickListener){
            this.iClickItemProductListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.iClickItemProductListener.onClickItemProduct(mListProduct.get(getAdapterPosition()));
        }
    }

    @Override
    public int getItemCount() {
        if (mListProduct != null) {
            return mListProduct.size();
        }
        else
            return 0;
    }
}
