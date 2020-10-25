package com.example.infs3605team3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.infs3605team3.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MineFragment extends Fragment implements View.OnClickListener {

    View rootview;
    private FirebaseAuth mAuth;
    private User model;
    private TextView tvFirst;
    private TextView tvSecond;
    private TextView tvThree;
    private TextView tvFour;
    private TextView tvUsername;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_mine,container,false);
        return rootview;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        rootview.findViewById(R.id.ll_first).setOnClickListener(this);
        rootview.findViewById(R.id.ll_second).setOnClickListener(this);
        rootview.findViewById(R.id.ll_three).setOnClickListener(this);
        rootview.findViewById(R.id.ll_four).setOnClickListener(this);
        rootview.findViewById(R.id.rl_user).setOnClickListener(this);
        tvUsername = rootview.findViewById(R.id.tv_username);
        tvFirst = rootview.findViewById(R.id.tv_first);
        tvSecond = rootview.findViewById(R.id.tv_second);
        tvThree = rootview.findViewById(R.id.tv_three);
        tvFour = rootview.findViewById(R.id.tv_four);
    }

    private void initData() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference myDf = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user.getUid());
        myDf.addValueEventListener(new ValueEventListener() {



            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 model = dataSnapshot.getValue(User.class);
                tvUsername.setText(model.getLastName()+model.getFirstName());
                if (model.equals("tenant")){
                    tvFirst.setText("My Booking");
                    tvSecond.setText("My Experience");
                    tvThree.setText("Wishlist");
                    tvFour.setText("Q&A");
                }else{
                    tvFirst.setText("My Listing");
                    tvSecond.setText("List a Space");
                    tvThree.setText("Q&");
                    tvFour.setText("My Experience");
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_user:
                if (model==null){
                    return;
                }
                Intent infosActivity =    new Intent(getActivity(),InfosActivity.class);
                infosActivity.putExtra("model",model);
                startActivity(infosActivity);
                break;
            case R.id.ll_first:

                break;
            case R.id.ll_second:

                break;
            case R.id.ll_three:

                break;
            case R.id.ll_four:

                break;
        }
    }
}
