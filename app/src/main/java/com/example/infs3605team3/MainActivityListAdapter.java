package com.example.infs3605team3;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.infs3605team3.model.OfficeData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MainActivityListAdapter extends ArrayAdapter<OfficeData> {

    private static final String TAG="SightListAdapter";

    Context mContext;
    int mResource;
    public Activity TheActivity;

    public MainActivityListAdapter(Context context, int resource, ArrayList<OfficeData> List){
        super(context,resource,List);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        //Get row view of list adapter element and set data to the row element
        OfficeData data = getItem(position);
        String Title = data.Name;
        String Location = data.Country + "/" + data.Province + "/" + data.District;
        String Price = String.valueOf(data.Price) + "$";
        float Rating = data.Rating;
        String ImageLink = data.ImageLink;


        if(data.ExternalBooking != null)
        {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String StartDateString = dateFormat.format(data.ExternalBooking.StartDate);
            String EndDateString = dateFormat.format(data.ExternalBooking.EndDate);
            Title += "\n"+ StartDateString.toString() + " - " + EndDateString;
            Price = String.valueOf(data.ExternalBooking.BookingPrice) + "$";
        }
        OfficeData res = new OfficeData(getItem(position));

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView ResName = (TextView)convertView.findViewById(R.id.Title);
        TextView PriceText = (TextView)convertView.findViewById(R.id.Price);
        TextView ResLocation = (TextView)convertView.findViewById(R.id.Location);
        TextView ResRating = (TextView)convertView.findViewById(R.id.Rating);
        ImageView ResImage = (ImageView)convertView.findViewById(R.id.RImage);


        Log.d("Image Link",ImageLink);


        ImageLoad(ResImage,ImageLink);
        ResName.setText(Title);
        PriceText.setText(Price);
        ResLocation.setText(Location);
        ResRating.setText(Rating + " / 5.0");

        return convertView;
    }

    public void ImageLoad(ImageView imageView,String Link)
    {
        Glide.with(mContext).load(Link).into(imageView);
    }
}
