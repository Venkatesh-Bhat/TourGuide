package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
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
public class locsAdapter extends RecyclerView.Adapter<locsAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<locations> locationsArrayList;
    private DatabaseReference wishListRef;

//    public locsAdapter(Context context,ArrayList<locations> locationsArrayList){
//        this.context = context;
//        this.locationsArrayList = locationsArrayList;
//    }

    public locsAdapter(Context context, DatabaseReference wishListRef, ArrayList<locations> locationsArrayList) {
        this.context = context;
        this.wishListRef = wishListRef;
        this.locationsArrayList = locationsArrayList;
    }

    @NonNull
    @Override
    public locsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull locsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.locNameTV.setText(locationsArrayList.get(position).getLoc_name());
        holder.locPlaceTV.setText(locationsArrayList.get(position).getLoc_place());
        holder.ratingTV.setText("" + locationsArrayList.get(position).getRating());

        //        Using Glide Library to display Images
        Glide.with(context)
                .load(locationsArrayList.get(position).getImage())
                .into(holder.locImgIV);


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser CurrentUsr = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference wishRef = myRef.child("Tourism").child("Locations");
        String currentUID = CurrentUsr.getUid();


        final boolean[] isAddedToWishlist = {locationsArrayList.get(position).isWishlist()};
        if (isAddedToWishlist[0]) {
            holder.wishlistDV.setImageResource(R.drawable.baseline_favorite_24);
        } else {
            holder.wishlistDV.setImageResource(R.drawable.baseline_favorite_border_24);
        }

        holder.wishlistDV.setOnClickListener(v -> {

            if (isAddedToWishlist[0]) {
                holder.wishlistDV.setImageResource(R.drawable.baseline_favorite_border_24);
                wishListRef.child(locationsArrayList.get(position).getLoc_name()).removeValue();
                wishRef.child(locationsArrayList.get(position).getLoc_name()).child("Wishlist").child(currentUID).setValue(false);
                locationsArrayList.get(position).setWishlist(false);
                Toast.makeText(context, "Removed from wishlist", Toast.LENGTH_SHORT).show();
                isAddedToWishlist[0] = false;
            } else {
                holder.wishlistDV.setImageResource(R.drawable.baseline_favorite_24);
                wishListRef.child(locationsArrayList.get(position).getLoc_name()).setValue(locationsArrayList.get(position));
//                wishListRef.child(locationsArrayList.get(position).getLoc_name()).child("wishlist").removeValue();
                wishRef.child(locationsArrayList.get(position).getLoc_name()).child("Wishlist").child(currentUID).setValue(true);
                locationsArrayList.get(position).setWishlist(true);
                isAddedToWishlist[0] = true;
                Toast.makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnClickListener(v -> {
            Context context1 = holder.itemView.getContext();
            Intent i = new Intent(context1,viewCard.class);
            Log.d("Adapter Wish","wish:"+locationsArrayList.get(position).isWishlist());
//            i.putExtra("image",locationsArrayList.get(position).getImage());
//            i.putExtra("name",locationsArrayList.get(position).getLoc_name());
//            i.putExtra("place",locationsArrayList.get(position).getLoc_place());
//            i.putExtra("rating",locationsArrayList.get(position).getRating());
//            i.putExtra("wishlist",locationsArrayList.get(position).isWishlist());
            i.putExtra("WISH",locationsArrayList.get(position).isWishlist());
            i.putExtra("Array",locationsArrayList.get(position));
            context1.startActivity(i);
        });

    }


    @Override
    public int getItemCount() {
        return locationsArrayList.size();
    }

    public void filterList(ArrayList<locations> filteredList){
        locationsArrayList = filteredList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView locImgIV;
        TextView locNameTV;
        TextView locPlaceTV;
        TextView ratingTV;
        ImageButton wishlistDV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locImgIV = itemView.findViewById(R.id.locs);
            locNameTV = itemView.findViewById(R.id.locs_name);
            locPlaceTV = itemView.findViewById(R.id.locs_place);
            ratingTV = itemView.findViewById(R.id.ratings);
            wishlistDV = itemView.findViewById(R.id.wish);
        }
    }
}
