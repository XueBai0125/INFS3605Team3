package com.example.infs3605team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.infs3605team3.model.BookingFilter;

public class Filter extends AppCompatActivity {


    EditText MinPriceInput;
    EditText MaxPriceInput;
    EditText MinCapacityInput;
    EditText MaxCapacityInput;
    EditText MinSizeInput;
    EditText MaxSizeInput;
    EditText MinRateInput;
    EditText MaxRateInput;
    EditText CountryInput;
    EditText CityInput;
    EditText SuburbInput;
    EditText OwnerInput;

    public CheckBox IsThereInternetCheckbox;
    public CheckBox IsTherePrinterCheckbox;
    public CheckBox IsThereScannerCheckbox;
    public CheckBox IsThereParkingCheckbox;
    public CheckBox IsThereSecurityCheckbox;
    public CheckBox IsThere24HrAccessCheckbox;
    public CheckBox IsThereKitchenCheckbox;
    public CheckBox IsThereProjectorCheckbox;


    public boolean IsInternetChanged = false;
    public boolean IsPrinterChanged = false;
    public boolean IsScannerChanged = false;
    public boolean IsSecurityChanged = false;
    public boolean IsParkingChanged = false;
    public boolean Is24HrAccessChanged = false;
    public boolean IsProjectorChanged = false;
    public boolean IsKitchenChanged = false;
    Context ThisContext;
    public Button FilterButton;
    public Button ClearFilterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ThisContext = this;
        MinPriceInput = (EditText) findViewById(R.id.MinPriceInput);
        MaxPriceInput = (EditText) findViewById(R.id.MaxPriceInput);
        MinCapacityInput = (EditText) findViewById(R.id.MinCapacity);
        MaxCapacityInput = (EditText) findViewById(R.id.MaxCapacity);
        MinSizeInput = (EditText) findViewById(R.id.MinSize);
        MaxSizeInput = (EditText) findViewById(R.id.MaxSize);
        MinRateInput = (EditText) findViewById(R.id.MinRate);
        MaxRateInput = (EditText) findViewById(R.id.MaxRate);
        CountryInput = (EditText) findViewById(R.id.CountryFilter);
        CityInput = (EditText) findViewById(R.id.CityInput);
        SuburbInput = (EditText) findViewById(R.id.SuburbInput);
        OwnerInput = (EditText) findViewById(R.id.Owner);
        FilterButton = (Button)findViewById(R.id.FilteringButton);
        ClearFilterButton = (Button)findViewById(R.id.ClearFilterButton);

        IsThereInternetCheckbox = (CheckBox)findViewById(R.id.IsThereInternet);
        IsTherePrinterCheckbox = (CheckBox) findViewById(R.id.IsTherePrinter);
        IsThereScannerCheckbox = (CheckBox) findViewById(R.id.IsThereScanner);
        IsThereSecurityCheckbox = (CheckBox) findViewById(R.id.Security);
        IsThereParkingCheckbox = (CheckBox) findViewById(R.id.Parking);
        IsThere24HrAccessCheckbox = (CheckBox) findViewById(R.id.IsThere24HourAccess);
        IsThereProjectorCheckbox = (CheckBox) findViewById(R.id.Projector);
        IsThereKitchenCheckbox = (CheckBox) findViewById(R.id.IsThereKitchen);



