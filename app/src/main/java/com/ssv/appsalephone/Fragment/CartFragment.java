package com.ssv.appsalephone.Fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ssv.appsalephone.Adapter.ProductCartAdapter;
import com.ssv.appsalephone.Class.DetailOrder;
import com.ssv.appsalephone.Class.Product;
import com.ssv.appsalephone.Class.Profile;
import com.ssv.appsalephone.Home;
import com.ssv.appsalephone.R;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartFragment extends Fragment {

    private int totalPrice;
    private View mView;
    private Home home;
    private DecimalFormat format;

    private RelativeLayout rlCartEmpty,rlCart;

    private List<Product> listCartProduct;
    private RecyclerView rcvCartProduct;
    private TextView tvCartTotalPrice;
    private EditText edtCartCustName,edtCartCustAddress,edtCartCustPhone;
    private Button btnCartOrder;

    private ProductCartAdapter productCartAdapter;
    private Profile profile;

    public CartFragment(List<Product> listCartProduct, Profile profile) {
        this.listCartProduct = listCartProduct;
        this.profile = profile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_cart, container, false);

        initItem(profile);

        setVisibilityView();

        return mView;
    }

    private void initItem(Profile profile){
        productCartAdapter = new ProductCartAdapter();
        rlCartEmpty = mView.findViewById(R.id.rl_cart_empty);
        rlCart = mView.findViewById(R.id.rl_cart);
        rcvCartProduct = mView.findViewById(R.id.rcv_cart_product);
        tvCartTotalPrice = mView.findViewById(R.id.tv_cart_total_price);
        edtCartCustName = mView.findViewById(R.id.edt_cart_cust_name);
        edtCartCustAddress = mView.findViewById(R.id.edt_cart_cust_address);
        edtCartCustPhone = mView.findViewById(R.id.edt_cart_cust_phone);

        edtCartCustName.setText(profile.getName());
        edtCartCustPhone.setText(profile.getPhone());
        edtCartCustAddress.setText(profile.getAddress());

        btnCartOrder = mView.findViewById(R.id.btn_cart_order);
        btnCartOrder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                addDataOrder();
            }
        });

        home = (Home) getActivity();
        format = new DecimalFormat("###,###,###");
    }

    private void setVisibilityView(){
        if (listCartProduct.size() == 0){

            setVisibilityEmptyCart();
        }else {

            setVisibilityCart();
            setDataProductCartAdapter();
        }
    }

    private void setVisibilityCart(){
        rlCartEmpty.setVisibility(View.GONE);
        rlCart.setVisibility(View.VISIBLE);
        String total = format.format(getTotalPrice());
        tvCartTotalPrice.setText( total +" VNĐ" );
    }

    private void setDataProductCartAdapter(){
        productCartAdapter.setData(listCartProduct,home,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(home,RecyclerView.VERTICAL,false);
        rcvCartProduct.setLayoutManager(linearLayoutManager);
        rcvCartProduct.setAdapter(productCartAdapter);
    }

    private int getTotalPrice(){
        for (Product product : listCartProduct){
            int priceProduct = product.getProductPrice() ;
            totalPrice = totalPrice +  priceProduct * product.getNumProduct();
        }
        return totalPrice;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addDataOrder(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Orders");

        Map<String,Object> map = new HashMap<>();

        Date date = new Date(System.currentTimeMillis());
        map.put("dateOrder", date.toString());
        map.put("custName",edtCartCustName.getText().toString());
        map.put("custAddress",edtCartCustAddress.getText().toString());
        map.put("custPhone",edtCartCustPhone.getText().toString());
        map.put("custEmail", profile.getEmail());

        int num = 0;
        for (Product product : listCartProduct){
            num = num + product.getNumProduct();
        }
        map.put("numProduct",num);
        map.put("totalPrice",totalPrice);
        map.put("status","Pending Accepted");

        String odrKey = myRef.push().getKey();
        myRef.child(odrKey).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                List<DetailOrder> listDetailOrder = makeDetailOrder(odrKey);

                for (DetailOrder detailOrder : listDetailOrder){

                    myRef.child(odrKey).child("detail").child(myRef.push().getKey())
                            .setValue(detailOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(),"Your order has been successfully!",Toast.LENGTH_SHORT).show();
                            listCartProduct.clear();
                            setVisibilityEmptyCart();
                            home.setCountProductInCart(0);
                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<DetailOrder> makeDetailOrder( String odrNo){
        List<DetailOrder> listDetailOrder = new ArrayList<>();
        for (Product product : home.getListCartProduct()){
            DetailOrder detailOrder = new DetailOrder();
            detailOrder.setOrderNo(odrNo);
            detailOrder.setProductName(product.getProductName());
            detailOrder.setProductPrice(product.getProductPrice());
            detailOrder.setUrlImg(product.getUrlImg());
            detailOrder.setNumProduct(product.getNumProduct());
            detailOrder.setSize(product.getSize());
            detailOrder.setStatus("Pending accepted");
            listDetailOrder.add(detailOrder);
        }
        return listDetailOrder;
    }

    public void setVisibilityEmptyCart(){
        rlCartEmpty.setVisibility(View.VISIBLE);
        rlCart.setVisibility(View.GONE);
    }

    public void setTotalPrice(int mode,int count, int priceProduct ){
        if( mode == 0){
            totalPrice = totalPrice - priceProduct * count;
        }else if (mode == 1){
            totalPrice = totalPrice + priceProduct * count;
        }

        tvCartTotalPrice.setText( format.format(totalPrice) + " VNĐ");
    }

    public void setCountForProduct(int possion,int countProduct){
        listCartProduct.get(possion).setNumProduct(countProduct);
    }

}