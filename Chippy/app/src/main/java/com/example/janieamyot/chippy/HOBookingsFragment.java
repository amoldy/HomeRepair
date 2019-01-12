package com.example.janieamyot.chippy;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HOBookingsFragment extends Fragment {

    Bundle bundle;
    private HomeOwner homeOwner;
    Dialog dialog;
    //Booking bookingSelected;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_ho_bookings, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = getActivity().getIntent();
        bundle = intent.getExtras();
        homeOwner = (HomeOwner) bundle.get("Account");

        //this list view displays the bookings
        ListView bookingList = getActivity().findViewById(R.id.hoBookingList);
        final ArrayList<ListItem> list = displayBookings();

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(list.get(position).getTitle());
                text2.setText(list.get(position).getSubTitle());
                return view;
            }
        };
        bookingList.setAdapter(adapter);

        bookingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyDBHandler dbHandler = new MyDBHandler(getActivity());
                ListItem bookingItem = list.get(position);
                Booking bookingSelected = dbHandler.findBookingbyID(bookingItem.getId());
                dbHandler.close();

                if (bookingSelected.getRating().getRating() == 0){
                    rateServiceProvider(bookingSelected);
                }
            }
        });
    }

    private ArrayList<ListItem> displayBookings(){

        String title;
        String subtitle;
        long bookingId;
        ArrayList<ListItem> listBookings = new ArrayList<>();
        MyDBHandler dbHandler = new MyDBHandler(getActivity());
        ArrayList<Booking> bookingList = dbHandler.findAllBookingsbyHO(homeOwner.getUserName());

        if (bookingList == null) {
            return listBookings;
        }
        for (Booking booking : bookingList) {

            title = booking.getServiceProvider().getName() + " : " + booking.getService().getName();
            subtitle = booking.toString();
            bookingId = booking.getBookingId();

            listBookings.add(new ListItem(title, subtitle, bookingId));
        }

        dbHandler.close();
        return listBookings;
    }

    private void rateServiceProvider(final Booking booking){
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rating);
        TextView header = dialog.findViewById(R.id.rateHeader);
        Button saveRatingButton = dialog.findViewById(R.id.saveRatingButton);
        Button cancelRatingButton = dialog.findViewById(R.id.cancelRatingButton);
        final RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        final EditText reviewField = dialog.findViewById(R.id.reviewField);
        String rate = "Rate " + booking.getServiceProvider().getName();
        header.setText(rate);

        saveRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDBHandler dbHandler = new MyDBHandler(getActivity());
                int rating = 5;
                rating = (int)ratingBar.getRating();
                booking.getRating().setRating(rating);
                booking.getRating().setComment(reviewField.getText().toString());
                dbHandler.addRating(booking.getRating());
                int sum=0;
                double avgRating;
                ArrayList<Integer> variables = dbHandler.findRatingsforSp(booking.getServiceProvider().getUserName());

                 if ( variables != null) {
                    for (int i : variables) {
                        sum = i + sum;
                    }
                    avgRating = sum/variables.size();
                }else{
                    avgRating=rating;
                }

                dbHandler.updateServiceProviderRating(booking.getServiceProvider().getUserName(), avgRating);
                dbHandler.close();
                Toast.makeText(getActivity(),"Rated: " + String.valueOf(rating) + " Stars",Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        });

        cancelRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Clicked CANCEL", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
