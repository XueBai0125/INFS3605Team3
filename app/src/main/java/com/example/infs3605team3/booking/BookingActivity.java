package com.example.infs3605team3.booking;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.infs3605team3.R;
import com.example.infs3605team3.model.Booking;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

public class BookingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<Booking> houseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(true, 0.2f)
                .init();
        houseList = new ArrayList<>();

        houseList.add(new Booking("Office 1255","Suite 3, Level 1, 195 North Terrace, Adelaide SA 5000","500"));
        houseList.add(new Booking("Office 1345","Suite 3, Level 1, 195 North Terrace, Adelaide SA 5000","800"));
        houseList.add(new Booking("Office 1345","Suite 3, Level 1, 195 North Terrace, Adelaide SA 5000","1200"));

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
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        BookingAdapter adapter = new BookingAdapter(this,houseList);
        recyclerView.setAdapter(adapter);

    }
}