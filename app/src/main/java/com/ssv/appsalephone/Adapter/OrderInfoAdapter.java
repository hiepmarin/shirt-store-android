package com.ssv.appsalephone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ssv.appsalephone.Class.DetailOrder;
import com.ssv.appsalephone.R;

import java.text.DecimalFormat;
import java.util.List;

public class OrderInfoAdapter extends RecyclerView.Adapter<OrderInfoAdapter.OrderInfoViewHolder> {

    private DecimalFormat formatPrice = new DecimalFormat("###,###,###");
    private List<DetailOrder> listDetailOrder;

    public void setData(List<DetailOrder> listDetailOrder){
        this.listDetailOrder = listDetailOrder;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_info,parent,false);
        return new OrderInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderInfoViewHolder holder, int position) {
        DetailOrder detailOrder = listDetailOrder.get(position);
        if (detailOrder == null){
            return;
        }
        else {
            Glide.with(holder.imgOrderInfo.getContext()).load(detailOrder.getUrlImg()).into(holder.imgOrderInfo);
            holder.tvOrderInfoName.setText(detailOrder.getProductName());
            holder.tvOrderInfoNum.setText(String.valueOf(detailOrder.getNumProduct()));
            holder.tvOrderInfoPrice.setText(formatPrice.format(detailOrder.getProductPrice()) + " VNĐ");
            holder.tvOrderInfoStatus.setText(detailOrder.getStatus());
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

    public class OrderInfoViewHolder extends RecyclerView.ViewHolder{

        ImageView imgOrderInfo;
        TextView tvOrderInfoName,tvOrderInfoNum,tvOrderInfoPrice,tvOrderInfoStatus;

        public OrderInfoViewHolder(@NonNull View itemView) {
            super(itemView);

            imgOrderInfo = itemView.findViewById(R.id.img_order_info);
            tvOrderInfoName = itemView.findViewById(R.id.tv_order_info_name);
            tvOrderInfoNum = itemView.findViewById(R.id.tv_order_info_num);
            tvOrderInfoPrice = itemView.findViewById(R.id.tv_order_info_price);
            tvOrderInfoStatus = itemView.findViewById(R.id.tv_order_info_status);
        }
    }
}
