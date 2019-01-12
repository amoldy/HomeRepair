package com.example.janieamyot.chippy;

public class Rating {
    private int rating;
    private String comment;
    private Booking booking;
    private int ratingID;

    public Rating (int rating, Booking booking){
        this.booking = booking;
        this.rating = rating;
    }
    public Rating (int rating, int ratingID, Booking booking){
        this.booking = booking;
        this.rating =rating;
        this.ratingID = ratingID;
    }
    public Rating (int rating, int ratingID, String comment, Booking booking){
        this.booking= booking;
        this.rating=rating;
        this.comment=comment;
        this.ratingID=ratingID;
    }

    public Rating (int rating, int ratingID, String comment){
        this.rating = rating;
        this.ratingID = ratingID;
        this.comment = comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public int getRating(){
        return rating;
    }
    public String getComment(){
        return comment;
    }

    public int getRatingID() {
        return ratingID;
    }

    public void setRatingID(int ratingID) {
        this.ratingID = ratingID;
    }

    public Booking getBooking() {
        return booking;
    }
}
