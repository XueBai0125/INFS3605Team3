package com.example.infs3605team3;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

public class ListActivity extends AppCompatActivity {

    private int[] images = {R.drawable.o1, R.drawable.o2,R.drawable.o3,R.drawable.o4,R.drawable.o5,
            R.drawable.o6,R.drawable.o7,R.drawable.o8};
    private RecyclerView rview;
    RecyclerView.LayoutManager layoutManager;
    ListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rview = findViewById(R.id.rview);
        layoutManager = new GridLayoutManager(this, 2);
        rview.setLayoutManager(layoutManager);
        adapter = new ListAdapter(images);
        rview.setAdapter(adapter);
        rview.setHasFixedSize(true);
    }
}