package com.example.infs3605team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.infs3605team3.model.User;

public class ProfileView extends AppCompatActivity {

    private Button GoBackButton;

    private ImageView ImageData;

    private TextView NameText;
    private TextView EmailText;

    private User UserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        Intent theintent = getIntent();
        UserData = MainActivity.StringToUserData(theintent.getStringExtra(MainActivity.ExtraUserData));
GoBackButton = findViewById(R.id.GoBackButton);
        NameText = this.findViewById(R.id.NameText);
        EmailText = this.findViewById(R.id.EmailTextId);


        if(UserData == null)
        {
            finish();
            return;
        }
        NameText.setText(UserData.getFirstName() + " " + UserData.getLastName());
        String Data = UserData.getEmail();
        if(UserData.getPhoneNumber() != null && !UserData.getPhoneNumber().equals(""))
        {
            Data+= " | " + UserData.getPhoneNumber();
        }
        EmailText.setText(Data);
        ImageData = this.findViewById(R.id.ProfileImage);
        if(UserData.ProfileImageLink != null &&
                !UserData.ProfileImageLink.equals("") )
        {
            ImageLoad(ImageData,UserData.ProfileImageLink);
        }

        GoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    public void ImageLoad(ImageView imageView,String Link)
    {
        Glide.with(this).load(Link).into(imageView);
    }
}