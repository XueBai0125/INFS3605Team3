package com.example.infs3605team3;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.infs3605team3.model.BookingData;
import com.example.infs3605team3.model.OfficeData;
import com.example.infs3605team3.model.Wish;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListActivity extends AppCompatActivity {

    public Spinner SpinnerElement;
    public EditText SearchText;
    ListView TheList;
    MainActivityListAdapter adapter;
    String[] SpinnerItems = new String[]{"A-Z","Z-A","Rate Asc","Rate Desc","Price Asc","Price Desc"}; // Spinner Elements
    public  int SelectedSpinnerValue = 0;
    ArrayList<OfficeData> offices;
    ArrayList<OfficeData> FilteredOffices;

    TextView ListTitleText;
    Button GoBackButton;

    ArrayList<BookingData> MyBookings;
    ArrayList<Wish> MyWishes;

    DatabaseReference db;
    StorageReference TheStorageReference;

    Context ContextOfThis;
    int Status = 0;
    ImageView SortImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent theintent = getIntent();
        String ext = theintent.getStringExtra(MainActivity.ExtraIsForBookingDataKey);
        Status = Integer.valueOf(ext);
        ContextOfThis = this;
        TheStorageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseDatabase.getInstance().getReference();
        SearchText = (EditText)findViewById(R.id.OfficeSearch);
        SpinnerElement = (Spinner)findViewById(R.id.spinner);
        TheList = (ListView)findViewById(R.id.TheListView);
        ListTitleText = (TextView)findViewById(R.id.ListTitleText);
        GoBackButton = (Button) findViewById(R.id.GoBackButton);


        SortImage = findViewById(R.id.SortImage);
        SortImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), Filter.class);
                startActivity(intent);
            }
        });

        if(Status == 1)
        {
            ListTitleText.setText("My Bookings");
        }
        else if(Status == 2)
        {
            ListTitleText.setText("My Offices");
        }
        else
        {
            ListTitleText.setText("My Wish");
        }

        ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, SpinnerItems);

        SpinnerElement.setAdapter(SpinnerAdapter);

        if(Status == 1 || Status == 0)
        {
            CollectMyBooking();
        }
        else
        {
            CollectOffices();
        }

        GoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoMainPage();
            }
        });

        TheList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OpenOfficeDetail(FilteredOffices.get(i));// On click list view element open second activity
            }


        });

        SearchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
