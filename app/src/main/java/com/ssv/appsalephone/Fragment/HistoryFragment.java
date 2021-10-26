package com.ssv.appsalephone.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssv.appsalephone.Adapter.HistoryProductAdapter;
import com.ssv.appsalephone.Class.DetailOrder;
import com.ssv.appsalephone.Class.Order;
import com.ssv.appsalephone.Class.Profile;
import com.ssv.appsalephone.Home;
import com.ssv.appsalephone.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    // region Variable

    private Home home;
    private List<Order> listOrder;
    private List<DetailOrder> listDetailOrder;

    private View mView;
    private EditText edtHistoryPhone;
    private Button btnHistorySearch;
    private RecyclerView rcvHistorySearch;

    private HistoryProductAdapter historyProductAdapter;
    private Profile profile;

    public HistoryFragment() {
    }

    public HistoryFragment(Profile profile) {
        this.profile = profile;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!edtHistoryPhone.getText().toString().trim().isEmpty()) {
            findOrder();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_history, container, false);

        initItem(profile);

        return mView;
    }

    private void initItem(Profile profile) {
        listOrder = new ArrayList<>();
        listDetailOrder = new ArrayList<>();

        home = (Home) getActivity();

        historyProductAdapter = new HistoryProductAdapter(profile);

        edtHistoryPhone = mView.findViewById(R.id.edt_history_phone);

        rcvHistorySearch = mView.findViewById(R.id.rcv_history_search);
        btnHistorySearch = mView.findViewById(R.id.btn_history_search);
        findOrder();
        btnHistorySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOrder();
            }
        });
    }

    private void setDataHistoryProductAdapter() {
        historyProductAdapter.setData(listDetailOrder, listOrder, home);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(home, RecyclerView.VERTICAL, false);
        rcvHistorySearch.setLayoutManager(linearLayoutManager);
        rcvHistorySearch.setAdapter(historyProductAdapter);
    }

    private void findOrder() {

        listOrder.clear();
        listDetailOrder.clear();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Order");

        myRef.orderByChild("custEmail").equalTo(profile.getEmail())
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        historyProductAdapter.notifyDataSetChanged();
                        for (DataSnapshot dataOrder : snapshot.getChildren()) {
                            Order order = dataOrder.getValue(Order.class);
                            order.setOrderNo(dataOrder.getKey());
                            listOrder.add(order);
                        }

                        if (listOrder.size() > 0) {
                            findDetailOrder(myRef);
                        } else {
                            Toast.makeText(getContext(), "Can't find order history", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Can't get order history from firebase", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void findDetailOrder(DatabaseReference myRef) {
        if (listOrder.size() > 0) {
            for (int i = 0; i < listOrder.size(); i++) {
                Order order = listOrder.get(i);
                myRef.child(order.getOrderNo()).child("detail").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataDetail : snapshot.getChildren()) {
                            historyProductAdapter.notifyDataSetChanged();
                            DetailOrder detailOrder = dataDetail.getValue(DetailOrder.class);
                            listDetailOrder.add(detailOrder);
                        }

                        if (listDetailOrder.size() > 0) {
                            setDataHistoryProductAdapter();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Can't get order history from firebase", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

}