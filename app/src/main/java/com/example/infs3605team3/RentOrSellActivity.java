package com.example.infs3605team3;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.infs3605team3.model.OfficeData;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class RentOrSellActivity extends Fragment {

    View rootview;
    public static final int PICK_IMAGE = 1;
    public Bitmap UploadedBitmap;
    public ImageView TheImageView;
    public EditText TitleInput;
    public EditText DescriptionInput;
    public EditText PriceInput;
    public EditText M2Input;
    public EditText CountryInput;
    public EditText ProvinceInput;
    public EditText DistrictInput;
    public EditText OpenAdressInput;
    public EditText CapacityInput;

    public CheckBox IsThereInternetCheckbox;
    public CheckBox IsTherePrinterCheckbox;
    public CheckBox IsThereScannerCheckbox;
    public CheckBox IsThereParkingCheckbox;
    public CheckBox IsThereSecurityCheckbox;
    public CheckBox IsThere24HrAccessCheckbox;
    public CheckBox IsThereKitchenCheckbox;
    public CheckBox IsThereProjectorCheckbox;

    public Button CreatePostButton;


    public static RentOrSellActivity newInstance() {
        RentOrSellActivity fragment = new RentOrSellActivity();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_rentorsell,container,false);
        return rootview;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TheImageView = (ImageView) view.findViewById(R.id.imageView3);
        TitleInput = (EditText) view.findViewById(R.id.TitleInput);
        DescriptionInput = (EditText) view.findViewById(R.id.DescriptionInput);
        PriceInput = (EditText) view.findViewById(R.id.PriceInput);
        M2Input = (EditText) view.findViewById(R.id.M2Input);
        CountryInput = (EditText) view.findViewById(R.id.CountryInput);
        ProvinceInput = (EditText) view.findViewById(R.id.ProvinceInput);
        DistrictInput = (EditText) view.findViewById(R.id.DistrictInput);
        OpenAdressInput = (EditText) view.findViewById(R.id.OpenAddressInput);
        CapacityInput = (EditText) view.findViewById(R.id.CapacityInput);

        IsThereInternetCheckbox = (CheckBox) view.findViewById(R.id.IsThereInternet);
        IsTherePrinterCheckbox = (CheckBox) view.findViewById(R.id.IsTherePrinter);
        IsThereScannerCheckbox = (CheckBox) view.findViewById(R.id.IsThereScanner);
        IsThereSecurityCheckbox = (CheckBox) view.findViewById(R.id.Security);
        IsThereParkingCheckbox = (CheckBox) view.findViewById(R.id.Parking);
        IsThere24HrAccessCheckbox = (CheckBox) view.findViewById(R.id.IsThere24HourAccess);
        IsThereProjectorCheckbox = (CheckBox) view.findViewById(R.id.Projector);
        IsThereKitchenCheckbox = (CheckBox) view.findViewById(R.id.IsThereKitchen);


        CreatePostButton = (Button) view.findViewById(R.id.CreatePostButton);

        CreatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UploadedBitmap == null)
                {
                    Toast.makeText(getContext(), "You should upload image for creating a post!", Toast.LENGTH_LONG).show();
                    return;
                }
                OfficeData data = GetOfficeData();
                if(data == null)
                {

                    return;
                }

                String Path = data.Id.toString().toLowerCase() + ".jpg";
                data.ImageLink = MainActivity.OfficeKey + "/" + Path;
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(data.ImageLink);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                UploadedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] bytedata = baos.toByteArray();

                UploadTask uploadTask = storageRef.putBytes(bytedata);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return storageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                            data.ImageLink = downloadUri.toString();

                            FirebaseDatabase.getInstance().getReference().
                                    child(MainActivity.OfficeDataKey).child(data.Id.toString().toLowerCase()).setValue(data);
                            Toast.makeText(getContext(), "Post created Successfully!", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getContext(), "Error occured while uploading the image...", Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        });

        TheImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    public OfficeData GetOfficeData(){
        OfficeData Result = new OfficeData();
        Result.Id = UUID.randomUUID().toString().toLowerCase();
        Result.Rating = 5;
        Result.Name = TitleInput.getText().toString();
        if(Result.Name == null || Result.Name.equals("")){
            Toast.makeText(this.getContext(), "Title cannot be empty...", Toast.LENGTH_LONG).show();
            return null;
        }

        Result.Description = DescriptionInput.getText().toString();
        if(Result.Description == null || Result.Description.equals("")){
            Toast.makeText(this.getContext(), "Description cannot be empty...", Toast.LENGTH_LONG).show();
            return null;
        }
        Result.Price = Float.valueOf(PriceInput.getText().toString());
        if(Result.Price <= 0){
            Toast.makeText(this.getContext(), "Price must be greater than zero...", Toast.LENGTH_LONG).show();
            return null;
        }
        Result.M2 = Integer.valueOf(M2Input.getText().toString());
        if(Result.M2 <= 0){
            Toast.makeText(this.getContext(), "M2 must be greater than zero...", Toast.LENGTH_LONG).show();
            return null;
        }
        Result.Capacity = Integer.valueOf(CapacityInput.getText().toString());
        if(Result.Capacity <= 0){
            Toast.makeText(this.getContext(), "Capacity must be greater than zero...", Toast.LENGTH_LONG).show();
            return null;
        }
        Result.Country = CountryInput.getText().toString();
        if(Result.Country == null || Result.Country.equals("")){
            Toast.makeText(this.getContext(), "Country cannot be empty...", Toast.LENGTH_LONG).show();
            return null;
        }
        Result.Province = ProvinceInput.getText().toString();
        if(Result.Province == null || Result.Province.equals("")){
            Toast.makeText(this.getContext(), "Province cannot be empty...", Toast.LENGTH_LONG).show();
            return null;
        }
        Result.District = DistrictInput.getText().toString();
        if(Result.District == null || Result.District.equals("")){
            Toast.makeText(this.getContext(), "District cannot be empty...", Toast.LENGTH_LONG).show();
            return null;
        }
        Result.Address = OpenAdressInput.getText().toString();
        if(Result.Address == null || Result.Address.equals("")){
            Toast.makeText(this.getContext(), "Address cannot be empty...", Toast.LENGTH_LONG).show();
            return null;
        }

        Result.Internet = IsThereInternetCheckbox.isChecked() ? 1:0;
        Result.Printer = IsTherePrinterCheckbox.isChecked() ? 1:0;
        Result.Scanner = IsThereScannerCheckbox.isChecked() ? 1:0;
        Result.Projector = IsThereProjectorCheckbox.isChecked() ? 1:0;
        Result.Kitchen = IsThereKitchenCheckbox.isChecked() ? 1:0;
        Result.Security = IsThereSecurityCheckbox.isChecked() ? 1:0;
        Result.Parking = IsThereParkingCheckbox.isChecked() ? 1:0;
        Result.Hr24Access = IsThere24HrAccessCheckbox.isChecked() ? 1:0;

        Result.PostCreationDate = new Date(System.currentTimeMillis());

        Result.OwnerId = MainActivity.LoginedUser.getUid();
        Result.OwnerName = MainActivity.LoginedUser.getFirstName() + " " + MainActivity.LoginedUser.getLastName();
        return Result;
    }

    private void selectImage() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d("Log","Picking image");
        if (requestCode == PICK_IMAGE) {
            decodeUri(data.getData());
        }
    }

    public void decodeUri(Uri uri) {
        ParcelFileDescriptor parcelFD = null;
        try {
            parcelFD = this.getContext().getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor imageSource = parcelFD.getFileDescriptor();

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(imageSource, null, o);

            // the new size we want to scale to
            final int REQUIRED_SIZE = 1024;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            UploadedBitmap = BitmapFactory.decodeFileDescriptor(imageSource, null, o2);

            if (UploadedBitmap != null) {
                Log.d("Log","Uploaded");
                TheImageView.setBackgroundColor(Color.TRANSPARENT);
                TheImageView.setImageBitmap(UploadedBitmap);
                Toast.makeText(this.getContext(), "Upload successful!", Toast.LENGTH_LONG).show();


            }

        } catch (FileNotFoundException e) {
            // handle errors
        } catch (IOException e) {
            // handle errors
        } finally {
            if (parcelFD != null)
                try {
                    parcelFD.close();
                } catch (IOException e) {
                    // ignored
                }
        }
    }

}
