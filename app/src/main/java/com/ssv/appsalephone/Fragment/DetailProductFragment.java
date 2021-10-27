package com.ssv.appsalephone.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ssv.appsalephone.Class.Product;
import com.ssv.appsalephone.Home;
import com.ssv.appsalephone.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class DetailProductFragment extends Fragment {
    private DecimalFormat format = new DecimalFormat("###,###,###");

    private Boolean isAddToCart;

    private Home home;

    private View mView;
    private Product detailProduct;
    private List<Product> listCartProduct;
    private TextView tvDetailProductName,tvDetailProductPrice,tvDetailProductDescription;
    private Button btnDetailProductBuy, btn_sizeS, btn_sizeM, btn_sizeL;
    private ImageView imgDetailProductPhoto;
    private String size = "";

    public DetailProductFragment(Product product,List<Product> listProduct) {
        detailProduct = product;
        listCartProduct = listProduct;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_detail_product, container, false);

        initItem();

        setValueItem();

        return mView;
    }


    private void initItem(){

        isAddToCart = false;
        home = (Home) getActivity();
        tvDetailProductName = mView.findViewById(R.id.tv_detail_product_name);
        tvDetailProductPrice = mView.findViewById(R.id.tv_detail_product_price);
        tvDetailProductDescription = mView.findViewById(R.id.tv_detail_product_description);
        imgDetailProductPhoto = mView.findViewById(R.id.img_detail_product_photo);
        btnDetailProductBuy = mView.findViewById(R.id.btn_detail_product_buy);
        btn_sizeS = mView.findViewById(R.id.btn_sizeS);
        btn_sizeM = mView.findViewById(R.id.btn_sizeM);
        btn_sizeL = mView.findViewById(R.id.btn_sizeL);

        if(listCartProduct == null){
            listCartProduct = new ArrayList<>();
        }
    }

    private void setValueItem(){
        if (detailProduct != null){
            tvDetailProductName.setText(detailProduct.getProductName());
            tvDetailProductPrice.setText(format.format(detailProduct.getProductPrice() ) + " VNĐ");
            Glide.with(getContext()).load(detailProduct.getUrlImg()).into(imgDetailProductPhoto);
            tvDetailProductDescription.setText(detailProduct.getDescription());

            for (int i = 0;i< listCartProduct.size();i++){

                if (listCartProduct.get(i).getProductName().equals(detailProduct.getProductName())){
                    isAddToCart = true;
                    btnDetailProductBuy.setText("Added");
                    btnDetailProductBuy.setBackgroundResource(R.drawable.custom_button_gray);
                    break;
                }
            }

            btnDetailProductBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(size == ""){
                        Toast.makeText(getActivity(),"Please choose a size",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (isAddToCart){
                            Toast.makeText(getActivity(),"This product already exists in your cart",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            isAddToCart = true;
                            btnDetailProductBuy.setText("Added");
                            btnDetailProductBuy.setBackgroundResource(R.drawable.custom_button_gray);
                            home.addToListCartProduct(detailProduct);
                            home.setCountProductInCart(home.getCountProduct() + 1);
                            Toast.makeText(getActivity(),"Product has been added to your cart",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            btn_sizeS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(size != "s"){
                        size = "s";
                        detailProduct.setSize("s");
                        btn_sizeS.setBackgroundResource(R.drawable.custom_button_red);
                        btn_sizeM.setBackgroundResource(R.drawable.custom_button_gray);
                        btn_sizeL.setBackgroundResource(R.drawable.custom_button_gray);
                    }
                }
            });

            btn_sizeM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(size != "m"){
                        size = "m";
                        detailProduct.setSize("m");
                        btn_sizeS.setBackgroundResource(R.drawable.custom_button_gray);
                        btn_sizeM.setBackgroundResource(R.drawable.custom_button_red);
                        btn_sizeL.setBackgroundResource(R.drawable.custom_button_gray);
                    }
                }
            });

            btn_sizeL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(size != "l"){
                        size = "l";
                        detailProduct.setSize("l");
                        btn_sizeS.setBackgroundResource(R.drawable.custom_button_gray);
                        btn_sizeM.setBackgroundResource(R.drawable.custom_button_gray);
                        btn_sizeL.setBackgroundResource(R.drawable.custom_button_red);
                    }
                }
            });

        }
    }

}