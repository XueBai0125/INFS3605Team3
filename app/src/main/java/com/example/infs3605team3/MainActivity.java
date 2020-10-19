package com.example.infs3605team3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private Fragment mAppFragment;
    private Fragment mBookingFragment;
    private Fragment mMineFragment;
    private Fragment mChatFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    protected void initView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        if (mAppFragment == null) {
            mAppFragment = HomeFragment.newInstance();
        }
        if (mBookingFragment == null) {
            mBookingFragment = BookingFragment.newInstance();
        }

        if (mChatFragment == null) {
            mChatFragment = ChatFragment.newInstance();
        }
        if (mMineFragment == null) {
            mMineFragment = MineFragment.newInstance();
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_home) {
                    switchTab(mAppFragment);
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_list) {
                    switchTab(mBookingFragment);
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_chat) {
                    switchTab(mChatFragment);
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_profile) {
                    switchTab(mMineFragment);
                    return true;
                }
                return false;
            }
        });


        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(0).getItemId());

    }


    private void switchTab(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }


}