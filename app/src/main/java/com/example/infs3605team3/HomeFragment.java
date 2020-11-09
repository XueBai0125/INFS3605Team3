package com.example.infs3605team3;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs3605team3.model.Office;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment  extends Fragment {

    private RecyclerView rview;
    RecyclerView.LayoutManager layoutManager;
    ListAdapter adapter;
    View rootview;
    private ProgressDialog mProgressDialog;
    private ArrayList<Office> list = new ArrayList();

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_home,container,false);
        return rootview;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressDialog =  new ProgressDialog(getActivity());
        rview = rootview.findViewById(R.id.rview);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        rview.setLayoutManager(layoutManager);
        adapter = new ListAdapter(getActivity(),list,new ListAdapter.RecyclerViewClickListener(){

            @Override
            public void onClick(View view, int position) {

            }


        });
        rview.setAdapter(adapter);
        rview.setHasFixedSize(true);
        init();
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
