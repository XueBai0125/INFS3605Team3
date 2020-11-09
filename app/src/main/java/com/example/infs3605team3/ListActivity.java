package com.example.infs3605team3;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.infs3605team3.model.Office;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {


    private RecyclerView rview;
    RecyclerView.LayoutManager layoutManager;
    MyOfficeAdapter adapter;
    private ArrayList<Office> list = new ArrayList();
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle("My Listing");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mProgressDialog= new ProgressDialog(this);
        rview = findViewById(R.id.rview);
        layoutManager = new LinearLayoutManager(this);
        rview.setLayoutManager(layoutManager);

        ListAdapter.RecyclerViewClickListener listener = new ListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                clickResponse(position);
            }
        };

        adapter = new MyOfficeAdapter(ListActivity.this,list);
        rview.setAdapter(adapter);
        rview.setHasFixedSize(true);
        init();
        adapter.setOnItemClickListener(new MyOfficeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                clickResponse(position);
            }

            @Override
            public void onItemDeleteClick(int position) {
                delete(position);
            }
        });
    }

    private void delete(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Would you want to delete the office")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Office item = list.get(position);
                        FirebaseDatabase.getInstance().getReference().child("Office").child(item.id).removeValue();
                    }
                }).setNegativeButton("Cancel",null).create().show();
    }

    private void clickResponse(int position) {
        Office item = list.get(position);
        Intent intent = new Intent(this,DetailedActivity.class);
        intent.putExtra("item",item);
        startActivity(intent);
    }

    private void init() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Office");
        mProgressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mProgressDialog.dismiss();
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Office office = snapshot.getValue(Office.class);
                    office.id = (snapshot.getKey());
                   String uid =  FirebaseAuth.getInstance().getCurrentUser().getUid();
                    if (office.uid.equals(uid)){
                        list.add(office);
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