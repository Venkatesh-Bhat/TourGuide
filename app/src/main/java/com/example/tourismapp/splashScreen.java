package com.example.tourismapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import soup.neumorphism.NeumorphImageView;

public class splashScreen extends AppCompatActivity {
TextView app,tour;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) NeumorphImageView iconImg = findViewById(R.id.iconImg);

        tour = findViewById(R.id.tour);
        app = findViewById(R.id.app);

        Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_side);
        iconImg.startAnimation(slideAnimation);
        Animation fadeIn = new AlphaAnimation(0,1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(5000);
        tour.startAnimation(fadeIn);
        app.startAnimation(fadeIn);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),loginOrRegister.class));
                finish();
            }
        },3000);
    }
}