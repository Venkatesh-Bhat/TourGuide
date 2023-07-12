package com.example.tourismapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import soup.neumorphism.NeumorphCardView;

public class hotelPage extends AppCompatActivity {
RecyclerView recyclerView;
private DatabaseReference myRef;
private ArrayList<Hotel> HotelList;
private HotelAdapter hotelAdapter;
    String loc_name;
    NeumorphCardView AddHotel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_page);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);

        AddHotel = findViewById(R.id.AddHotel);
        AddHotel.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(),addHotel.class);
            i.putExtra("loc_name",loc_name);
            startActivity(i);
        });

        Intent i = getIntent();
        loc_name = i.getStringExtra("name");

        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        
        myRef = FirebaseDatabase.getInstance().getReference();
        
        HotelList = new ArrayList<>();
        ClearAll();

        getDataFromFirebase();

    }

    private void getDataFromFirebase() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.show();
        Query query = myRef.child("Hotels").child(loc_name);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClearAll();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Hotel hotel = new Hotel();
                    hotel.setImg(dataSnapshot.child("image").getValue().toString());
                    hotel.setName(dataSnapshot.child("name").getValue().toString());
                    hotel.setPhone(dataSnapshot.child("phone").getValue().toString());
                    hotel.setRating(dataSnapshot.child("rating").getValue().toString());
                    hotel.setPrice(dataSnapshot.child("price").getValue().toString());
                    hotel.setLoc_name(loc_name);
                    HotelList.add(hotel);
                }
                pd.dismiss();
                Parcelable recViewPos = recyclerView.getLayoutManager().onSaveInstanceState();
                recyclerView.getLayoutManager().onRestoreInstanceState(recViewPos);
                hotelAdapter = new HotelAdapter(getApplicationContext(),HotelList);
                recyclerView.setAdapter(hotelAdapter);
                hotelAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(hotelPage.this, "No hotels Nearby!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ClearAll() {
        if(HotelList != null){
            HotelList.clear();

            if(hotelAdapter != null){
                hotelAdapter.notifyDataSetChanged();
            }
        }
        HotelList = new ArrayList<>();
    }
}