        InitializeCheckboxListeners();
        FilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.Filter = GetFilter();
                finish();
            }
        });

        ClearFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.Filter = null;
                finish();

            }
        });

        SetFilters();
    }

    public  void SetFilters(){
        if(MainActivity.Filter != null)
        {
            MinPriceInput.setText(MainActivity.Filter.MinPrice!= null?MainActivity.Filter.MinPrice.toString():"");
            MaxPriceInput.setText(MainActivity.Filter.MaxPrice!= null?MainActivity.Filter.MaxPrice.toString():"");
            MinCapacityInput.setText(MainActivity.Filter.MinCapacity!= null?MainActivity.Filter.MinCapacity.toString():"");
            MaxCapacityInput.setText(MainActivity.Filter.MaxCapacity!= null?MainActivity.Filter.MaxCapacity.toString():"");
            MinSizeInput.setText(MainActivity.Filter.MinM2!= null?MainActivity.Filter.MinM2.toString():"");
            MaxSizeInput.setText(MainActivity.Filter.MaxM2!= null?MainActivity.Filter.MaxM2.toString():"");
            MinRateInput.setText(MainActivity.Filter.MinRate!= null?MainActivity.Filter.MinRate.toString():"");
            MaxRateInput.setText(MainActivity.Filter.MaxRate!= null?MainActivity.Filter.MaxRate.toString():"");
            CountryInput.setText(MainActivity.Filter.Country!= null?MainActivity.Filter.Country.toString():"");
            CityInput.setText(MainActivity.Filter.Province!= null?MainActivity.Filter.Province.toString():"");
            SuburbInput.setText(MainActivity.Filter.District!= null?MainActivity.Filter.District.toString():"");
            OwnerInput.setText(MainActivity.Filter.OwnerName!= null?MainActivity.Filter.OwnerName.toString():"");

            IsThereInternetCheckbox.setChecked(MainActivity.Filter.Internet!= null?MainActivity.Filter.Internet== 1:false);
            IsTherePrinterCheckbox.setChecked(MainActivity.Filter.Printer!= null?MainActivity.Filter.Printer== 1:false);
            IsThereScannerCheckbox.setChecked(MainActivity.Filter.Scanner!= null?MainActivity.Filter.Scanner== 1:false);
            IsThereSecurityCheckbox.setChecked(MainActivity.Filter.Security!= null?MainActivity.Filter.Security== 1:false);
            IsThereParkingCheckbox.setChecked(MainActivity.Filter.Parking!= null?MainActivity.Filter.Parking== 1:false);
            IsThere24HrAccessCheckbox.setChecked(MainActivity.Filter.Hr24Access!= null?MainActivity.Filter.Hr24Access== 1:false);
            IsThereProjectorCheckbox.setChecked(MainActivity.Filter.Projector!= null?MainActivity.Filter.Projector== 1:false);
            IsThereKitchenCheckbox.setChecked(MainActivity.Filter.Kitchen!= null?MainActivity.Filter.Kitchen== 1:false);
        }
    }

    public  void InitializeCheckboxListeners(){

        IsThereInternetCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                               @Override
                                                               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                                   IsInternetChanged = true;
                                                               }
                                                           }
        );
        IsTherePrinterCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                               @Override
                                                               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                                   IsPrinterChanged = true;
                                                               }
                                                           }
        );
        IsThereScannerCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                               @Override
                                                               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                                   IsScannerChanged = true;
                                                               }
                                                           }
        );
        IsThereSecurityCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                               @Override
                                                               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                                   IsSecurityChanged = true;
                                                               }
                                                           }
        );
        IsThereParkingCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                               @Override
                                                               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                                   IsParkingChanged = true;
                                                               }
                                                           }
        );
        IsThere24HrAccessCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                               @Override
                                                               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                                   Is24HrAccessChanged = true;
                                                               }
                                                           }
        );
        IsThereProjectorCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                               @Override
                                                               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                                   IsProjectorChanged = true;
                                                               }
                                                           }
        );
        IsThereKitchenCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                               @Override
                                                               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                                   IsKitchenChanged = true;
                                                               }
                                                           }
        );
    }

    public BookingFilter GetFilter(){
        BookingFilter Result = new BookingFilter();

        boolean IsChanged = false;
        if(MinPriceInput.getText() != null && MinPriceInput.getText().toString() != null &&
                !MinPriceInput.getText().toString().equals(""))
        {
            Result.MinPrice = Float.valueOf(MinPriceInput.getText().toString());
            IsChanged = true;
        }
        if(MaxPriceInput.getText() != null && MaxPriceInput.getText().toString() != null &&
                !MaxPriceInput.getText().toString().equals(""))
        {
            Result.MaxPrice = Float.valueOf(MaxPriceInput.getText().toString());
            IsChanged = true;
        }
        if(MinCapacityInput.getText() != null && MinCapacityInput.getText().toString() != null &&
                !MinCapacityInput.getText().toString().equals(""))
        {
            Result.MinCapacity = Integer.valueOf(MinCapacityInput.getText().toString());
            IsChanged = true;
        }
        if(MaxCapacityInput.getText() != null && MaxCapacityInput.getText().toString() != null &&
                !MaxCapacityInput.getText().toString().equals(""))
        {
            Result.MaxCapacity = Integer.valueOf(MaxCapacityInput.getText().toString());
            IsChanged = true;
        }
        if(MinSizeInput.getText() != null && MinSizeInput.getText().toString() != null &&
                !MinSizeInput.getText().toString().equals(""))
        {
            Result.MinM2 = Float.valueOf(MinSizeInput.getText().toString());
            IsChanged = true;
        }
        if(MaxSizeInput.getText() != null && MaxSizeInput.getText().toString() != null &&
                !MaxSizeInput.getText().toString().equals(""))
        {
            Result.MaxM2 = Float.valueOf(MaxSizeInput.getText().toString());
            IsChanged = true;
        }
        if(MinRateInput.getText() != null && MinRateInput.getText().toString() != null &&
                !MinRateInput.getText().toString().equals(""))
        {
            Result.MinRate = Float.valueOf(MinRateInput.getText().toString());
            IsChanged = true;
        }
        if(MaxRateInput.getText() != null && MaxRateInput.getText().toString() != null &&
                !MaxRateInput.getText().toString().equals(""))
        {
            Result.MaxRate = Float.valueOf(MaxRateInput.getText().toString());
            IsChanged = true;
        }
        if(CountryInput.getText() != null && CountryInput.getText().toString() != null &&
                !CountryInput.getText().toString().equals(""))
        {
            Result.Country = CountryInput.getText().toString();
            IsChanged = true;
        }
        if(CityInput.getText() != null && CityInput.getText().toString() != null &&
                !CityInput.getText().toString().equals(""))
        {
            Result.Province = CityInput.getText().toString();
            IsChanged = true;
        }
        if(SuburbInput.getText() != null && SuburbInput.getText().toString() != null &&
                !SuburbInput.getText().toString().equals(""))
        {
            Result.District = SuburbInput.getText().toString();
            IsChanged = true;
        }
        if(OwnerInput.getText() != null && OwnerInput.getText().toString() != null &&
                !OwnerInput.getText().toString().equals(""))
        {
            Result.OwnerName = OwnerInput.getText().toString();
            IsChanged = true;
        }

        Result.Internet = null;
        Result.Printer = null;
        Result.Scanner = null;
        Result.Security = null;
        Result.Parking = null;
        Result.Hr24Access = null;
        Result.Projector = null;
        Result.Kitchen = null;
        if(IsInternetChanged)
        {
            Result.Internet = IsThereInternetCheckbox.isChecked()?1:0;
            IsChanged = true;
        }
        if(IsPrinterChanged)
        {
            Result.Printer = IsTherePrinterCheckbox.isChecked()?1:0;
            IsChanged = true;
        }
        if(IsScannerChanged)
        {
            Result.Scanner = IsThereScannerCheckbox.isChecked()?1:0;
            IsChanged = true;
        }
        if(IsSecurityChanged)
        {
            Result.Security = IsThereSecurityCheckbox.isChecked()?1:0;
            IsChanged = true;
        }
        if(IsParkingChanged)
        {
            Result.Parking = IsThereParkingCheckbox.isChecked()?1:0;
            IsChanged = true;
        }
        if(Is24HrAccessChanged)
        {
            Result.Hr24Access = IsThere24HrAccessCheckbox.isChecked()?1:0;
            IsChanged = true;
        }
        if(IsProjectorChanged)
        {
            Result.Projector = IsThereProjectorCheckbox.isChecked()?1:0;
            IsChanged = true;
        }
        if(IsKitchenChanged)
        {
            Result.Kitchen = IsThereKitchenCheckbox.isChecked()?1:0;
            IsChanged = true;
        }

        if(!IsChanged)
        {
            Result = null;
        }

        return Result;


    }
}