package com.example.infs3605team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Intent intent = getIntent();
        int [] images = intent.getIntArrayExtra("image");
        int pos = intent.getIntExtra("position", 0);

        ImageView imageView = findViewById(R.id.imageView2);
        imageView.setImageResource(images[pos]);
        TextView text = findViewById(R.id.textView5);
        text.setText("Office " + (pos+1));
    }
}