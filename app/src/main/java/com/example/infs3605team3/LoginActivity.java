package com.example.infs3605team3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gyf.immersionbar.ImmersionBar;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        mAuth = FirebaseAuth.getInstance();
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(false)
                .init();

    }

    private void updateUserStatus() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null){
            if (!user.isEmailVerified()) {
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("邮箱尚未验证,请发送邮件认证")
                        .setPositiveButton("发送邮件", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                user.sendEmailVerification()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(LoginActivity.this, "己成功送驗證郵件，請檢查你的電子郵件信箱/Please check your email", Toast.LENGTH_LONG).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this, "無法送出驗證郵件/Can't send verification email：" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

            }else{
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void initView() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        Button  btnsign = findViewById(R.id.btn_login);
        findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        findViewById(R.id.tv_forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(LoginActivity.this, ForgetPwdActivity.class));
            }
        });
    }

    private void signIn() {
        String email = etEmail.getText().toString();
        String pwd = etPassword.getText().toString();
        if (TextUtils.isEmpty(email)){
            return;
        }
        if (TextUtils.isEmpty(pwd)){
            return;
        }
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        updateUserStatus();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Login Fail:" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
