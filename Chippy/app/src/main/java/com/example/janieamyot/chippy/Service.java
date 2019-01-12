package com.example.janieamyot.chippy;


import java.io.Serializable;
import java.util.ArrayList;

public class Service implements Serializable {
    private double hourlyRate;
    private String name;
    private Category category;
    private ArrayList<Booking> bookings;

    public Service(double hourlyRate, String name, String categorySpin) {
        this.hourlyRate = hourlyRate;
        this.name = name;
        this.category = AdminServiceEditor.catMap.get(categorySpin);

    }

    public double getHourlyRate(){
        return hourlyRate;
    }

    public String getName(){
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String toString() {
        return "Name: [" + name + "] " + " Rate ($/h) " + hourlyRate + " " + " Category: " + category.getLabel();
    }
    public ArrayList<Booking> getBookings(){
        return bookings;
    }
    public void addBooking(Booking booking){
        bookings.add(booking);
    }
}
