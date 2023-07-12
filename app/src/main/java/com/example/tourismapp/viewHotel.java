package com.example.tourismapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewHotel extends AppCompatActivity {
TextView name,phone,price,rating;
ImageView image,zoomImg;
ArrayList<String> imgUrls;
int currentPosition;
RelativeLayout background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hotel);

        Intent i = getIntent();
        Hotel hotel = i.getParcelableExtra("Array");

        String Name = hotel.getName();
        name = findViewById(R.id.name);
        name.setText(Name);

        String Phone = hotel.getPhone();
        phone = findViewById(R.id.phone);
        phone.setText(Phone);

        String Price = hotel.getPrice();
        price = findViewById(R.id.price);
        price.setText(Price);

        String Rating = hotel.getRating();
        rating = findViewById(R.id.rating);
        rating.setText(Rating);

        String img = hotel.getImg();
        image = findViewById(R.id.image);
        zoomImg = findViewById(R.id.zoomImg);



        String loc_name = hotel.getLoc_name();

        currentPosition = 0;
        DatabaseReference hotelRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference imgRef = hotelRef.child("Hotels").child(loc_name).child(Name).child("imgUrls");
        imgUrls = new ArrayList<>();
        imgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imgUrls.clear();
                imgUrls.add(img);
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String imgUrl = dataSnapshot.getValue(String.class);
                    imgUrls.add(imgUrl);
                }
                showImage(currentPosition);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        background = findViewById(R.id.background);

        image.setOnClickListener(v -> {
            Animation fadeIn = new AlphaAnimation(0,1);
            fadeIn.setInterpolator(new DecelerateInterpolator());
            fadeIn.setDuration(1000);
            String zoomImgUrl = imgUrls.get(currentPosition);
            background.setVisibility(View.INVISIBLE);
            Glide.with(this)
                    .load(zoomImgUrl)
                    .into(zoomImg);
            zoomImg.setVisibility(View.VISIBLE);
            zoomImg.startAnimation(fadeIn);
        });

        zoomImg.setOnClickListener(v -> {
            Animation fadeIn = new AlphaAnimation(1,0);
            fadeIn.setInterpolator(new DecelerateInterpolator());
            fadeIn.setDuration(1000);
            zoomImg.startAnimation(fadeIn);
            zoomImg.setVisibility(View.INVISIBLE);
            background.setVisibility(View.VISIBLE);
        });

    }
    private void showImage(int currentPosition) {
        String imageUrl = imgUrls.get(currentPosition);
        Glide.with(this)
                .load(imageUrl)
                .into(image);

        fadeInImage();
    }
    private void fadeInImage() {
        Animation fadeIn = new AlphaAnimation(0,1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(1000);
        image.startAnimation(fadeIn);
    }
    public void onNextButtonClick(View view) {
        currentPosition++;
        if (currentPosition >= imgUrls.size()) {
            currentPosition = 0;
        }
        showImage(currentPosition);
    }

    public void onPreviousButtonClick(View view) {
        currentPosition--;
        if (currentPosition < 0) {
            currentPosition = imgUrls.size() - 1;
        }
        showImage(currentPosition);
    }

    public void call(View view) {
        String myNum=phone.getText().toString();
        Intent i=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+myNum));
        startActivity(i);
    }
}