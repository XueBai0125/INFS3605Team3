package com.example.infs3605team3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment  extends Fragment {
    private int[] images = {R.drawable.o1, R.drawable.o2,R.drawable.o3,R.drawable.o4,R.drawable.o5,
            R.drawable.o6,R.drawable.o7,R.drawable.o8};
    private RecyclerView rview;
    RecyclerView.LayoutManager layoutManager;
    ListAdapter adapter;
    View rootview;
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
        rview = rootview.findViewById(R.id.rview);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        rview.setLayoutManager(layoutManager);
        adapter = new ListAdapter(images);
        rview.setAdapter(adapter);
        rview.setHasFixedSize(true);
    }
}
