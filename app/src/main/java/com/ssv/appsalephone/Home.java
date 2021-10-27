package com.ssv.appsalephone;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ssv.appsalephone.Class.Order;
import com.ssv.appsalephone.Class.Product;
import com.ssv.appsalephone.Class.Profile;
import com.ssv.appsalephone.Fragment.CartFragment;
import com.ssv.appsalephone.Fragment.DetailProductFragment;
import com.ssv.appsalephone.Fragment.HistoryFragment;
import com.ssv.appsalephone.Fragment.OrderInfoFragment;
import com.ssv.appsalephone.Fragment.ProductFragment;
import com.ssv.appsalephone.Fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private List<Product> listCartProduct;
    private int countProduct;
    private AHBottomNavigation ahBotNavHome;
    private FragmentTransaction fragmentTransaction;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initItem();
        setDataBotNavHome();
    }

    private void initItem() {

        ahBotNavHome = findViewById(R.id.ahbotnav_home);
        if (listCartProduct == null) {
            listCartProduct = new ArrayList<>();
        }
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new ProductFragment());
        fragmentTransaction.commit();

    }

    private void setDataBotNavHome() {

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        if(userId == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        Profile profile = new Profile();
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                profile.setName(value.getString("name"));
                profile.setEmail(value.getString("email"));
                profile.setAddress(value.getString("address"));
                profile.setPhone(value.getString("phone"));
            }
        });

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_product, R.drawable.ic_baseline_home_24, R.color.teal_700);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_cart, R.drawable.ic_baseline_add_shopping_cart_24, R.color.teal_700);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_history, R.drawable.ic_baseline_history_24, R.color.teal_700);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.tab_profile, R.drawable.ic_menu_profile, R.color.teal_700);

        ahBotNavHome.addItem(item1);
        ahBotNavHome.addItem(item2);
        ahBotNavHome.addItem(item3);
        ahBotNavHome.addItem(item4);

        ahBotNavHome.setColored(true);

        ahBotNavHome.setDefaultBackgroundColor(getResources().getColor(R.color.white));

        ahBotNavHome.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                switch (position) {
                    case 0:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new ProductFragment());
                        fragmentTransaction.commit();
                        break;

                    case 1:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new CartFragment(listCartProduct, profile));
                        fragmentTransaction.commit();
                        break;
                    case 2:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, new HistoryFragment(profile));
                        fragmentTransaction.commit();
                        break;
                    case 3:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();

                        fragmentTransaction.replace(R.id.content_frame, new ProfileFragment(profile));
                        fragmentTransaction.commit();
                        break;
                }

                return true;
            }
        });
    }

    public void setCountProductInCart(int count) {
        countProduct = count;
        AHNotification notification = new AHNotification.Builder()
                .setText(String.valueOf(count))
                .setBackgroundColor(ContextCompat.getColor(Home.this, R.color.red))
                .setTextColor(ContextCompat.getColor(Home.this, R.color.white))
                .build();
        ahBotNavHome.setNotification(notification, 1);
    }
    
    public void toDetailProductFragment(Product product) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new DetailProductFragment(product, listCartProduct));
        fragmentTransaction.commit();
    }
    
    public void toOrderInfoFragment(Order orderInfo) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new OrderInfoFragment(orderInfo));
        fragmentTransaction.addToBackStack(OrderInfoFragment.TAG);
        fragmentTransaction.commit();
    }

    public void addToListCartProduct(Product product) {
        listCartProduct.add(product);
    }

    public List<Product> getListCartProduct() {
        return listCartProduct;
    }

    public int getCountProduct() {
        return countProduct;
    }

    public void setCountForProduct(int position, int countProduct) {
        listCartProduct.get(position).setNumProduct(countProduct);
    }
}