// When the search text changed find data  according to its name, location and type name.
                Filter();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


            }});
        SpinnerElement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                SelectedSpinnerValue = position;// Set sorting option.
                SortBySelectedSpinnerValue();// Apply sorting options.

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

    }


    public  void AfterOfficeDataCollected(){

        RefillFilteredOffices();
        adapter = new MainActivityListAdapter(ContextOfThis, R.layout.mainpagerowactivity,FilteredOffices);
        TheList.setAdapter(adapter);
        Filter();
    }

    @Override
    public void onResume() {
        super.onResume();
        Filter();
    }

    public  void Filter(){
        String Text = SearchText.getText().toString().toLowerCase();

        RefillFilteredOffices();
        if(Text!= null || MainActivity.Filter != null)
        {
            Log.d("Filter","Started");
            for(int x=0;x < FilteredOffices.size();x++){
                if(Text!= null) {
                    Log.d("Filter",Text);
                    if (!(FilteredOffices.get(x).Name.toLowerCase().contains(Text) ||
                            FilteredOffices.get(x).Address.toLowerCase().contains(Text) ||
                            FilteredOffices.get(x).Description.toLowerCase().contains(Text))) {
                        FilteredOffices.remove(x);
                        x--;
                        continue;
                    }
                }

                if(MainActivity.Filter != null)
                {
                    Log.d("Filter","Filter");
                    if(MainActivity.Filter.Country != null)
                    {
                        if(!FilteredOffices.get(x).Country.toLowerCase().contains(MainActivity.Filter.Country.toLowerCase()))
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.Province != null)
                    {
                        if(!FilteredOffices.get(x).Province.toLowerCase().contains(MainActivity.Filter.Province.toLowerCase()))
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.District != null)
                    {
                        if(!FilteredOffices.get(x).District.toLowerCase().contains(MainActivity.Filter.District.toLowerCase()))
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.OwnerName != null)
                    {
                        if(!FilteredOffices.get(x).OwnerName.toLowerCase().contains(MainActivity.Filter.OwnerName.toLowerCase()))
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.MinCapacity != null)
                    {
                        if(FilteredOffices.get(x).Capacity < MainActivity.Filter.MinCapacity)
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.MinM2 != null)
                    {
                        if(FilteredOffices.get(x).M2 < MainActivity.Filter.MinM2)
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.MinPrice != null)
                    {
                        if(FilteredOffices.get(x).Price < MainActivity.Filter.MinPrice)
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.MinRate != null)
                    {
                        if(FilteredOffices.get(x).Rating < MainActivity.Filter.MinRate)
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }

                    if(MainActivity.Filter.MaxCapacity != null)
                    {
                        if(FilteredOffices.get(x).Capacity > MainActivity.Filter.MaxCapacity)
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.MaxM2 != null)
                    {
                        if(FilteredOffices.get(x).M2 > MainActivity.Filter.MaxM2)
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.MaxPrice != null)
                    {
                        if(FilteredOffices.get(x).Price > MainActivity.Filter.MaxPrice)
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.MaxRate != null)
                    {
                        if(FilteredOffices.get(x).Rating > MainActivity.Filter.MaxRate)
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.Internet != null)
                    {
                        if(FilteredOffices.get(x).Internet != MainActivity.Filter.Internet)
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.Kitchen != null)
                    {
                        if(FilteredOffices.get(x).Kitchen != MainActivity.Filter.Kitchen)
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.Scanner != null)
                    {
                        if(FilteredOffices.get(x).Scanner != MainActivity.Filter.Scanner)
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.Hr24Access != null)
                    {
                        if(FilteredOffices.get(x).Hr24Access != MainActivity.Filter.Hr24Access)
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.Parking != null)
                    {
                        if(FilteredOffices.get(x).Parking != MainActivity.Filter.Parking)
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.Printer != null)
                    {
                        if(FilteredOffices.get(x).Printer != MainActivity.Filter.Printer)
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.Projector != null)
                    {
                        if(FilteredOffices.get(x).Projector != MainActivity.Filter.Projector)
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                    if(MainActivity.Filter.Security != null)
                    {
                        if(FilteredOffices.get(x).Security != MainActivity.Filter.Security)
                        {
                            FilteredOffices.remove(x);
                            x--;
                            continue;
                        }
                    }
                }
            }
        }


        SortBySelectedSpinnerValue();// Apply sorting options.
    }

    public void CollectMyBooking()
    {

        if(Status == 2)
            return;;
        if(Status == 1)
        {
            if(MyBookings == null)
            {
                MyBookings = new ArrayList<BookingData>();
            }
            MyBookings.clear();
        }
        else
        {
            if(MyWishes == null)
            {
                MyWishes = new ArrayList<Wish>();
            }
            MyWishes.clear();
        }

        String MyUserId = MainActivity.LoginedUser.getUid();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {

                    if(Status == 1)
                    {
                        BookingData post = snapshot.getValue(BookingData.class);
                        if(post.UserId.toLowerCase().toString().equals(MyUserId.toLowerCase().toString()))
                        {
                            MyBookings.add(post);
                        }
                    }
                    else
                    {
                        Wish post = snapshot.getValue(Wish.class);
                        if(post.UserId.toLowerCase().toString().equals(MyUserId.toLowerCase().toString()))
                        {
                            MyWishes.add(post);
                        }
                    }
                }
                CollectOffices();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(ContextOfThis, "Oops! Something went wrong...", Toast.LENGTH_LONG).show();
                // ...
            }
        };
        if(Status == 1)
        {
            db.child(MainActivity.BookingDBKey).addListenerForSingleValueEvent(postListener);
        }
        else
        {
            db.child(MainActivity.WishListKey).addListenerForSingleValueEvent(postListener);
        }
    }


    public void CollectOffices()
    {
        if(offices == null)
        {
            offices = new ArrayList<OfficeData>();
        }
        if(FilteredOffices == null)
        {
            FilteredOffices = new ArrayList<OfficeData>();
        }

        offices.clear();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {


                    OfficeData post = snapshot.getValue(OfficeData.class);

                    if(Status == 1)
                    {
                        for(int x=0;x < MyBookings.size();x++){
                            if(MyBookings.get(x).OfficeId.toLowerCase().equals(post.Id.toString().toLowerCase()))
                            {
                                post.ExternalBooking = MyBookings.get(x);
                                offices.add(post);
                            }
                        }
                    }
                    else if(Status == 2)
                    {
                        if(post.OwnerId.toLowerCase().equals(MainActivity.LoginedUser.getUid().toString().toLowerCase()))
                        {
                            offices.add(post);
                        }
                    }
                    else
                    {
                        for(int x=0;x < MyWishes.size();x++){
                            if(MyWishes.get(x).OfficeId.toLowerCase().equals(post.Id.toString().toLowerCase()))
                            {
                                post.ExternalWish = MyWishes.get(x);
                                offices.add(post);
                            }
                        }
                    }

                }
                AfterOfficeDataCollected();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(ContextOfThis, "Oops! Something went wrong...", Toast.LENGTH_LONG).show();
                // ...
            }
        };
        db.child(MainActivity.OfficeDataKey).addListenerForSingleValueEvent(postListener);
    }


    public  void GoMainPage(){
        // Open second activity by sending data required.
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public  void RefillFilteredOffices(){
        if(offices == null)
            offices = new ArrayList<OfficeData>();
        if(FilteredOffices == null)
            FilteredOffices = new ArrayList<OfficeData>();

        FilteredOffices.clear();
        for(int x=0;x < offices.size();x++){
            FilteredOffices.add(offices.get(x));
        }
    }

    public  void SortBySelectedSpinnerValue(){

        if(FilteredOffices == null)
            return;
        // Apply sorting options according to chosen sort element.
        if(SelectedSpinnerValue == 0)
        {
            Collections.sort(FilteredOffices,NameCompAsc);
        }
        else if(SelectedSpinnerValue == 1)
        {
            Collections.sort(FilteredOffices,NameCompDesc);
        }
        else if(SelectedSpinnerValue == 2)
        {
            Collections.sort(FilteredOffices,RateCompAsc);
        }
        else if(SelectedSpinnerValue == 3)
        {
            Collections.sort(FilteredOffices,RateCompDesc);
        }
        else if(SelectedSpinnerValue == 4)
        {
            Collections.sort(FilteredOffices,PriceCompAsc);
        }
        else if(SelectedSpinnerValue == 5)
        {
            Collections.sort(FilteredOffices,PriceCompDesc);
        }
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    public  void OpenOfficeDetail(OfficeData data){
        if(data==null)
        {
            return;
        }
        // Open second activity by sending data required.
        Intent InfoActivity = new Intent(this,OfficeDetail.class);
        InfoActivity.putExtra(MainActivity.ExtraBookingData,MainActivity.BookingDataToString(data.ExternalBooking));
        data.ExternalBooking = null;
        InfoActivity.putExtra(MainActivity.ExtraOfficeData,MainActivity.OfficeDataToString(data));
        startActivity(InfoActivity); //Start activity
    }

    public static Comparator<OfficeData> NameCompAsc = new Comparator<OfficeData>() {

        // Compare names for sorting in ascending order
        public int compare(OfficeData s1, OfficeData s2) {
            String Name1 = s1.Name.toLowerCase();
            String Name2 = s2.Name.toLowerCase();

            //ascending order
            return Name1.compareTo(Name2);

        }};

    public static Comparator<OfficeData> NameCompDesc = new Comparator<OfficeData>() {

        // Compare names for sorting in descending order
        public int compare(OfficeData s1, OfficeData s2) {
            String Name1 = s1.Name.toLowerCase();
            String Name2 = s2.Name.toLowerCase();

            //descending order
            return Name2.compareTo(Name1);
        }};

    public static Comparator<OfficeData> RateCompAsc = new Comparator<OfficeData>() {


        // Compare rates for sorting in ascending order
        public int compare(OfficeData s1, OfficeData s2) {

            int rate1 = (int)(s1.Rating*10);
            int rate2 = (int)(s2.Rating*10);

            /*For ascending order*/
            return rate1-rate2;
        }};

    public static Comparator<OfficeData> RateCompDesc = new Comparator<OfficeData>() {


        // Compare rates for sorting in descending order
        public int compare(OfficeData s1, OfficeData s2) {

            int rate1 = (int)(s1.Rating*10);
            int rate2 = (int)(s2.Rating*10);

            /*For ascending order*/
            return rate2-rate1;
        }};

    public static Comparator<OfficeData> PriceCompAsc = new Comparator<OfficeData>() {


        // Compare rates for sorting in ascending order
        public int compare(OfficeData s1, OfficeData s2) {

            int rate1 = (int)(s1.Price);
            int rate2 = (int)(s2.Price);

            /*For ascending order*/
            return rate1-rate2;
        }};

    public static Comparator<OfficeData> PriceCompDesc = new Comparator<OfficeData>() {


        // Compare rates for sorting in descending order
        public int compare(OfficeData s1, OfficeData s2) {

            int rate1 = (int)(s1.Price);
            int rate2 = (int)(s2.Price);

            /*For ascending order*/
            return rate2-rate1;
        }};

}