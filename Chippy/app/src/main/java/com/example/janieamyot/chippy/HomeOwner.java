package com.example.janieamyot.chippy;

import java.util.ArrayList;

public class HomeOwner extends Account{
    private ArrayList<Booking> bookings;

    public HomeOwner(){
        super();
    }

    public HomeOwner(String name, String lastName, String userName, String password, String email){
    super(name,lastName,userName, password, email);
  }
  public ArrayList<Booking> getBookings(){
        return bookings;
  }
  public void addBooking(Booking booking){
        bookings.add(booking);
    }
}