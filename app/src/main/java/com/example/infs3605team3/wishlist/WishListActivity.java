package com.example.infs3605team3.wishlist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs3605team3.DetailedActivity;
import com.example.infs3605team3.MyOfficeAdapter;
import com.example.infs3605team3.OfficeDetailActivity;
import com.example.infs3605team3.R;
import com.example.infs3605team3.booking.BookingAdapter;
import com.example.infs3605team3.model.Booking;
import com.example.infs3605team3.model.Office;
import com.example.infs3605team3.model.Wish;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.HashMap;

public class WishListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<Wish> houseList;
    private ProgressDialog mProgressDialog;
    private WishAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        mProgressDialog = new ProgressDialog(this);
        houseList = new ArrayList<>();
        Toolbar mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle("Wishlist");
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

         adapter = new WishAdapter(this,houseList);
        recyclerView.setAdapter(adapter);
        initDataList();
        adapter.setOnItemClickListener(new WishAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                clickResponse(position);
            }

            @Override
            public void onItemLongClick(int position) {
                delete(position);
            }


        });
    }

    private void clickResponse(int position) {
        Wish item = houseList.get(position);
        Intent intent = new Intent(this, OfficeDetailActivity.class);
        intent.putExtra("id",item.officeId);
        startActivity(intent);
    }

    private void delete(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Would you want to delete the wish")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Wish item = houseList.get(position);
                        FirebaseDatabase.getInstance().getReference().child("Wish").child(item.id).removeValue();
                    }
                }).setNegativeButton("Cancel",null).create().show();
    }

    public void initDataList(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Wish");
        mProgressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                mProgressDialog.dismiss();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Wish wish = snapshot.getValue(Wish.class);
                    wish.id = (snapshot.getKey());
                    if (wish.uid.equals(uid)){
                        houseList.add(wish);
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