package com.example.tourismapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class viewCard extends AppCompatActivity{
TextView name,rating,place,descript;
ImageButton wishlist;
ImageView im,zoomImg;
ArrayList<String> ImgUrlsList;
int currentPosition;
RelativeLayout background;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_card);
        descript = findViewById(R.id.descript);
        locations LOC = getIntent().getParcelableExtra("Array");

        background = findViewById(R.id.background);

        name = findViewById(R.id.name);
        String Name = LOC.getLoc_name();
        name.setText(Name);

        im = findViewById(R.id.image);

        String imgURL = LOC.getImage();
        zoomImg = findViewById(R.id.zoomImg);


        rating = findViewById(R.id.rating);
        String Rating = ""+LOC.getRating();
        rating.setText(Rating);

        place = findViewById(R.id.place);
        String Place = LOC.getLoc_place();
        place.setText(Place);

        final boolean[] isWish = {LOC.isWishlist()};
        Log.d("checking Wish","WISH"+isWish[0]);
        wishlist = findViewById(R.id.wishlist);

        if(isWish[0]){
            wishlist.setImageResource(R.drawable.baseline_favorite_24);
        }else{
            wishlist.setImageResource(R.drawable.baseline_favorite_border_24);
        }


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUsr = FirebaseAuth.getInstance().getCurrentUser();
        String currentUID = currentUsr != null ? currentUsr.getUid() : null;
        DatabaseReference locRef = myRef.child("Tourism").child("Locations").child(Name);

        DatabaseReference wishRef = myRef.child("Wishlist").child(currentUID);
        wishlist.setOnClickListener(v -> {
            if(isWish[0]){
                wishlist.setImageResource(R.drawable.baseline_favorite_border_24);
                wishRef.child(Name).removeValue();
                locRef.child("Wishlist").child(currentUID).setValue(false);
                isWish[0] = false;
                Toast.makeText(this, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
            }else{
                wishlist.setImageResource(R.drawable.baseline_favorite_24);
                LOC.setWishlist(false);
                wishRef.child(Name).setValue(LOC);
                locRef.child("Wishlist").child(currentUID).setValue(true);
                isWish[0] = true;
                Toast.makeText(this, "Added to Wishlist", Toast.LENGTH_SHORT).show();
            }
        });




        DatabaseReference imgURLS = locRef.child("imgUrls");
        ImgUrlsList = new ArrayList<>();

        currentPosition = 0;
        imgURLS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ImgUrlsList.clear();
                ImgUrlsList.add(imgURL);
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String imageUrls = dataSnapshot.getValue(String.class);
                    ImgUrlsList.add(imageUrls);
                }
//            Log.d("Image array List","List: "+ImgUrlsList);
                showImage(currentPosition);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        showImage(currentPosition);
        im.setOnClickListener(v -> {
            // Add zoom animation to the ImageView
//                Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
            Animation fadeIn = new AlphaAnimation(0,1);
            fadeIn.setInterpolator(new DecelerateInterpolator());
            fadeIn.setDuration(1000);
                String zoomImgUrl = ImgUrlsList.get(currentPosition);
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


        FirebaseFirestore desc = FirebaseFirestore.getInstance();
        desc.collection("Description").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if(document.contains(Name))
                        descript.setText(document.getString(Name));
                    else
                        descript.setText("No data available");
                }
            }
            else{
                descript.setText("Unable to retrieve");
            }
        });

    }

    private void showImage(int currentPosition) {
        String imageUrl = ImgUrlsList.get(currentPosition);
                Glide.with(this)
                .load(imageUrl)
                .into(im);

                fadeInImage();
    }

    private void fadeInImage() {
        Animation fadeIn = new AlphaAnimation(0,1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(1000);
        im.startAnimation(fadeIn);
    }
    public void onNextButtonClick(View view) {
        currentPosition++;
        if (currentPosition >= ImgUrlsList.size()) {
            currentPosition = 0;
        }
        showImage(currentPosition);
    }

    public void onPreviousButtonClick(View view) {
        currentPosition--;
        if (currentPosition < 0) {
            currentPosition = ImgUrlsList.size() - 1;
        }
        showImage(currentPosition);
    }

    public void viewHotelPage(View view) {
        Intent i =new Intent(getApplicationContext(),hotelPage.class);
        i.putExtra("name",name.getText().toString());
        startActivity(i);
    }
}
