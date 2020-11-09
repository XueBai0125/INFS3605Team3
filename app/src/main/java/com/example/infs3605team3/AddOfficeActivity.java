package com.example.infs3605team3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddOfficeActivity extends AppCompatActivity {
    String bookingTime;
    private EditText tv_country;
    private EditText tv_State;
    private EditText tv_Surburb;
    private EditText tv_Street;
    private EditText tv_Name;
    private EditText tv_des;
    private EditText tv_hhour;
    private EditText tv_hone;
    private EditText tv_lone;
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
    String[] permissions = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();

    private void submit() {


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String country = tv_country.getText().toString();
        String state = tv_State.getText().toString();
        String surburb= tv_Surburb.getText().toString();
        String street= tv_Street.getText().toString();
        String name= tv_Name.getText().toString();
        String des= tv_des.getText().toString();
        String halfHour= tv_hhour.getText().toString();
        String highDay= tv_hone.getText().toString();
        String lowDay= tv_lone.getText().toString();

        HashMap<String, Object> hashMap = new HashMap<>();
        if (cb_whiteb.isChecked()){
            hashMap.put("whiteBoard", true);
        }else{
            hashMap.put("whiteBoard", false);
        }
        if (cb_tv.isChecked()){
            hashMap.put("appleTv", true);
        }else{
            hashMap.put("appleTv", false);
        }
        if (cb_telephone.isChecked()){
            hashMap.put("telephone", true);
        }else{
            hashMap.put("telephone", false);
        }
        if (cb_printer.isChecked()){
            hashMap.put("printer", true);
        }else{
            hashMap.put("printer", false);
        }
        if (cb_wifi.isChecked()){
            hashMap.put("wifi", true);
        }else{
            hashMap.put("wifi", false);
        }
        if (cb_24hc.isChecked()){
            hashMap.put("hourAccess24", true);
        }else{
            hashMap.put("hourAccess24", false);
        }
        if (cb_cleanSer.isChecked()){
            hashMap.put("cleaningSer", true);
        }else{
            hashMap.put("cleaningSer", false);
        }
        if (cb_reception.isChecked()){
            hashMap.put("reception", true);
        }else{
            hashMap.put("reception", false);
        }
        if (cb_computer.isChecked()){
            hashMap.put("computer", true);
        }else{
            hashMap.put("computer", false);
        }
        if (cb_paper.isChecked()){
            hashMap.put("paper", true);
        }else{
            hashMap.put("paper", false);
        }
        if (cb_Fully_furnished.isChecked()){
            hashMap.put("fullyFurnished", true);
        }else{
            hashMap.put("fullyFurnished", false);
        }
        if (cb_Secure_Access.isChecked()){
            hashMap.put("secureAccess", true);
        }else{
            hashMap.put("secureAccess", false);
        }
        hashMap.put("uid", uid);
        hashMap.put("country", country);
        hashMap.put("state",state);
        hashMap.put("surburb",surburb);
        hashMap.put("img",imageUrl);
        hashMap.put("street",street);
        hashMap.put("name",name);
        hashMap.put("des",des);
        hashMap.put("bookingTime",bookingTime);
        hashMap.put("halfHour",halfHour);
        hashMap.put("highDay",highDay);
        hashMap.put("lowDay",lowDay);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Office").push().setValue(hashMap);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(true, 0.2f)
                .init();
        setContentView(R.layout.activity_addoffice);
        dialog = new ProgressDialog(this);
        initView();
    }
    private void initView() {

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("Add New Office");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.sp_bookingtime);
        String[] mItems = {"All date", "All date", "All date", "All date"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                bookingTime= mItems[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPermissionList.clear();
                for (int i = 0; i < permissions.length; i++) {
                    if (ContextCompat.checkSelfPermission(AddOfficeActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                        mPermissionList.add(permissions[i]);
                    }
                }
                if (mPermissionList.isEmpty()) {
                   getImage();
                } else {
                    String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
                    ActivityCompat.requestPermissions(AddOfficeActivity.this, permissions, 101);
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 101){
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(AddOfficeActivity.this, permissions[i]);
                    if (showRequestPermission) {
                        Toast.makeText(AddOfficeActivity.this,"Permission not requested",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void getImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 99);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case 99:
                    final Uri selectedUri = data.getData();
                    upload(selectedUri);
                    break;

                default:
                    break;
            }
        }

    }
    private void upload(Uri selectedUri) {
        dialog.show();
        StorageReference mStoreReference = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = mStoreReference.child(System.currentTimeMillis()+".jpg");
        UploadTask uploadTask = riversRef.putFile(selectedUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                dialog.dismiss();
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();
                    RequestOptions options = new RequestOptions()
                            .placeholder(new ColorDrawable(Color.parseColor("#666666")))
                            .fallback(new ColorDrawable(Color.parseColor("#666666")))
                            .error(new ColorDrawable(Color.parseColor("#666666")));
                    Glide.with(AddOfficeActivity.this).load(imageUrl).apply(options).into(iv_img);
                }
            }
        });
    }


}