package com.example.infs3605team3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
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
    RadioGroup rbRole;
    EditText etEmail,etPwd;

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etPwdAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        //


    }

    private void initView() {
        Toolbar mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle("Create a new account");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etEmail=findViewById(R.id.et_email);
        etFirstName=findViewById(R.id.et_firstname);
        etLastName=findViewById(R.id.et_lastname);
        rbRole =findViewById(R.id.chooseRole);
        etPwd=findViewById(R.id.et_pwd);
        etPwdAgain=findViewById(R.id.et_pwd_again);
        findViewById(R.id.btn_sign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUP();
            }

        });

    }




    private void signUP(){
        String email =etEmail.getText().toString();
        String role = "tenant";
        if (rbRole.getCheckedRadioButtonId()!=R.id.rb_tenant){
            role="Property Owner";
        }
        String firstName =etFirstName.getText().toString();
        String lastName =etLastName.getText().toString();
        String pwd =etPwd.getText().toString();
        String pwdAgain =etPwdAgain.getText().toString();
        if (TextUtils.isEmpty(email)){
            return;
        }
        if (TextUtils.isEmpty(firstName)){
            return;
        }
        if (TextUtils.isEmpty(lastName)){
            return;
        }
        if (TextUtils.isEmpty(role)){
            return;
        }
        if (TextUtils.isEmpty(pwd)){
            return;
        }
        if (TextUtils.isEmpty(pwdAgain)){
            return;
        }
        if (!pwd.equals(pwdAgain)){
            etPwd.setText("");
            etPwdAgain.setText("");
            return;
        }
        String finalRole = role;
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                    User user = new User();
                    user.setEmail(email);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setUid(uid);
                    user.setRole(finalRole);
                    FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uid).setValue(user);
                    finish();
                }
            }

        });




    }





}
