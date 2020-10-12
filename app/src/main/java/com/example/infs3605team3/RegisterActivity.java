package com.example.infs3605team3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.infs3605team3.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    TextView tvRole;
    EditText etEmail,etPwd;

    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();


    }

    private void initView() {
        Toolbar mToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etEmail=findViewById(R.id.et_email);
        etName=findViewById(R.id.et_name);
        tvRole =findViewById(R.id.tvRole);
        etPwd=findViewById(R.id.et_pwd);
        findViewById(R.id.btn_sign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUP();
            }

        });
        findViewById(R.id.chooseRole).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseRole();
            }
        });



    }

    private void chooseRole() {
         String[] Roles =new String[] {"Tenant","Homeowner"};
        new AlertDialog.Builder(this)
                .setTitle("Choose role")
                .setSingleChoiceItems(Roles, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        tvRole.setText(Roles[which]);
                    }
                }).create().show();
    }


    private void signUP(){
        String email =etEmail.getText().toString();
        String role = tvRole.getText().toString();
        String name =etName.getText().toString();
        String pwd =etPwd.getText().toString();
        if (TextUtils.isEmpty(email)){
            return;
        }
        if (TextUtils.isEmpty(name)){
            return;
        }
        if (TextUtils.isEmpty(role)){
            return;
        }
        if (TextUtils.isEmpty(pwd)){
            return;
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                    User user = new User();
                    user.setEmail(email);
                    user.setName(name);
                    user.setUid(uid);
                    user.setRole(role);
                    FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uid).setValue(user);
                    finish();
                }
            }

        });




    }





}
