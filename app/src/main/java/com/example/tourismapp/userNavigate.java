package com.example.tourismapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import soup.neumorphism.NeumorphCardView;

public class userNavigate extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_navigate);
        NeumorphCardView chooseLoc = findViewById(R.id.CHoseLocs);
        NeumorphCardView logout = findViewById(R.id.Logout);
        NeumorphCardView favLoc = findViewById(R.id.FavLoc);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) NeumorphCardView request = findViewById(R.id.requests);
        ImageView gifImageView = findViewById(R.id.gifImageView);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) NeumorphCardView addHotel = findViewById(R.id.ReqHotels);
        Glide.with(this).asGif().load(R.drawable.flyingairplaneunscreen).into(gifImageView);

//        Bitmap bitmapImage = BitmapFactory.decodeResource(getResources(), R.drawable.plane);
//        int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
//        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
//        gifImageView.setImageBitmap(scaled);


        chooseLoc.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),MainActivity.class)));

        favLoc.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),wishList.class)));
        addHotel.setOnClickListener(v -> {
            startActivity(new Intent(userNavigate.this, addHotel.class));
        });

        request.setOnClickListener(v -> {
            startActivity(new Intent(userNavigate.this,ViewRequest.class));
        });

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(userNavigate.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(userNavigate.this,loginOrRegister.class));
            finish();
        });
    }
}