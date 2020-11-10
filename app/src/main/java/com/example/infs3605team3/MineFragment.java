package com.example.infs3605team3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.infs3605team3.booking.MyBookingActivity;
import com.example.infs3605team3.experience.MyExperienceActivity;
import com.example.infs3605team3.model.User;
import com.example.infs3605team3.qa.QAActivity;
import com.example.infs3605team3.wishlist.WishListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gyf.immersionbar.ImmersionBar;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        Toolbar llToolbar =  rootview.findViewById(R.id.toolbar);
        ImmersionBar.setTitleBar(this, llToolbar);
        rootview.findViewById(R.id.ll_first).setOnClickListener(this);
        rootview.findViewById(R.id.ll_second).setOnClickListener(this);
        rootview.findViewById(R.id.ll_three).setOnClickListener(this);
        rootview.findViewById(R.id.ll_four).setOnClickListener(this);
        rootview.findViewById(R.id.rl_user).setOnClickListener(this);
        llToolbar.findViewById(R.id.iv_set).setOnClickListener(this);
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
                tvUsername.setText(model.getLastName()+"  "+model.getFirstName());
                if (model.getRole().equals("tenant")){
                    tvFirst.setText("My Booking");
                    tvSecond.setText("My Experience");
                    tvThree.setText("Wishlist");
                    tvFour.setText("Q&A");
                }else{
                    tvFirst.setText("My Listing");
                    tvSecond.setText("List a Space");
                    tvThree.setText("Q&A");
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
        Intent intent= null;
        switch (v.getId()){

            case R.id.iv_set:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Alert")
                        .setMessage("Confirm to log out")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                                startActivity(new Intent(getActivity(),LoginActivity.class));
                                getActivity().finish();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
            case R.id.rl_user:
                if (model==null){
                    return;
                }
                Intent infosActivity =    new Intent(getActivity(),InfosActivity.class);
                infosActivity.putExtra("model",model);
                startActivity(infosActivity);
                break;
            case R.id.ll_first:
                if (model==null){
                    return;
                }
                if (model.getRole().equals("tenant")){
                     intent =    new Intent(getActivity(), MyBookingActivity.class);
                    startActivity(intent);
                }else{
                     intent =    new Intent(getActivity(), ListActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.ll_second:
                if (model==null){
                    return;
                }
                if (model.getRole().equals("tenant")){
                    Intent intent1 =    new Intent(getActivity(), MyExperienceActivity.class);
                    startActivity(intent1);
                }else{
                    Intent intent1 =    new Intent(getActivity(), AddOfficeActivity.class);
                    startActivity(intent1);
                }

                break;
            case R.id.ll_three:
                if (model==null){
                    return;
                }
                if (model.getRole().equals("tenant")){
                    intent =    new Intent(getActivity(), WishListActivity.class);
                    startActivity(intent);
                }else{
                    intent =    new Intent(getActivity(), QAActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.ll_four:
                if (model.getRole().equals("tenant")){
                    intent =    new Intent(getActivity(), QAActivity.class);
                    startActivity(intent);
                }else{
                    intent =    new Intent(getActivity(), MyExperienceActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
}
