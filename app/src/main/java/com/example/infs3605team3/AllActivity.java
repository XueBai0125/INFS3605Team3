package com.example.infs3605team3;

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

import com.example.infs3605team3.model.Office;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

public class AllActivity extends AppCompatActivity {


    private RecyclerView rview;
    RecyclerView.LayoutManager layoutManager;
    OfficeAdapter adapter;
    private ArrayList<Office> list = new ArrayList();
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle("All Office");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImmersionBar.with(this)
                .titleBar(mToolbar)
                .transparentStatusBar()
                .statusBarDarkFont(true, 0.2f)
                .init();
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

        adapter = new OfficeAdapter(AllActivity.this,list);
        rview.setAdapter(adapter);
        rview.setHasFixedSize(true);
        init();
        adapter.setOnItemClickListener(new OfficeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                clickResponse(position);
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
                    list.add(office);




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