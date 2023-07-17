package com.example.tourismapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class wishList extends AppCompatActivity {
    private DatabaseReference myRef;

    private ArrayList<locations> locationsArrayList;
    private wishListAdapter WishListAdapter;
    RecyclerView wishRV;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference wishRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);


        EditText searchItem = findViewById(R.id.searchItem);
        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }

            private void filter(String text) {
                ArrayList<locations> filteredList= new ArrayList<>();
                for(locations item : locationsArrayList){
                    if(item.getLoc_name().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add(item);
                    }
                }

                WishListAdapter.filterList(filteredList);
            }
        });


        wishRV = findViewById(R.id.wishRv);

        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        wishRV.setLayoutManager(llm);
        wishRV.setHasFixedSize(true);



        myRef = FirebaseDatabase.getInstance().getReference();

        wishRef = myRef.getDatabase().getReference("Wishlist");
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
            wishRef = wishRef.child(currentUserId);
        }


        locationsArrayList = new ArrayList<>();
        ClearAll();

        getDataFromFirebase();

    }

    private void ClearAll() {
        if(locationsArrayList != null){
            locationsArrayList.clear();

            if(WishListAdapter != null){
                WishListAdapter.notifyDataSetChanged();
            }
        }
        locationsArrayList = new ArrayList<>();
    }


    private void getDataFromFirebase() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Wait for a moment");
        pd.show();
        Query query = myRef.child("Wishlist").child(currentUserId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClearAll();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    locations loc = new locations();
                    loc.setImage(snapshot1.child("image").getValue().toString());
                    loc.setLoc_name(snapshot1.child("loc_name").getValue().toString());
                    loc.setRating(snapshot1.child("rating").getValue().toString());
                    loc.setLoc_place(snapshot1.child("loc_place").getValue().toString());

                    loc.setWishlist(Boolean.parseBoolean(snapshot1.child("wishlist").getValue().toString()));
                    locationsArrayList.add(loc);



                }
                pd.dismiss();
                Parcelable recViewPos = wishRV.getLayoutManager().onSaveInstanceState();
                wishRV.getLayoutManager().onRestoreInstanceState(recViewPos);
                WishListAdapter = new wishListAdapter(getApplicationContext(),locationsArrayList,wishRef);
                wishRV.setAdapter(WishListAdapter);
                WishListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}