package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {
private final Context context;
private final ArrayList<Hotel> HotelList;


public HotelAdapter(Context context, ArrayList<Hotel> HotelList){
        this.context = context;
        this.HotelList = HotelList;
        }

@NonNull
@Override
public HotelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_card,parent,false);
        return new HotelAdapter.ViewHolder(view);
        }

@SuppressLint("SetTextI18n")
@Override
public void onBindViewHolder(@NonNull HotelAdapter.ViewHolder holder, int position) {
        holder.NameTV.setText(HotelList.get(position).getName());
        holder.PhoneTV.setText(HotelList.get(position).getPhone());
        holder.ratingTV.setText(""+HotelList.get(position).getRating());
        holder.PriceTV.setText(HotelList.get(position).getPrice());



        //        Using Glide Library to display Images
        Glide.with(context)
        .load(HotelList.get(position).getImg())
        .into(holder.ImgIV);





        holder.itemView.setOnClickListener(v -> {
        Context context1 = holder.itemView.getContext();
        Intent i = new Intent(context1,viewHotel.class);
        i.putExtra("Array",HotelList.get(position));
        context1.startActivity(i);
        });
        }

@Override
public int getItemCount() {
        return HotelList.size();
        }

public static class ViewHolder extends RecyclerView.ViewHolder {
    ImageView ImgIV;
    TextView NameTV;
    TextView PhoneTV;
    TextView ratingTV;
    TextView PriceTV;
    ImageButton previousIB,nextIB;
    public ViewHolder(@NonNull View itemView){
        super(itemView);
        ImgIV = itemView.findViewById(R.id.image);
        NameTV = itemView.findViewById(R.id.name);
        PhoneTV = itemView.findViewById(R.id.phone);
        ratingTV = itemView.findViewById(R.id.rating);
        PriceTV = itemView.findViewById(R.id.price);
        previousIB = itemView.findViewById(R.id.previous);
        nextIB = itemView.findViewById(R.id.next);
    }
}
}
