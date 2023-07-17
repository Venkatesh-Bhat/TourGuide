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

public class wishListAdapter extends RecyclerView.Adapter<wishListAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<locations> locationsArrayList;
    private final DatabaseReference wishRef;

    public wishListAdapter(Context context,ArrayList<locations> locationsArrayList, DatabaseReference wishRef){
        this.context = context;
        this.locationsArrayList = locationsArrayList;
        this.wishRef = wishRef;
    }

    @NonNull
    @Override
    public wishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        return new wishListAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull wishListAdapter.ViewHolder holder, int position) {
        holder.locNameTV.setText(locationsArrayList.get(position).getLoc_name());
        holder.locPlaceTV.setText(locationsArrayList.get(position).getLoc_place());
        holder.ratingTV.setText(""+locationsArrayList.get(position).getRating());

        //        Using Glide Library to display Images
        Glide.with(context)
                .load(locationsArrayList.get(position).getImage())
                .into(holder.locImgIV);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser CurrentUsr = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference wishListRef = myRef.child("Tourism").child("Locations");
        String currentUID = CurrentUsr.getUid();



        final boolean[] isAddedToWishlist = {!locationsArrayList.get(position).isWishlist()};
//        final boolean[] isAddedToWishlist = {wishListRef.child(locationsArrayList.get(position).getLoc_name().toString())};
        if(isAddedToWishlist[0]){
            holder.wishlistDV.setImageResource(R.drawable.baseline_favorite_24);
            locationsArrayList.get(position).setWishlist(true);
        }
        else {
            holder.wishlistDV.setImageResource(R.drawable.baseline_favorite_border_24);
        }

        holder.wishlistDV.setOnClickListener(v -> {

            if(isAddedToWishlist[0]){
                holder.wishlistDV.setImageResource(R.drawable.baseline_favorite_border_24);
                wishRef.child(locationsArrayList.get(position).getLoc_name()).removeValue();
                wishListRef.child(locationsArrayList.get(position).getLoc_name()).child("Wishlist").child(currentUID).setValue(false);
                locationsArrayList.get(position).setWishlist(false);
                Toast.makeText(context, "Removed from wishlist", Toast.LENGTH_SHORT).show();
                isAddedToWishlist[0] = false;
            }
//            else{
//                holder.wishlistDV.setImageResource(R.drawable.baseline_favorite_24);
//                wishRef.child(locationsArrayList.get(position).getLoc_name()).setValue(locationsArrayList.get(position));
////                wishListRef.child(locationsArrayList.get(position).getLoc_name()).child("wishlist").removeValue();
//                wishListRef.child(locationsArrayList.get(position).getLoc_name()).child("Wishlist").child(currentUID).setValue(true);
//                isAddedToWishlist[0]=true;
//                locationsArrayList.get(position).setWishlist(true);
//                Toast.makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT).show();
//            }
        });
        holder.itemView.setOnClickListener(v -> {
            Context context1 = holder.itemView.getContext();
            Intent i = new Intent(context1,viewCard.class);
            i.putExtra("Array",locationsArrayList.get(position));
            context1.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return locationsArrayList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<locations> filteredList) {
        locationsArrayList = filteredList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView locImgIV;
        TextView locNameTV;
        TextView locPlaceTV;
        TextView ratingTV;
        ImageButton wishlistDV;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            locImgIV = itemView.findViewById(R.id.locs);
            locNameTV = itemView.findViewById(R.id.locs_name);
            locPlaceTV = itemView.findViewById(R.id.locs_place);
            ratingTV = itemView.findViewById(R.id.ratings);
            wishlistDV = itemView.findViewById(R.id.wish);
        }
    }
}
