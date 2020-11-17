package com.example.infs3605team3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.infs3605team3.model.OfficeData;
import com.example.infs3605team3.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gyf.immersionbar.ImmersionBar;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MineFragment extends Fragment {

    View rootview;
    private FirebaseAuth mAuth;
    private User model;
    private LinearLayout BookingLayout;
    private ImageView BookingImage;
    private TextView BookingText;
    private LinearLayout WishLayout;
    private ImageView WishImage;
    private TextView WishText;

    private LinearLayout MyListingsLayout;
    private ImageView MyListingsImage;
    private TextView MyListingsText;

    private LinearLayout LogoutLayout;
    private ImageView LogoutImage;
    private TextView LogoutText;


    private TextView tvThree;
    private TextView tvFour;
    private EditText NameText;
    private EditText LastNameText;
    private TextView EmailText;
    private EditText PhoneText;
    private ImageView ProfileImage;

    private Bitmap UploadedBitmap;
    private int PICK_IMAGE = 1;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_mine,container,false);
        return rootview;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    private void initView() {
        Toolbar llToolbar =  rootview.findViewById(R.id.toolbar);
        ImmersionBar.setTitleBar(this, llToolbar);
        ProfileImage = rootview.findViewById(R.id.ProfileImage);

        NameText = rootview.findViewById(R.id.NameText);
        NameText.setText(MainActivity.LoginedUser.getFirstName());
        LastNameText = rootview.findViewById(R.id.LastNameText);
        LastNameText.setText(MainActivity.LoginedUser.getLastName());
        EmailText = rootview.findViewById(R.id.EmailTextId);
        EmailText.setText(MainActivity.LoginedUser.getEmail());
        PhoneText = rootview.findViewById(R.id.PhoneTextId);
        PhoneText.setText(MainActivity.LoginedUser.getPhoneNumber());
        EditText.OnEditorActionListener e = new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.

                        UpdateAccount();
                        return true; // consume.
                    }
                }
                return false; // pass on to other listeners.
            }
        };
        EmailText.setOnEditorActionListener(e);
        PhoneText.setOnEditorActionListener(e);
        NameText.setOnEditorActionListener(e);
        LastNameText.setOnEditorActionListener(e);


        BookingLayout = (LinearLayout) rootview.findViewById(R.id.MyBookingsLayout);
        BookingImage = rootview.findViewById(R.id.MyBookingsImage);
        BookingText = rootview.findViewById(R.id.MyBookingsText);

        WishLayout = (LinearLayout) rootview.findViewById(R.id.WishlistLayout);
        WishImage = rootview.findViewById(R.id.WishlistImage);
        WishText = rootview.findViewById(R.id.WishlistText);

        MyListingsLayout = (LinearLayout) rootview.findViewById(R.id.MyListingsLayout);
        MyListingsImage = rootview.findViewById(R.id.MyListingsImage);
        MyListingsText = rootview.findViewById(R.id.MyListingsText);

        LogoutLayout = (LinearLayout) rootview.findViewById(R.id.LogoutLayout);
        LogoutImage = rootview.findViewById(R.id.LogoutImage);
        LogoutText = rootview.findViewById(R.id.LogoutText);

        if(MainActivity.LoginedUser.ProfileImageLink != null &&
                !MainActivity.LoginedUser.ProfileImageLink.equals("") )
        {
            ImageLoad(ProfileImage,MainActivity.LoginedUser.ProfileImageLink);
        }

        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        BookingText.setText("My Bookings");
        MyListingsText.setText("My Offices");
        WishText.setText("WishList");
        View.OnClickListener BookingListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenLists(1);
            }
        };
        View.OnClickListener WishlistListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenLists(0);
            }
        };
        View.OnClickListener MyListingsListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenLists(2);
            }
        };
        View.OnClickListener LogoutListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        };

        BookingLayout.setOnClickListener(BookingListener);
        BookingImage.setOnClickListener(BookingListener);
        BookingText.setOnClickListener(BookingListener);
        WishLayout.setOnClickListener(WishlistListener);
        WishImage.setOnClickListener(WishlistListener);
        WishText.setOnClickListener(WishlistListener);

        MyListingsLayout.setOnClickListener(MyListingsListener);
        MyListingsImage.setOnClickListener(MyListingsListener);
        MyListingsText.setOnClickListener(MyListingsListener);

        LogoutLayout.setOnClickListener(LogoutListener);
        LogoutImage.setOnClickListener(LogoutListener);
        LogoutText.setOnClickListener(LogoutListener);


    }

    public void UpdateAccount(){

        String EmailVal = EmailText.getText().toString();
        if(EmailVal != null && !EmailVal.equals("") && EmailVal.contains("@") &&
        EmailVal.contains("."))
        {
            MainActivity.LoginedUser.setEmail(EmailVal);
        }
        MainActivity.LoginedUser.setFirstName(NameText.getText().toString());
        MainActivity.LoginedUser.setLastName(LastNameText.getText().toString());
        MainActivity.LoginedUser.setPhoneNumber(PhoneText.getText().toString());
        FirebaseDatabase.getInstance().getReference().
                child(MainActivity.UserKey).child(MainActivity.LoginedUser.getUid().toString().toLowerCase()).setValue(MainActivity.LoginedUser);
    }

    public  void OpenLists(int Status){

        // Open second activity by sending data required.
        Intent act = new Intent(this.getContext(),ListActivity.class);
        act.putExtra(MainActivity.ExtraIsForBookingDataKey,String.valueOf(Status));
        startActivity(act); //Start activity
    }
    public  void Logout(){

        // Open second activity by sending data required.
        MainActivity.LoginedUser = null;
        Intent act = new Intent(this.getContext(),LoginActivity.class);
        startActivity(act); //Start activity
        this.getActivity().finish();
    }
    public void ImageLoad(ImageView imageView,String Link)
    {
        Glide.with(this.getContext()).load(Link).into(imageView);
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
                ProfileImage.setBackgroundColor(Color.TRANSPARENT);
                ProfileImage.setImageBitmap(UploadedBitmap);
                UploadImage();


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

    public void UploadImage()
    {
        if(UploadedBitmap == null)
        {
            Toast.makeText(getContext(), "You should upload image for creating a post!", Toast.LENGTH_LONG).show();
            return;
        }

        String Path = MainActivity.LoginedUser.getUid().toString().toLowerCase() + ".jpg";
        MainActivity.LoginedUser.ProfileImageLink = MainActivity.ProfileImagesKey + "/" + Path;
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(MainActivity.LoginedUser.ProfileImageLink);
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

                    MainActivity.LoginedUser.ProfileImageLink = downloadUri.toString();

                    UpdateAccount();
                    Toast.makeText(getContext(), "Profile Image Updated!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Error occured while uploading the image...", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }






}
