package com.example.infs3605team3.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class OfficeData implements Serializable {

    public OfficeData(String Id, String Name, float Price, float Rating
            , String ImageLink, int M2,
                      boolean Internet, boolean Printer,boolean Hr24Access,boolean Kitchen,
                      boolean Scanner, boolean Parking,boolean Security,boolean Projector,
            String Country, String Province, String District, String Address, String Description,
                      String OwnerName,String OwnerId,int Capacity){

        this.Id = Id;
        this.Name = Name;
        this.Country = Country;
        this.ImageLink = ImageLink;
        this.Province = Province;
        this.District = District;
        this.Address = Address;
        this.Price = Price;
        this.Rating = Rating;
        this.M2 = M2;
        this.Description = Description;
        this.Internet = Internet ? 1:0;
        this.Printer = Printer ? 1:0;
        this.Hr24Access = Hr24Access ? 1:0;
        this.Kitchen = Kitchen ? 1:0;
        this.Scanner = Scanner ? 1:0;
        this.Parking = Parking ? 1:0;
        this.Security = Security ? 1:0;
        this.Projector = Projector ? 1:0;


        this.OwnerId = OwnerId;
        this.OwnerName = OwnerName;
        this.Capacity = Capacity;
    }
    public OfficeData(OfficeData data){

        if(data == null)
            return;
        this.Id = data.Id;
        this.Name = data.Name;
        this.Country = data.Country;
        this.ImageLink = data.ImageLink;
        this.Province = data.Province;
        this.District = data.District;
        this.Address = data.Address;
        this.Price = data.Price;
        this.Rating = data.Rating;
        this.M2 = data.M2;
        this.Description = data.Description;
        this.OwnerId = data.OwnerId;
        this.OwnerName = data.OwnerName;
        this.Capacity = data.Capacity;

        this.Internet = data.Internet;
        this.Printer = data.Printer;
        this.Hr24Access = data.Hr24Access;
        this.Kitchen = data.Kitchen;
        this.Scanner = data.Scanner;
        this.Parking = data.Parking;
        this.Security = data.Security;
        this.Projector = data.Projector;
        this.ExternalBooking = data.ExternalBooking;
        this.ExternalWish = data.ExternalWish;
        this.PostCreationDate = data.PostCreationDate;

    }

    public OfficeData(){

    }

    public String Id;
    public String Name;
    public String Country;
    public String Province;
    public String District;
    public String Address;
    public float Price;
    public float Rating;
    public String ImageLink;
    public int M2;
    public String Description;
    public int Printer;
    public int Internet;
    public int Hr24Access;
    public int Kitchen;
    public int Scanner;
    public int Parking;
    public int Security;
    public int Projector;
    public  String OwnerId;
    public  String OwnerName;
    public int Capacity;

    public Date PostCreationDate;

    public  BookingData ExternalBooking;
    public  Wish ExternalWish;

    public String GetName()
    {
        return Name;
    }
    public float GetRate()
    {
        return Rating;
    }

}
