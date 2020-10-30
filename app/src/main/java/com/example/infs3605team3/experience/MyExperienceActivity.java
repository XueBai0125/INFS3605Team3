package com.example.infs3605team3.experience;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs3605team3.R;
import com.example.infs3605team3.model.Wish;
import com.example.infs3605team3.wishlist.WishAdapter;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

public class MyExperienceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<Wish> houseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        houseList = new ArrayList<>();

        houseList.add(new Wish());
        houseList.add(new Wish());
        houseList.add(new Wish());
        houseList.add(new Wish());
        Toolbar mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle("My Experience");
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

        ExperienceAdapter adapter = new ExperienceAdapter(this,houseList);
        recyclerView.setAdapter(adapter);

    }
}