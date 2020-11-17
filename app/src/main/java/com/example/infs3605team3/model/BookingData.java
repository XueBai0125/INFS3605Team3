package com.example.infs3605team3.model;

import java.io.Serializable;
import java.util.Date;

public class BookingData implements Serializable {

    public BookingData(BookingData data){
        this.Id = data.Id;
        this.OfficeId = data.OfficeId;
        this.UserId = data.UserId;
        this.StartDate = data.StartDate;
        this.EndDate = data.EndDate;
        this.BookingPrice = data.BookingPrice;
        this.Rate = data.Rate;
        this.IsRated = data.IsRated;
        this.BookingDate = data.BookingDate;
    }

    public  BookingData(){

    }

    public String Id;
    public String OfficeId;
    public String UserId;
    public Date StartDate;
    public Date EndDate;
    public Date BookingDate;
    public float BookingPrice;
    public float Rate;
    public int IsRated;
}
