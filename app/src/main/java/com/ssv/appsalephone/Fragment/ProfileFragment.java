package com.ssv.appsalephone.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ssv.appsalephone.Class.Profile;
import com.ssv.appsalephone.Home;
import com.ssv.appsalephone.LoginActivity;
import com.ssv.appsalephone.R;

import java.text.DecimalFormat;
import java.util.concurrent.Executor;

public class ProfileFragment extends Fragment {

    private DecimalFormat format = new DecimalFormat("###,###,###");

    private View mView;
    TextView name, email, phone, address;
    Profile profile;
    Button logout;

    public ProfileFragment() {

    }

    public ProfileFragment(Profile profile) {
        this.profile = profile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        name = mView.findViewById(R.id.profile_name);
        email = mView.findViewById(R.id.profile_email);
        phone = mView.findViewById(R.id.profile_phone);
        address = mView.findViewById(R.id.profile_address);
        logout = mView.findViewById(R.id.profile_logout);

        name.setText("Name: " + profile.getName());
        email.setText("Email: " + profile.getEmail());
        phone.setText("Phone: " + profile.getPhone());
        address.setText("Address: " + profile.getAddress());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return mView;
    }


}