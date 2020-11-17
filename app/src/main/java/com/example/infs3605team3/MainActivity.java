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

import com.example.infs3605team3.model.BookingData;
import com.example.infs3605team3.model.BookingFilter;
import com.example.infs3605team3.model.OfficeData;
import com.example.infs3605team3.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;

public class MainActivity extends AppCompatActivity {

    public  static String ExtraUserData = "com.application.example.UserData";
    public  static String ExtraOfficeData = "com.application.example.OfficeData";
    public  static String ExtraBookingData = "com.application.example.BookingDataExtra";
    public  static String ExtraIsForBookingDataKey = "com.application.example.IsForBookingData";
    BottomNavigationView bottomNavigationView;
    private Fragment mAppFragment;
    private Fragment mMineFragment;
    private Fragment mChatFragment;

    public static User LoginedUser;
    public  static BookingFilter Filter;

    public  static String UserKey ="UserInfo";
    public  static String OfficeDataKey ="OfficeData";
    public static String BookingDBKey = "Booking";
    public static String WishListKey = "Wishlist";
    public  static String OfficeKey = "OfficeImages";
    public  static String ProfileImagesKey = "ProfileImages";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(true, 0.2f)
                .init();
        initView();
    }


    protected void initView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        if (mAppFragment == null) {
            mAppFragment = HomeFragment.newInstance();
        }


        if (mChatFragment == null) {
            mChatFragment = RentOrSellActivity.newInstance();
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
                } else if (menuItem.getItemId() == R.id.nav_rent) {
                    switchTab(mChatFragment);
                    return true;
                }else if (menuItem.getItemId() == R.id.nav_profile) {
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


    public  static String UserDataToString(User data){
        if(data == null)
            return "";
        Gson g = new Gson();
        String str = g.toJson(data);
        return str;
    }
    public  static User StringToUserData(String s){
        if(s == null || s.equals(""))
            return null;

        Gson g = new Gson();
        User o = g.fromJson(s, User.class);
        return o;

    }

    public  static String OfficeDataToString(OfficeData data){
        if(data == null)
            return "";
        Gson g = new Gson();
        String str = g.toJson(data);
        return str;
    }
    public  static OfficeData StringToOfficeData(String s){
        if(s == null || s.equals(""))
            return null;

        Gson g = new Gson();
        OfficeData o = g.fromJson(s, OfficeData.class);
        return o;

    }
    public  static String BookingDataToString(BookingData data){
        if(data == null)
            return "";
        Gson g = new Gson();
        String str = g.toJson(data);
        return str;
    }
    public  static BookingData StringToBookingData(String s){
        if(s == null || s.equals(""))
            return null;

        Gson g = new Gson();
        BookingData o = g.fromJson(s, BookingData.class);
        return o;

    }


}