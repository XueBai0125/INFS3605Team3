package com.example.infs3605team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.infs3605team3.model.BookingData;
import com.example.infs3605team3.model.OfficeData;
import com.example.infs3605team3.model.User;
import com.example.infs3605team3.model.Wish;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class OfficeDetail extends AppCompatActivity {

    OfficeData officedata;
    BookingData bookingdata;

    ImageView TheImage;
    TextView TitleText;
    TextView DescriptionText;
    TextView PriceText;
    TextView CapacityText;
    TextView M2Text;
    TextView CountryText;
    TextView AddressText;


    public CheckBox IsThereInternetCheckbox;
    public CheckBox IsTherePrinterCheckbox;
    public CheckBox IsThereScannerCheckbox;
    public CheckBox IsThereParkingCheckbox;
    public CheckBox IsThereSecurityCheckbox;
    public CheckBox IsThere24HrAccessCheckbox;
    public CheckBox IsThereKitchenCheckbox;
    public CheckBox IsThereProjectorCheckbox;

    TextView OwnerText;
    EditText StartDateDay;
    EditText StartDateMonth;
    EditText StartDateYear;
    EditText EndDateDay;
    EditText EndDateMonth;
    EditText EndDateYear;
    TextView EndDateText;
    TextView StartDateText;
    Button BookAReservationButton;
    Button AddToWishListButton;
    Button GoBackButton;

    Button RateButton;

    ArrayList<Button> Stars;

    boolean ReadyToBook = false;
    boolean ReadyToAddWishlist = false;
    ArrayList<BookingData> Bookings;
    ArrayList<Wish> MyWishList;
    ArrayList<Wish> AllWishList;


    DatabaseReference db;

    private boolean IsPressedStartDate;
    private boolean IsMine = false;

    private int CurrentRate = 5;
    private User OwnerData;
    private Context ThisContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_office_detail);
        Intent theintent = getIntent();
        ThisContext = this;
        officedata = MainActivity.StringToOfficeData(theintent.getStringExtra(MainActivity.ExtraOfficeData));
        String BookingDataString = theintent.getStringExtra(MainActivity.ExtraBookingData);

        if(BookingDataString != null && !BookingDataString.equals(""))
        {
            bookingdata = MainActivity.StringToBookingData(BookingDataString);
        }
        if(officedata == null)
        {
            GoMainPage();
            return;
        }


        RateButton = (Button)findViewById(R.id.FilteringButton);
        Stars = new ArrayList<Button>();
        Stars.add((Button) findViewById(R.id.Star1));
        Stars.add((Button) findViewById(R.id.Star2));
        Stars.add((Button) findViewById(R.id.Star3));
        Stars.add((Button) findViewById(R.id.Star4));
        Stars.add((Button) findViewById(R.id.Star5));


        Stars.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentRate = 1;
                SetStarImagesAccordingToGivenStars();
            }
        });
        Stars.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentRate = 2;
                SetStarImagesAccordingToGivenStars();
            }
        });
        Stars.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentRate = 3;
                SetStarImagesAccordingToGivenStars();
            }
        });
        Stars.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentRate = 4;
                SetStarImagesAccordingToGivenStars();
            }
        });
        Stars.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentRate = 5;
                SetStarImagesAccordingToGivenStars();
            }
        });

        TheImage = (ImageView)findViewById(R.id.imageView4);
        TitleText = (TextView)findViewById(R.id.TitleText);
        DescriptionText = (TextView)findViewById(R.id.DescriptionText);
        PriceText = (TextView)findViewById(R.id.PriceText);
        CapacityText = (TextView)findViewById(R.id.CapacityText);
        M2Text = (TextView)findViewById(R.id.M2Text);
        CountryText = (TextView)findViewById(R.id.CountryData);
        AddressText = (TextView)findViewById(R.id.OpenAddressText);

        IsThereInternetCheckbox = (CheckBox)findViewById(R.id.IsThereInternet);
        IsTherePrinterCheckbox = (CheckBox) findViewById(R.id.IsTherePrinter);
        IsThereScannerCheckbox = (CheckBox) findViewById(R.id.IsThereScanner);
        IsThereSecurityCheckbox = (CheckBox) findViewById(R.id.Security);
        IsThereParkingCheckbox = (CheckBox) findViewById(R.id.Parking);
        IsThere24HrAccessCheckbox = (CheckBox) findViewById(R.id.IsThere24HourAccess);
        IsThereProjectorCheckbox = (CheckBox) findViewById(R.id.Projector);
        IsThereKitchenCheckbox = (CheckBox) findViewById(R.id.IsThereKitchen);



        OwnerText = (TextView)findViewById(R.id.OwnerText);
        StartDateDay = (EditText)findViewById(R.id.StartDateDay);
        StartDateMonth = (EditText)findViewById(R.id.StartDateMonth);
        StartDateYear = (EditText)findViewById(R.id.StartDateYear);
        EndDateDay = (EditText)findViewById(R.id.EndDateDay);
        EndDateMonth = (EditText)findViewById(R.id.EndDateMonth);
        EndDateYear = (EditText)findViewById(R.id.EndDateYear);

        StartDateText = (TextView) findViewById(R.id.StartDateText);
        EndDateText = (TextView) findViewById(R.id.EndDateText);

        AddToWishListButton = (Button) findViewById(R.id.AddToWishListButton);
        BookAReservationButton = (Button) findViewById(R.id.BookAReservationButton);
        GoBackButton = (Button) findViewById(R.id.GoBackButton);


        boolean IsEditable = false;


        Date currentdate = new Date(System.currentTimeMillis());
        Date nowdate = new Date(System.currentTimeMillis()+ (1000 * 60 * 60 * 24));
        Date tomorrowdate = new Date(System.currentTimeMillis()+ ((1000 * 60 * 60 * 24)*2));

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(nowdate);
        int year = calendar.get(Calendar.YEAR);
