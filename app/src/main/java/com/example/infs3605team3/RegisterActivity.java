package com.example.infs3605team3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.infs3605team3.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    RadioGroup rbRole;
    EditText etEmail,etPwd;

    DatabaseReference db;

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
        db = FirebaseDatabase.getInstance().getReference();
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
            Toast.makeText(RegisterActivity.this, "Email field must be filled...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(firstName)){
            Toast.makeText(RegisterActivity.this, "Name field must be filled...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(lastName)){
            Toast.makeText(RegisterActivity.this, "Last Name field must be filled...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(role)){
            return;
        }
        if (TextUtils.isEmpty(pwd)){
            Toast.makeText(RegisterActivity.this, "Password field must be filled...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(pwdAgain)){
            Toast.makeText(RegisterActivity.this, "Password field must be filled...", Toast.LENGTH_LONG).show();
            return;
        }
        if (!pwd.equals(pwdAgain)){
            Toast.makeText(RegisterActivity.this, "Password fields are not same...", Toast.LENGTH_LONG).show();
            etPwd.setText("");
            etPwdAgain.setText("");
            return;
        }
        String finalRole = role;
        User user = new User();
        user.setEmail(email.toLowerCase());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(pwd);
        user.setUid(UUID.randomUUID().toString());
        user.setRole(finalRole);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                // Get Post object and use the values to update the UI
                boolean Exists = false;
                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {


                    String emaildata = (String)snapshot.child("email").getValue();

                    if(emaildata.toLowerCase().equals(email.toLowerCase()))
                    {
                        Exists = true;
                        break;
                    }

                }


                if(!Exists)
                {
                    FirebaseDatabase.getInstance().getReference().child(MainActivity.UserKey).child(user.getUid().toLowerCase()).setValue(user);
                    Toast.makeText(RegisterActivity.this, "Register successful!", Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Email already exists...", Toast.LENGTH_LONG).show();
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_LONG).show();
                // ...
            }
        };
        db.child(MainActivity.UserKey).addListenerForSingleValueEvent(postListener);

    }





}
