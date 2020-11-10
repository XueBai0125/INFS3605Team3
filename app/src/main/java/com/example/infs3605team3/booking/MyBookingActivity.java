package com.example.infs3605team3.booking;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.infs3605team3.R;
import com.example.infs3605team3.model.Booking;
import com.example.infs3605team3.model.Office;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

public class MyBookingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<Booking> houseList;
    private ProgressDialog mProgressDialog;
    private BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        mProgressDialog= new ProgressDialog(this);

        houseList = new ArrayList<>();



        Toolbar mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle("My Booking");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImmersionBar.with(this)
                .transparentStatusBar()
                .titleBar(mToolbar)
                .statusBarDarkFont(true, 0.2f)
                .init();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

         adapter = new BookingAdapter(this,houseList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BookingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onItemDeleteClick(int position) {
                delete(position);
            }
        });
        init();

    }
    private void delete(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Would you want to delete the Order")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Booking item = houseList.get(position);
                        FirebaseDatabase.getInstance().getReference().child("Booking").child(item.id).removeValue();
                    }
                }).setNegativeButton("Cancel",null).create().show();
    }
    private void init() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Booking");
        mProgressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mProgressDialog.dismiss();
                houseList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Booking office = snapshot.getValue(Booking.class);
                    office.id = (snapshot.getKey());
                    String uid =  FirebaseAuth.getInstance().getCurrentUser().getUid();
                    if (office.uid.equals(uid)){
                        houseList.add(office);
                    }




                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mProgressDialog.dismiss();
            }
        });
    }
}