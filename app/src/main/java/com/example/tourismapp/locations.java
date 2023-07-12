package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

public class locations implements Parcelable {
    private String loc_name;
    private String loc_place;
    private String rating;
    private String image;
    private  boolean wishlist;

    public locations(){

    }

    public locations(String image,String rating,String loc_name,String loc_place, boolean wishlist){
        this.image = image;
        this.rating = rating;
        this.loc_name = loc_name;
        this.loc_place = loc_place;
        this.wishlist = wishlist;
    }

    @SuppressLint("NewApi")
    protected locations(Parcel in) {
        loc_name = in.readString();
        loc_place = in.readString();
        rating = in.readString();
        image = in.readString();
        wishlist = in.readBoolean();
    }



    //GETTER AND SETTER
    public String getLoc_name() {
        return loc_name;
    }
    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }
    public String getLoc_place() {
        return loc_place;
    }
    public void setLoc_place(String loc_place) {
        this.loc_place = loc_place;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }

    public boolean isWishlist() {
        return wishlist;
    }

    public void setWishlist(boolean wishlist) {
        this.wishlist = wishlist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressLint("NewApi")
    @Override
    public  void writeToParcel(Parcel dest, int flags){
        dest.writeString(loc_name);
        dest.writeString(loc_place);
        dest.writeString(rating);
        dest.writeString(image);
        dest.writeBoolean(wishlist);
    }

    public static final Creator<locations> CREATOR = new Creator<locations>() {
        @Override
        public locations createFromParcel(Parcel source) {
            return new locations(source);
        }

        @Override
        public locations[] newArray(int size) {
            return new locations[size];
        }
    };

}
