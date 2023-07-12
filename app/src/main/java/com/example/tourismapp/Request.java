package com.example.tourismapp;

public class Request {
    String HotelName;
    String Location;
    String Status;
    Request(){}
    Request(String HotelName,String Location,String Status){
        this.HotelName = HotelName;
        this.Location = Location;
        this.Status = Status;
    }

    public String getHotelName() {
        return HotelName;
    }

    public String getLocation() {
        return Location;
    }

    public String getStatus() {
        return Status;
    }

    public void setHotelName(String hotelName) {
        HotelName = hotelName;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
