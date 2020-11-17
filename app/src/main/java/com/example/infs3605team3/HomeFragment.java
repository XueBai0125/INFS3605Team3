package com.example.infs3605team3;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.infs3605team3.model.OfficeData;
import com.example.infs3605team3.model.User;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

public class HomeFragment  extends Fragment {
    private int[] images = {R.drawable.o1, R.drawable.o2,R.drawable.o3,R.drawable.o4,R.drawable.o5,
            R.drawable.o6,R.drawable.o7,R.drawable.o8};


    public Spinner SpinnerElement;
    public EditText SearchText;
    ListView TheList;
    MainActivityListAdapter adapter;
    String[] SpinnerItems = new String[]{"A-Z","Z-A","Rate Asc","Rate Desc","Price Asc","Price Desc"}; // Spinner Elements
    public  int SelectedSpinnerValue = 0;
    ArrayList<OfficeData> offices;
    ArrayList<OfficeData> FilteredOffices;
    View rootview;
    View innerview;
    DatabaseReference db;
    StorageReference TheStorageReference;

    ImageView SortImage;



    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_home,container,false);
        return rootview;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TheStorageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseDatabase.getInstance().getReference();
        innerview = view;
        SearchText = (EditText)view.findViewById(R.id.OfficeSearch);
        SpinnerElement = (Spinner)view.findViewById(R.id.spinner);
        TheList = (ListView)view.findViewById(R.id.TheListView);
        ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, SpinnerItems);

        SpinnerElement.setAdapter(SpinnerAdapter);

        CollectOffices();

        SortImage = view.findViewById(R.id.SortImage);
        SortImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), Filter.class);
                startActivity(intent);
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
        adapter = new MainActivityListAdapter(this.rootview.getContext(), R.layout.mainpagerowactivity,FilteredOffices);
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

                    offices.add(post);
                }
                AfterOfficeDataCollected();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(getContext(), "Oops! Something went wrong...", Toast.LENGTH_LONG).show();
                // ...
            }
        };
        db.child(MainActivity.OfficeDataKey).addListenerForSingleValueEvent(postListener);
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
            Log.d("Listview","Updated");
            adapter.notifyDataSetChanged();
        }
    }

    public  void OpenOfficeDetail(OfficeData data){
        if(data==null)
        {
            return;
        }
        // Open second activity by sending data required.
        Intent InfoActivity = new Intent(rootview.getContext(),OfficeDetail.class);
        InfoActivity.putExtra(MainActivity.ExtraOfficeData,MainActivity.OfficeDataToString(data));
        InfoActivity.putExtra(MainActivity.ExtraBookingData,"");
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