//Add one to month {0 - 11}
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        StartDateDay.setEnabled(false);
        StartDateMonth.setEnabled(false);
        StartDateYear.setEnabled(false);
        EndDateDay.setEnabled(false);
        EndDateMonth.setEnabled(false);
        EndDateYear.setEnabled(false);
        if(IsForCancellation())
        {
            BookAReservationButton.setText("CANCEL BOOKING");
            calendar.setTime(bookingdata.StartDate);
            year = calendar.get(Calendar.YEAR);
//Add one to month {0 - 11}
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        else if(IsForRating())
        {
            BookAReservationButton.setText("RATE THIS LOCATION");
            calendar.setTime(bookingdata.StartDate);
            year = calendar.get(Calendar.YEAR);
//Add one to month {0 - 11}
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        else
        {
            StartDateDay.setEnabled(true);
            StartDateMonth.setEnabled(true);
            StartDateYear.setEnabled(true);
            EndDateDay.setEnabled(true);
            EndDateMonth.setEnabled(true);
            EndDateYear.setEnabled(true);
        }

        StartDateDay.setText(String.valueOf(day));
        StartDateMonth.setText(String.valueOf(month));
        StartDateYear.setText(String.valueOf(year));

        calendar.setTime(tomorrowdate);
        year = calendar.get(Calendar.YEAR);
//Add one to month {0 - 11}
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        if(IsForCancellation() || IsForRating())
        {
            calendar.setTime(bookingdata.EndDate);
            year = calendar.get(Calendar.YEAR);
//Add one to month {0 - 11}
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        EndDateDay.setText(String.valueOf(day));
        EndDateMonth.setText(String.valueOf(month));
        EndDateYear.setText(String.valueOf(year));


        ImageLoad(TheImage,officedata.ImageLink);

        TitleText.setText(officedata.Name);
        DescriptionText.setText("Description : " + officedata.Name);
        PriceText.setText("Price : " + String.valueOf(officedata.Price) + "$");
        CapacityText.setText("Capacity : " + String.valueOf(officedata.Capacity));
        M2Text.setText("Size : " + String.valueOf(officedata.M2)+ "m2");
        CountryText.setText("Location : " + officedata.Country + "/" + officedata.Province + "/" + officedata.District);
        AddressText.setText("Address : " + officedata.Address);


        IsThereInternetCheckbox.setChecked(officedata.Internet==1?true:false);
        IsTherePrinterCheckbox.setChecked(officedata.Printer==1?true:false);
        IsThereScannerCheckbox.setChecked(officedata.Scanner==1?true:false);
        IsThereSecurityCheckbox.setChecked(officedata.Security==1?true:false);
        IsThereParkingCheckbox.setChecked(officedata.Parking==1?true:false);
        IsThere24HrAccessCheckbox.setChecked(officedata.Hr24Access==1?true:false);
        IsThereProjectorCheckbox.setChecked(officedata.Projector==1?true:false);
        IsThereKitchenCheckbox.setChecked(officedata.Kitchen==1?true:false);

        IsThereInternetCheckbox.setEnabled(IsEditable);
        IsTherePrinterCheckbox.setEnabled(IsEditable);
        IsThereScannerCheckbox.setEnabled(IsEditable);
        IsThereSecurityCheckbox.setEnabled(IsEditable);
        IsThereParkingCheckbox.setEnabled(IsEditable);
        IsThere24HrAccessCheckbox.setEnabled(IsEditable);
        IsThereProjectorCheckbox.setEnabled(IsEditable);
        IsThereKitchenCheckbox.setEnabled(IsEditable);


        HideDisplayRateActions(false);
        SetStarImagesAccordingToGivenStars();
        OwnerText.setText("Owner : " + officedata.OwnerName);
        GetRelatedBookings();
        GetMyWishList();
        BookAReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ReadyToAddWishlist)
                    return;
                if(!ReadyToBook)
                    return;
                if(IsMine)
                {
                    RemovePost();
                }
                else if(IsForCancellation()){
                    OnClickCancelReservation();
                }
                else if(IsForRating()){
                    HideDisplayRateActions(true);
                    SetStarImagesAccordingToGivenStars();
                }
                else{
                    OnClickBookAReservation();
                }
            }
        });

        AddToWishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnAddWishListButtonClick();
            }
        });

        GoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoMainPage();
            }
        });

        RateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IsForRating())
                {
                    bookingdata.IsRated = 1;
                    bookingdata.Rate = CurrentRate;
                    db.child(MainActivity.BookingDBKey).child(bookingdata.Id.toLowerCase().toLowerCase()).setValue(bookingdata);
                    CalculateAndUpdateRateOfOffice(0,true);
                }
            }
        });



        if(officedata.OwnerId.toLowerCase().equals(MainActivity.LoginedUser.getUid().toLowerCase())){
            IsMine = true;
            BookAReservationButton.setText("Remove Post");
            BookAReservationButton.setEnabled(true);
            AddToWishListButton.setEnabled(false);
            StartDateDay.setEnabled(false);
            StartDateMonth.setEnabled(false);
            StartDateYear.setEnabled(false);
            EndDateDay.setEnabled(false);
            EndDateMonth.setEnabled(false);
            EndDateYear.setEnabled(false);
            StartDateText.setEnabled(false);
            EndDateText.setEnabled(false);


            BookAReservationButton.setVisibility(View.VISIBLE);
            AddToWishListButton.setVisibility(View.GONE);
            StartDateDay.setVisibility(View.GONE);
            StartDateMonth.setVisibility(View.GONE);
            StartDateYear.setVisibility(View.GONE);
            EndDateDay.setVisibility(View.GONE);
            EndDateMonth.setVisibility(View.GONE);
            EndDateYear.setVisibility(View.GONE);
            StartDateText.setVisibility(View.GONE);
            EndDateText.setVisibility(View.GONE);

        }
        else
        {
            GetOwnerData();
            OwnerText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OpenOwnerDetail();
                }
            });
        }
    }

    public  void RemovePost(){
        Date nowdate = new Date(System.currentTimeMillis());
        for(int x=0;x < Bookings.size();x++){
            if(Bookings.get(x).EndDate.after(nowdate))
            {
                Toast.makeText(this,"You cant remove this post as there are users who rented this location",Toast.LENGTH_SHORT);
                return;
            }
        }

        for(int x=0;x < AllWishList.size();x++)
        {
            db.child(MainActivity.WishListKey).child(AllWishList.get(x).Id.toLowerCase().toString()).removeValue();
        }

        db.child(MainActivity.OfficeDataKey).child(officedata.Id.toLowerCase().toString()).removeValue();
        Toast.makeText(this,"Post removed successfully!",Toast.LENGTH_SHORT);
        GoMainPage();

    }

    private void HideDisplayRateActions(boolean IsDisplayingRate){
        RateButton.setEnabled(IsDisplayingRate);
        RateButton.setVisibility(IsDisplayingRate?View.VISIBLE:View.GONE);

        for(int x=0;x < Stars.size();x++)
        {
            Stars.get(x).setEnabled(IsDisplayingRate);
            Stars.get(x).setVisibility(IsDisplayingRate?View.VISIBLE:View.GONE);
        }

        BookAReservationButton.setEnabled(!IsDisplayingRate);
        BookAReservationButton.setVisibility(IsDisplayingRate?View.GONE:View.VISIBLE);
        AddToWishListButton.setEnabled(!IsDisplayingRate);
        AddToWishListButton.setVisibility(IsDisplayingRate?View.GONE:View.VISIBLE);
        GoBackButton.setEnabled(!IsDisplayingRate);
        GoBackButton.setVisibility(IsDisplayingRate?View.GONE:View.VISIBLE);
    }

    public  void SetStarImagesAccordingToGivenStars()
    {
        Drawable on = this.getResources().getDrawable(R.drawable.comment_ratingbar_on);
        Drawable off = this.getResources().getDrawable(R.drawable.comment_ratingbar_off);
        for(int x=0;x < Stars.size();x++)
        {
            if(CurrentRate >= x+1)
            {
                Log.d("star","setting on");
                Stars.get(x).setBackground(on);
            }
            else
            {
                Log.d("star","setting off");
                Stars.get(x).setBackground(off);
            }
        }
    }

    private boolean IsForCancellation(){
        Date currentdate = new Date(System.currentTimeMillis());
        return bookingdata != null && bookingdata.StartDate != null && bookingdata.StartDate.after(currentdate);
    }
    private boolean IsForRating(){
        Date currentdate = new Date(System.currentTimeMillis());
        return bookingdata != null && bookingdata.EndDate != null && bookingdata.EndDate.before(currentdate) && bookingdata.IsRated == 0;
    }

    public  void OnClickBookAReservation(){
        if(!ReadyToBook){
            return;
        }

        String StartDateDayString = StartDateDay.getText().toString();
        String StartDateMonthString = StartDateMonth.getText().toString();
        String StartDateYearString = StartDateYear.getText().toString();
        String EndDateDayString = EndDateDay.getText().toString();
        String EndDateMonthString = EndDateMonth.getText().toString();
        String EndDateYearString = EndDateYear.getText().toString();
        int StartDay = Integer.valueOf(StartDateDayString);
        int StartMonth = Integer.valueOf(StartDateMonthString);
        int EndDay = Integer.valueOf(EndDateDayString);
        int EndMonth = Integer.valueOf(EndDateMonthString);

        if(StartDay > 31 || StartDay < 1 ||
                StartMonth > 12 || StartMonth < 1 ||
                EndDay > 31 || EndDay < 1 ||
                EndMonth > 12 || EndMonth < 1
        )
        {
            Toast.makeText(this, "There is something wrong with the dates...", Toast.LENGTH_SHORT).show();
            return;
        }


        if(StartDateDayString == null || StartDateDayString.equals("") ||
                StartDateMonthString == null || StartDateMonthString.equals("") ||
                StartDateYearString == null || StartDateYearString.equals("")){
            Toast.makeText(this, "You should choose start date for making a reservation...", Toast.LENGTH_SHORT).show();
            return;
        }

        if(EndDateDayString == null || EndDateDayString.equals("") ||
                EndDateMonthString == null || EndDateMonthString.equals("") ||
                EndDateYearString == null || EndDateYearString.equals("")) {
            Toast.makeText(this, "You should choose end date for making a reservation...", Toast.LENGTH_SHORT).show();
            return;
        }


        String StartDateString = StartDateDayString+"/"+StartDateMonthString+"/"+StartDateYearString;
        if(StartDateString == null || StartDateString.equals("")){
            Toast.makeText(this, "You should choose start date for making a reservation...", Toast.LENGTH_SHORT).show();
            return;
        }
        String EndDateString =  EndDateDayString+"/"+EndDateMonthString+"/"+EndDateYearString;
        if(EndDateString == null || EndDateString.equals("")){
            Toast.makeText(this, "You should choose end date for making a reservation...", Toast.LENGTH_SHORT).show();
            return;
        }
        Date StartDateTime;
        Date EndDateTime;
        Date nowdate = new Date(System.currentTimeMillis());
        Date yesterdaydate = new Date(System.currentTimeMillis()- (1000 * 60 * 60 * 24));
        try {
            StartDateTime=new SimpleDateFormat("dd/MM/yyyy").parse(StartDateString);
            EndDateTime=new SimpleDateFormat("dd/MM/yyyy").parse(EndDateString);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Oops Error Occured...", Toast.LENGTH_SHORT).show();
            return;
        }

        if(officedata.OwnerId.toLowerCase().equals(MainActivity.LoginedUser.getUid().toLowerCase())){
            Toast.makeText(this, "You already own this place...", Toast.LENGTH_SHORT).show();
            return;
        }

        if(StartDateTime.before(nowdate))
        {
            Toast.makeText(this, "Start Date should be after today", Toast.LENGTH_SHORT).show();
            return;
        }
        if(EndDateTime.before(nowdate))
        {
            Toast.makeText(this, "End Date should be after today", Toast.LENGTH_SHORT).show();
            return;
        }
        if(EndDateTime.before(StartDateTime))
        {
            Toast.makeText(this, "End Date should be after Start Date", Toast.LENGTH_SHORT).show();
            return;
        }
        if(EndDateTime.equals(StartDateTime))
        {
            Toast.makeText(this, "There should be at least 1 day between start and end dates", Toast.LENGTH_SHORT).show();
            return;
        }

        String MyUserId = MainActivity.LoginedUser.getUid().toLowerCase().toString();
        for(int x=0;x < Bookings.size();x++){
            if(Bookings.get(x).OfficeId.toLowerCase().toString().equals(officedata.Id.toLowerCase().toString()))
            {
                if((StartDateTime.before(Bookings.get(x).EndDate)  &&
                        StartDateTime.after(Bookings.get(x).StartDate)) ||
                        (StartDateTime.equals(Bookings.get(x).StartDate)  ||
                                EndDateTime.equals(Bookings.get(x).EndDate)) ||
                        (EndDateTime.before(Bookings.get(x).EndDate)  &&
                                EndDateTime.after(Bookings.get(x).StartDate)))
                {
                    Toast.makeText(this, "This location is already booked for these dates...", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        BookingData data = new BookingData();
        data.Id = UUID.randomUUID().toString().toLowerCase();
        data.BookingPrice = officedata.Price;
        data.Rate = 5f;
        data.UserId = MainActivity.LoginedUser.getUid().toLowerCase().toString();
        data.OfficeId = officedata.Id.toLowerCase().toString();
        data.StartDate = StartDateTime;
        data.EndDate = EndDateTime;
        data.IsRated = 0;
        data.BookingDate = nowdate;

        FirebaseDatabase.getInstance().getReference().child(MainActivity.BookingDBKey).child(data.Id.toLowerCase().toString())
                .setValue(data);


        Toast.makeText(this, "Booking Successful!", Toast.LENGTH_SHORT).show();
        GoToBookingPage();

    }



    public  void OnClickCancelReservation()
    {
        db.child(MainActivity.BookingDBKey).child(bookingdata.Id.toLowerCase().toString())
                .removeValue();
        CalculateAndUpdateRateOfOffice(0,true);
        Toast.makeText(this, "Booking Canceled!", Toast.LENGTH_SHORT).show();
        GoMainPage();
    }

public  void OnAddWishListButtonClick(){
        if(!ReadyToAddWishlist)
            return;

    String MyUserId = MainActivity.LoginedUser.getUid().toLowerCase().toString();
        for(int x=0;x < MyWishList.size();x++){
            if(MyUserId.equals(MyWishList.get(x).UserId.toLowerCase().toString()) &&
                    officedata.Id.toLowerCase().toString().equals(MyWishList.get(x).OfficeId.toLowerCase().toString()))
            {
                FirebaseDatabase.getInstance().getReference().child(MainActivity.WishListKey).child(MyWishList.get(x).Id).removeValue();
                Toast.makeText(this, "Location removed from your wishlist...", Toast.LENGTH_SHORT).show();
                GetMyWishList();
                return;
            }
        }

    Wish data = new Wish();
    data.Id = UUID.randomUUID().toString().toLowerCase();
    data.UserId = MyUserId;
    data.OfficeId = officedata.Id.toString().toLowerCase();

    FirebaseDatabase.getInstance().getReference().child(MainActivity.WishListKey).child(data.Id.toLowerCase().toString())
            .setValue(data);

    Toast.makeText(this, "Location added to your wishlist...", Toast.LENGTH_SHORT).show();
    GetMyWishList();
}

    public  void GoToBookingPage(){
        // Open second activity by sending data required.
        Intent act = new Intent(this,ListActivity.class);
        act.putExtra(MainActivity.ExtraIsForBookingDataKey,"1");
        startActivity(act); //Start activity
        finish();
    }
    public  void GoMainPage(){
        // Open second activity by sending data required.
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void ImageLoad(ImageView imageView,String Link)
    {
        Glide.with(this).load(Link).into(imageView);
    }

    public void GetRelatedBookings(){
        Bookings = new ArrayList<BookingData>();
        db = FirebaseDatabase.getInstance().getReference();
        String MyUserId = MainActivity.LoginedUser.getUid().toLowerCase().toString();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                // Get Post object and use the values to update the UI
                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {

                    String userId = (String)snapshot.child("UserId").getValue();
                    String OfficeId = (String)snapshot.child("OfficeId").getValue();
                    if(officedata.Id.toLowerCase().toString().equals(OfficeId.toLowerCase()))
                    {
                        BookingData post = snapshot.getValue(BookingData.class);
                        Bookings.add(post);
                    }

                }
                ReadyToBook = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        db.child(MainActivity.BookingDBKey).addListenerForSingleValueEvent(postListener);
    }

    public void CalculateAndUpdateRateOfOffice(int Action,boolean GoMainPageAfter){
        Bookings = new ArrayList<BookingData>();
        db = FirebaseDatabase.getInstance().getReference();
        String MyUserId = MainActivity.LoginedUser.getUid().toLowerCase().toString();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                // Get Post object and use the values to update the UI
                float TotalRateSum = 0;
                float HowManyBookings =0;
                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {

                    String userId = (String)snapshot.child("UserId").getValue();
                    String OfficeId = (String)snapshot.child("OfficeId").getValue();
                    if(officedata.Id.toLowerCase().toString().equals(OfficeId.toLowerCase()))
                    {
                        BookingData post = snapshot.getValue(BookingData.class);
                        if(post.IsRated == 1)
                        {
                            TotalRateSum+= post.Rate;
                            HowManyBookings+=1;
                        }
                    }

                }
                switch (Action)
                {
                    case 0:
                        float NewRate = 2.5f;
                        if(HowManyBookings > 0)
                        {
                            NewRate = TotalRateSum/HowManyBookings;
                        }

                        String FallOrRise = "";
                        if(NewRate > officedata.Rating)
                        {
                            FallOrRise = "risen";
                        }
                        else
                        {
                            FallOrRise = "fallen";
                        }
                        Toast.makeText(ThisContext, "Rating has " + FallOrRise + " to " + String.valueOf(NewRate) , Toast.LENGTH_SHORT).show();
                        officedata.Rating = NewRate;
                        db.child(MainActivity.OfficeDataKey).child(officedata.Id.toString().toLowerCase()).setValue(officedata);
                        break;
                }
                if(GoMainPageAfter)
                {
                    GoMainPage();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        db.child(MainActivity.BookingDBKey).addListenerForSingleValueEvent(postListener);
    }

    public  void OpenOwnerDetail(){
        if(OwnerData==null)
        {
            Log.d("Oops","Owner data null");
            return;
        }
        // Open second activity by sending data required.
        Intent Act = new Intent(this,ProfileView.class);
        Act.putExtra(MainActivity.ExtraUserData,MainActivity.UserDataToString(OwnerData));
        startActivity(Act); //Start activity
    }

    public void GetMyWishList(){
        AllWishList = new ArrayList<Wish>();
        MyWishList = new ArrayList<Wish>();
        db = FirebaseDatabase.getInstance().getReference();
        String MyUserId = MainActivity.LoginedUser.getUid().toLowerCase().toString();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                // Get Post object and use the values to update the UI
                AddToWishListButton.setText("ADD TO WISHLIST");
                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {

                    Wish post = snapshot.getValue(Wish.class);
                    String userId = (String)snapshot.child("UserId").getValue();
                    if(userId.toLowerCase().toString().equals(MyUserId.toLowerCase()) )
                    {
                        MyWishList.add(post);
                        if(post.OfficeId.toLowerCase().equals(officedata.Id.toLowerCase().toString()))
                        {
                            AddToWishListButton.setText("REMOVE FROM WISHLIST");
                        }
                    }
                    AllWishList.add(post);

                }
                ReadyToAddWishlist = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        db.child(MainActivity.WishListKey).addListenerForSingleValueEvent(postListener);
    }
    private void GetOwnerData() {
        if(db == null)
            return;
        if(officedata == null)
            return;
        if(officedata.OwnerId == null || officedata.OwnerId.equals(""))
            return;


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {


                    String uid = (String)snapshot.child("uid").getValue();

                    if(uid.toLowerCase().equals(officedata.OwnerId.toLowerCase().toString()))
                    {
                        Log.d("Gathered Data","Success");
                        User post = snapshot.getValue(User.class);
                        OwnerData = post;
                        return;
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        db.child(MainActivity.UserKey).addListenerForSingleValueEvent(postListener);

    }
}