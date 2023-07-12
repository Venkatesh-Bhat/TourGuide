package com.example.tourismapp;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Hotel implements Parcelable {
    String img;
    String name;
    String rating;
    String phone;
    String price;
    String loc_name;


    Hotel(){}

    Hotel(String img,String name,String rating,String phone,String price,String loc_name){
        this.img = img;
        this.name = name;
        this.rating = rating;
        this.phone = phone;
        this.price = price;
        this.loc_name = loc_name;
    }

    protected Hotel(Parcel in) {
        img = in.readString();
        name = in.readString();
        rating = in.readString();
        phone = in.readString();
        price = in.readString();
        loc_name = in.readString();
    }

    public static final Creator<Hotel> CREATOR = new Creator<Hotel>() {
        @Override
        public Hotel createFromParcel(Parcel in) {
            return new Hotel(in);
        }

        @Override
        public Hotel[] newArray(int size) {
            return new Hotel[size];
        }
    };

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLoc_name() {
        return loc_name;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(img);
        dest.writeString(name);
        dest.writeString(rating);
        dest.writeString(phone);
        dest.writeString(price);
        dest.writeString(loc_name);
    }
}
