package com.example.janieamyot.chippy;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

public class Booking {
    private Calendar startTime;
    private Calendar endTime;
    private Rating rating;
    private long bookingId;
    private Service service;
    private ServiceProvider serviceProvider;
    private HomeOwner homeOwner;

    public Booking (Service service, ServiceProvider serviceProvider, HomeOwner homeOwner, Calendar startTime, Calendar endTime){
        this.endTime=endTime;
        this.startTime=startTime;
        //this.bookingId = bookingId;
        this.homeOwner = homeOwner;
        this.service = service;
        this.serviceProvider = serviceProvider;
        this.rating = new Rating(0,this);
    }
    public Booking (Service service, ServiceProvider serviceProvider, HomeOwner homeOwner, Calendar startTime, Calendar endTime, long bookingId){
        this.endTime=endTime;
        this.startTime=startTime;
        this.bookingId = bookingId;
        this.homeOwner = homeOwner;
        this.service = service;
        this.serviceProvider = serviceProvider;
        this.rating = new Rating(0,this);
    }
    public void setRating(Rating rating){
        this.rating=rating;
    }
    public void setStartTime(int day, int hour, int minute){
        startTime = Calendar.getInstance();
        startTime.set(startTime.getTime().getYear(), startTime.getTime().getMonth(), day, hour, minute);
    }
    public void setEndTime(int day, int hour, int minute){
        startTime = Calendar.getInstance();
        startTime.set(startTime.getTime().getYear(), startTime.getTime().getMonth(), day, hour, minute);
    }
    public Calendar getStartTime(){
        return startTime;
    }
    public Calendar getEndTime(){
        return endTime;
    }
    public Rating getRating(){
        return rating;
    }
    public void setBookingId(long bookingId) {
        this.bookingId = bookingId;
    }
    public long getBookingId() {
        return bookingId;
    }

    public HomeOwner getHomeOwner() {
        return homeOwner;
    }

    public Service getService() {
        return service;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setHomeOwner(HomeOwner homeOwner) {
        this.homeOwner = homeOwner;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public String toString(){
        String dayOfWeek = DateFormat.format("EEEE", new Date(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH)-1)).toString();
        String date = " " + startTime.get(Calendar.DAY_OF_MONTH)+ "-" + (startTime.get(Calendar.MONTH)+1) + "-" + startTime.get(Calendar.YEAR);

        String time = " " + startTime.get(Calendar.HOUR_OF_DAY)+ "-" + (endTime.get(Calendar.HOUR_OF_DAY));
        return dayOfWeek + date + time;

    }
}
