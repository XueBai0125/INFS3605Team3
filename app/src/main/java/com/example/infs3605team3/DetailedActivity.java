package com.example.infs3605team3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.infs3605team3.model.Office;

import org.w3c.dom.Text;

import java.io.Serializable;

public class DetailedActivity extends AppCompatActivity {

    private Office office;
    private TextView tv_country;
    private TextView tv_State;
    private TextView tv_Surburb;
    private TextView tv_Street;
    private TextView tv_Name;
    private TextView sp_bookingtime;
    private TextView tv_des;
    private TextView tv_hhour;
    private TextView tv_hone;
    private TextView tv_lone;
    private CheckBox cb_whiteb;
    private CheckBox cb_wifi;
    private CheckBox cb_tv;
    private CheckBox cb_telephone;
    private CheckBox cb_printer;
    private CheckBox cb_cleanSer;
    private CheckBox cb_24hc;
    private CheckBox cb_reception;
    private CheckBox cb_computer;
    private CheckBox cb_paper;
    private CheckBox cb_Fully_furnished;
    private CheckBox cb_Secure_Access;
    private ImageView iv_img;
    private ProgressDialog dialog;
    private String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Intent intent = getIntent();
        office = (Office) intent.getSerializableExtra("item");
        initView();
        tv_country.setText(office.country+"");
        tv_State.setText(office.state+"");
        tv_Surburb.setText(office.surburb+"");
        tv_Street.setText(office.street+"");
        tv_Name.setText(office.name+"");
        sp_bookingtime.setText(office.bookingTime+"");
        tv_des.setText(office.des+"");
        tv_hhour.setText(office.halfHour+"");
        tv_hone.setText(office.highDay+"");
        tv_lone.setText(office.lowDay+"");
        cb_whiteb.setChecked(office.whiteBoard);
        cb_wifi.setChecked(office.wifi);
        cb_tv.setChecked(office.appleTv);
        cb_telephone.setChecked(office.telephone);
        cb_printer.setChecked(office.printer);
        cb_cleanSer.setChecked(office.cleaningSer);
        cb_24hc.setChecked(office.hourAccess24);
        cb_reception.setChecked(office.reception);
        cb_computer.setChecked(office.computer);
        cb_paper.setChecked(office.paper);
        cb_Fully_furnished.setChecked(office.fullyFurnished);
        cb_Secure_Access.setChecked(office.secureAccess);
        Glide.with(this).load(office.img).into(iv_img);

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


        tv_country = findViewById(R.id.tv_country);
        tv_State = findViewById(R.id.tv_State);
        tv_Surburb = findViewById(R.id.tv_Surburb);
        tv_Street = findViewById(R.id.tv_Street);
        cb_whiteb = findViewById(R.id.cb_whiteb);
        cb_tv = findViewById(R.id.cb_tv);
        cb_wifi = findViewById(R.id.cb_wifi);
        cb_telephone = findViewById(R.id.cb_telephone);
        cb_printer = findViewById(R.id.cb_printer);
        sp_bookingtime = findViewById(R.id.sp_bookingtime);
        cb_cleanSer = findViewById(R.id.cb_cleanSer);
        cb_24hc = findViewById(R.id.cb_24hc);
        cb_reception = findViewById(R.id.cb_reception);
        cb_computer = findViewById(R.id.cb_computer);
        cb_paper = findViewById(R.id.cb_paper);
        cb_Fully_furnished = findViewById(R.id.cb_Fully_furnished);
        cb_Secure_Access = findViewById(R.id.cb_Secure_Access);
        tv_Name = findViewById(R.id.tv_Name);
        tv_des = findViewById(R.id.tv_des);
        iv_img = findViewById(R.id.iv_img);
        tv_hhour = findViewById(R.id.tv_hhour);
        tv_hone = findViewById(R.id.tv_hone);
        tv_lone = findViewById(R.id.tv_lone);



    }
}