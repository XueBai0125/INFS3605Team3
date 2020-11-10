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

import com.bumptech.glide.Glide;
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
import com.gyf.immersionbar.ImmersionBar;


public class InfosActivity extends AppCompatActivity {

    private TextView tvEmail;
    private DatabaseReference reference;
    private TextView tvName;
    private TextView tvLastName;
    private TextView tvPhone;
    private TextView tvAccount;
    private StorageReference mStoreReference;
    private ProgressDialog dialog;
    private ImageView ivAvert;
    private User model;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitity_info);
        dialog = new ProgressDialog(this);
        initView();
        model = (User) getIntent().getSerializableExtra("model");
        refreshUI(model);
        if (!TextUtils.isEmpty(model.getImages())){
            model.setImages("");
        }
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user.getUid());


    }

    private void refreshUI(User user){
        if (user !=null){
            tvEmail.setText(user.getEmail());
            tvName.setText(user.getFirstName());
            tvLastName.setText(user.getLastName());
            tvPhone.setText(user.getPhoneNumber());
            tvAccount.setText(user.getRole());

            if (!TextUtils.isEmpty(user.getImages())){
                Glide.with(InfosActivity.this).load(user.getImages()).into(ivAvert);
            }

        }
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
        ImmersionBar.with(this)
                .titleBar(mToolbar)
                .transparentStatusBar()
                .statusBarDarkFont(true, 0.2f)
                .init();
         tvEmail =  findViewById(R.id.tv_email);
        tvName =  findViewById(R.id.tv_name);
        tvLastName =  findViewById(R.id.tv_lastname);
        tvPhone =  findViewById(R.id.tv_phone);
        tvAccount =  findViewById(R.id.tv_account);
        LinearLayout  rlNickName =  findViewById(R.id.rl_nickName);
        LinearLayout rlPhone =  findViewById(R.id.rl_phone);
        ivAvert =  findViewById(R.id.iv_avert);



      findViewById(R.id.layout_head).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              changeImage();
          }
      });
        findViewById(R.id.rl_lastname).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLastName();
            }
        });
        rlNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeName();
            }
        });
        rlPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePhone();

            }
        });
    }

    private void changeName() {
        EditText editText =  new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Name")
                .setView(editText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = editText.getText().toString();
                        model.setFirstName(name);
                        reference.setValue(model);
                        dialogInterface.dismiss();
                        tvName.setText(name);
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
dialogInterface.dismiss();
            }
        }).create().show();
    }
    private void changeLastName() {
        EditText editText =  new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Name")
                .setView(editText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = editText.getText().toString();
                        model.setLastName(name);
                        reference.setValue(model);
                        dialogInterface.dismiss();
                        tvLastName.setText(name);
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }
    private void changePhone() {
        EditText editText =  new EditText(this);
        editText.setText(model.getPhoneNumber()+"");
        new AlertDialog.Builder(this)
                .setTitle("Phone Number")
                .setView(editText)
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String phone = editText.getText().toString();
                        model.setPhoneNumber(phone);
                        dialogInterface.dismiss();
                        tvPhone.setText(phone);
                        reference.setValue(model, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            }
                        });
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    private void changeImage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, 100);
            }
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {
            showPermissionDialog();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (permissions.length == 1 && grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 100);
                }
            } else {
                showPermissionDialog();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void showPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Tips")
                .setMessage("Permission denied")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(InfosActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                100);
                    }
                }).create().show();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case 100:
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
        mStoreReference=  FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = mStoreReference.child(System.currentTimeMillis()+".jpg");
        UploadTask uploadTask = riversRef.putFile(selectedUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                    String imageUrl = downloadUri.toString();
                    model.setImages(imageUrl);
                    reference.setValue(model);

                    Glide.with(InfosActivity.this).load(imageUrl).into(ivAvert);
                }
            }
        });
    }


}
