package com.example.infs3605team3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import com.example.infs3605team3.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gyf.immersionbar.ImmersionBar;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private FirebaseAuth mAuth;
    DatabaseReference db;

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
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db = FirebaseDatabase.getInstance().getReference();
            // do your stuff
        } else {
            signInAnonymously();
        }
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(false)
                .init();

    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                db = FirebaseDatabase.getInstance().getReference();
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("Tag", "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    private void updateUserStatus() {
        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            if (!user.isEmailVerified()) {
                new AlertDialog.Builder(this)
                        .setTitle("Tips")
                        .setMessage("Email has not been verified, please send email for verification")
                        .setPositiveButton("Send Email", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                user.sendEmailVerification()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(LoginActivity.this, "Please check your email", Toast.LENGTH_LONG).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this, "Can't send verification email：" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

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
        etEmail.setText("test@gmail.com");
        etPassword.setText("123456");
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
        if(db == null)
            return;
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(email)){
            return;
        }
        if (TextUtils.isEmpty(password)){
            return;
        }


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {


                    String emaildata = (String)snapshot.child("email").getValue();
                    String Passworddata = (String)snapshot.child("password").getValue();

                    if(emaildata.toLowerCase().equals(email.toLowerCase()) &&
                            Passworddata.equals(password))
                    {
                        User post = snapshot.getValue(User.class);
                        MainActivity.LoginedUser = post;
                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getBaseContext(),MainActivity.class);
                        startActivity(i);
                        finish();
                        return;
                    }

                }

                Toast.makeText(LoginActivity.this, "Login failed...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(LoginActivity.this, "Login failed...", Toast.LENGTH_SHORT).show();
                // ...
            }
        };
        db.child(MainActivity.UserKey).addListenerForSingleValueEvent(postListener);

    }
}
