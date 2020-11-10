package com.example.infs3605team3;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.example.infs3605team3.model.Office;
import com.example.infs3605team3.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class BookingActivity extends AppCompatActivity {

    private TextView tv_start;
    private TextView tv_enddata;
    private TextView tvPhone;
    private TextView etDes;
    private Date selectStartDate;
    private Date selectEndData;
    private Office office;
    private EditText tvLastName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tobooking);
        office = (Office) getIntent().getSerializableExtra("item");
        initView();


    }


    private void initView() {

        Toolbar mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle("Account");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_start =  findViewById(R.id.tv_start);
        tv_enddata =  findViewById(R.id.tv_enddata);
        tvLastName =  findViewById(R.id.et_name);
        tvPhone =  findViewById(R.id.et_phone);
        etDes =  findViewById(R.id.et_code);



        findViewById(R.id.tv_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBeginTime();
            }
        });
        findViewById(R.id.tv_enddata). setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectEndTime();
            }
        });
        findViewById(R.id.btn_commit) .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commit();
            }
        });

    }

    private void commit() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String name = tvLastName.getText().toString();
        String phone = tvPhone.getText().toString();
        String message= etDes.getText().toString();
        String endTime= tv_enddata.getText().toString();
        String startTime= tv_start.getText().toString();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("officeId", office.id);
        hashMap.put("phone",phone);
        hashMap.put("message",message);
        hashMap.put("name",name);
        hashMap.put("startTime",startTime);
        hashMap.put("endTime",endTime);
        hashMap.put("officeName",office.name);
        hashMap.put("location",office.country+office.state+office.surburb+office.street);
        hashMap.put("image",office.img);
        hashMap.put("price",office.highDay);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Booking").push().setValue(hashMap);
        databaseReference.child("Experience").push().setValue(hashMap);
        finish();
    }

    private void selectBeginTime() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Calendar selectDate = Calendar.getInstance();
        endDate.set(2030, 1, 1);
        Date date = getDateAfter(new Date(),1);
        startDate.setTime(date);
        if (selectEndData!=null){
            endDate.setTime(selectEndData);
            endDate.add(Calendar.DAY_OF_MONTH, -1);
            if (startDate.getTime().getTime()>endDate.getTime().getTime()){
                Calendar temp = startDate;
                startDate = endDate;
                endDate = temp;
            }
        }
        if (selectStartDate!=null){
            selectDate.setTime(selectStartDate);
        }else{
            selectDate = startDate;
        }
        TimePickerView startPicker = new TimePickerBuilder(BookingActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                selectStartDate = date;
                tv_start.setText(getTime(date));

            }
        }).setDate(selectDate).setRangDate(startDate,endDate).build();
        startPicker.show();
    }
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }
    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        return format.format(date);
    }
    private Date getDate(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(time);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void selectEndTime() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Calendar selectDate = Calendar.getInstance();
        endDate.set(2030, 1, 1);
        if (selectStartDate==null){
            Date date = getDateAfter(new Date(),2);
            startDate.setTime(date);
        }else{
            startDate.setTime(selectStartDate);
            startDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (selectEndData!=null){
            selectDate.setTime(selectEndData);
        }else{
            selectDate = startDate;
        }
        TimePickerView endPicker = new TimePickerBuilder(BookingActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                selectEndData = date;
                tv_enddata.setText(getTime(date));

            }
        }).setDate(selectDate).setRangDate(startDate,endDate).build();
        endPicker.show();
    }

}
