package com.example.infs3605team3;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetPwdActivity extends AppCompatActivity {
    private EditText etEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initView();

    }


    private void initView() {
        Toolbar mToolbar=findViewById(R.id.toolbar);
        mToolbar.setTitle("Forget password");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etEmail = findViewById(R.id.et_email);

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forget();
            }
        });
    }

    private void forget() {
        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)){
            return;
        }

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(email);


    }
}
