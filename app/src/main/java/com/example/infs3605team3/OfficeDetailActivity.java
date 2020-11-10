package com.example.infs3605team3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.infs3605team3.model.Booking;
import com.example.infs3605team3.model.Office;
import com.example.infs3605team3.view.FlowLayout;
import com.example.infs3605team3.view.FlowerAttrView;

import java.util.ArrayList;
import java.util.List;

public class OfficeDetailActivity extends AppCompatActivity {

    private Office office;

    private ImageView viewpager_banner;
    private FlowLayout flowlayout;
    private TextView tv_price;
    private TextView tv_name;
    private TextView tv_location;
    private TextView tv_time;
    private TextView tv_des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officedetailed);
        Intent intent = getIntent();
        office = (Office) intent.getSerializableExtra("item");
        initView();
        tv_price.setText(office.highDay+"");
        tv_name.setText(office.name+"");
        tv_des.setText(office.des+"");
        tv_location.setText(office.country+office.state+office.surburb+office.street);
        tv_time.setText(office.startTime+"   ~   " + office.endTime);
        Glide.with(this).load(office.img).into(viewpager_banner);
        flowlayout.removeAllViews();
        List<String> attrs = new ArrayList<>();
        if (office.whiteBoard){
            attrs.add("White board");
        }
        if (office.wifi){
            attrs.add("WIFI");
        }
        if (office.appleTv){
            attrs.add("Apple Tv");
        }
        if (office.telephone){
            attrs.add("Telephone");
        }
        if (office.printer){
            attrs.add("Printer");
        }
        if (office.cleaningSer){
            attrs.add("Cleaning Service");
        }
        if (office.hourAccess24){
            attrs.add("24 Hours Access");
        }
        if (office.reception){
            attrs.add("Reception");
        }
        if (office.computer){
            attrs.add("Computer");
        }
        if (office.paper){
            attrs.add("Paper");
        }
        if (office.fullyFurnished){
            attrs.add("Fully Furnished");
        }
        if (office.secureAccess){
            attrs.add("Secure Access");
        }


        if (attrs != null && attrs.size() > 0) {
            for (int i = 0; i < attrs.size(); i++) {
                FlowerAttrView itemView = new FlowerAttrView(this);
                itemView.setAttributeValue(attrs.get(i));
                itemView.setLayoutParams(new FlowLayout.LayoutParams(
                        FlowLayout.LayoutParams.WRAP_CONTENT,
                        dp2px(OfficeDetailActivity.this, 16)));
                flowlayout.addView(itemView);
            }
        }
    }
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = getScreenDensity(context);
        return (int) (dipValue * scale + 0.5);
    }
    private void initView() {

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("Office Detail");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        flowlayout = findViewById(R.id.flowlayout);
        flowlayout.setMinimumHeight(dp2px(this, 16));
        flowlayout.setChildSpacing(dp2px(this, 4));
        flowlayout.setRowSpacing(dp2px(this, 6));
        viewpager_banner = findViewById(R.id.viewpager_banner);

        tv_price = findViewById(R.id.tv_price);
        tv_name = findViewById(R.id.tv_name);
        tv_location = findViewById(R.id.tv_location);
        tv_time = findViewById(R.id.tv_time);
        tv_des = findViewById(R.id.tv_des);

        findViewById(R.id.bt_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OfficeDetailActivity.this, BookingActivity.class);
                intent.putExtra("item",office);
                startActivity(intent);
            }
        });


    }
}