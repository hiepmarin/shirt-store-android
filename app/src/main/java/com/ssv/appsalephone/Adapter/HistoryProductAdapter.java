package com.ssv.appsalephone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ssv.appsalephone.Class.DetailOrder;
import com.ssv.appsalephone.Class.Order;
import com.ssv.appsalephone.Class.Profile;
import com.ssv.appsalephone.Home;
import com.ssv.appsalephone.R;

import java.text.DecimalFormat;
import java.util.List;

public class HistoryProductAdapter extends RecyclerView.Adapter<HistoryProductAdapter.HistoryProductViewHolder> {

    private DecimalFormat formatPrice = new DecimalFormat("###,###,###");
    private List<DetailOrder> listDetailOrder;
    private List<Order> listOrder;
    private Order orderInfo;
    private Home home;
    private Profile profile;

    public HistoryProductAdapter(Profile profile){
        this.profile = profile;
    }

    public void setData(List<DetailOrder> listDetailOrder, List<Order> listOrder,Home home) {
        this.listDetailOrder = listDetailOrder;
        this.listOrder = listOrder;
        this.home = home;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        return new  HistoryProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryProductViewHolder holder, int position) {
        DetailOrder detailOrder = listDetailOrder.get(position);
        if (detailOrder == null){
            return;
        }
        else {
            Glide.with(holder.imgHistoryProduct.getContext()).load(detailOrder.getUrlImg()).into(holder.imgHistoryProduct);
            holder.tvHistoryProductName.setText(detailOrder.getProductName());
            holder.tvHistoryProductNum.setText(String.valueOf(detailOrder.getNumProduct()));
            holder.tvHistoryProductPrice.setText(formatPrice.format(detailOrder.getProductPrice()) + "VNĐ");
            holder.tvHistoryProductStatus.setText(detailOrder.getStatus());
            holder.tvHistoryProductOrderNo.setText(detailOrder.getOrderNo().toUpperCase());
            holder.tvHistoryProductSize.setText(detailOrder.getSize().toUpperCase());

            for (Order order : listOrder){
                if (order.getOrderNo().equals(detailOrder.getOrderNo() ) ){
                    holder.tvHistoryProductDate.setText(order.getDateOrder());
                    break;
                }
            }

            holder.tvHistoryProductOrderNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Order order : listOrder){
                        if (order.getOrderNo().equals(detailOrder.getOrderNo() ) ){
                            orderInfo = order;
                            break;
                        }
                    }

                    for (DetailOrder itemDetailOrder :listDetailOrder){
                        if (detailOrder.getOrderNo().equals(itemDetailOrder.getOrderNo())){
                            orderInfo.addToListDetailOrder(itemDetailOrder);
                        }
                    }
                    home.toOrderInfoFragment(orderInfo);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (listDetailOrder.size() != 0){
            return listDetailOrder.size();
        }else {
            return 0;
        }
    }

    public class HistoryProductViewHolder extends RecyclerView.ViewHolder{

        ImageView imgHistoryProduct;
        TextView tvHistoryProductName,tvHistoryProductNum,tvHistoryProductPrice,tvHistoryProductDate
                ,tvHistoryProductStatus,tvHistoryProductOrderNo, tvHistoryProductSize;

        public HistoryProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHistoryProduct = itemView.findViewById(R.id.img_history_product);
            tvHistoryProductName = itemView.findViewById(R.id.tv_history_product_name);
            tvHistoryProductNum = itemView.findViewById(R.id.tv_history_product_num);
            tvHistoryProductPrice = itemView.findViewById(R.id.tv_history_product_price);
            tvHistoryProductDate = itemView.findViewById(R.id.tv_history_product_date);
            tvHistoryProductStatus = itemView.findViewById(R.id.tv_history_product_status);
            tvHistoryProductOrderNo = itemView.findViewById(R.id.tv_history_product_orderNo);
            tvHistoryProductSize = itemView.findViewById(R.id.tv_history_product_size);
        }
    }
}
