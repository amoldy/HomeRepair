package com.example.janieamyot.chippy;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HOSearchResults extends AppCompatActivity {


    Bundle bundle;
    private HomeOwner homeOwner;
    Dialog dialog;
    ArrayList<ServiceProvider> spList;
    public String startTime;
    final Calendar c = Calendar.getInstance();
    TextView dateSelected;
    Spinner timeSpinner;
    String serviceChosen;
    int year1;
    int month1;
    int day1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ho_search_result);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        Intent intent = this.getIntent();
        bundle = intent.getExtras();
        homeOwner = (HomeOwner) bundle.get("Account");
        spList = (ArrayList<ServiceProvider>) bundle.get("SearchResult");
        final ArrayList<ListItem> list = displayProviders(spList);


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, list) {
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

        ListView providersList = this.findViewById(R.id.providersList);
        providersList.setAdapter(adapter);

        providersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyDBHandler dbHandler = new MyDBHandler(getApplicationContext());
                ListItem bookingItem = list.get(position);
                ServiceProvider spSelected = dbHandler.findServiceProvider(bookingItem.getTitle());
                dbHandler.close();
                makeBooking(spSelected);
            }
        });


    }




    private ArrayList<ListItem> displayProviders(ArrayList<ServiceProvider> spList){

        String title = "";
        String subtitle = "";

        MyDBHandler dbHandler = new MyDBHandler(this);
        ArrayList<ListItem> listProviders = new ArrayList<>();

        if (spList == null) {
            return listProviders;
        }
        for (ServiceProvider sp : spList) {


            title = sp.getUserName();
                subtitle = dbHandler.findSPAverageRating(sp.getUserName()) + "";
            listProviders.add(new ListItem(title, subtitle));
        }

        dbHandler.close();
        return listProviders;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void makeBooking(final ServiceProvider serviceProvider){

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_make_booking);
        TextView header = dialog.findViewById(R.id.bookingHeader);
        Button saveBookingButton = dialog.findViewById(R.id.saveBookingButton);
        Button cancelBookingButton = dialog.findViewById(R.id.cancelBookingButton);
        String title = "Book with " + serviceProvider.getUserName();
        header.setText(title);
        dateSelected = dialog.findViewById(R.id.dateSelected);
        ImageButton dateButton = dialog.findViewById(R.id.dateButton);
        timeSpinner = dialog.findViewById(R.id.bookingTime);
        final Spinner serviceSpinner = dialog.findViewById(R.id.bookingService);

        saveBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDBHandler dbHandler = new MyDBHandler(getApplicationContext());

                Calendar beginTime = Calendar.getInstance();
                beginTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTime));
                beginTime.set(Calendar.DAY_OF_MONTH, day1);
                beginTime.set(Calendar.MONTH, month1);
                beginTime.set(Calendar.YEAR, year1);
                beginTime.set(Calendar.MINUTE, 0);
                Calendar endTime = Calendar.getInstance();
                endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTime)+1);
                endTime.set(Calendar.DAY_OF_MONTH, day1);
                endTime.set(Calendar.MONTH, month1);
                endTime.set(Calendar.YEAR, year1);
                endTime.set(Calendar.MINUTE, 0);

                Booking booking = new Booking(dbHandler.findService(serviceChosen), serviceProvider, homeOwner, beginTime, endTime);
                dbHandler.addBooking(booking);
                dbHandler.close();
                dialog.dismiss();

                Intent intent = new Intent(getApplicationContext(), HomeOwnerWelcomePage.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("Account", homeOwner);
                intent.putExtras(bundle2);
                startActivity(intent);

            }
        });

        cancelBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        dateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setDate(v, serviceProvider);
            }
        });

        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext());
        ArrayList<String> listSPtimes = dbHandler.findAllSpServices(serviceProvider.getUserName());
        ArrayAdapter adapterServices = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, listSPtimes);
        serviceSpinner.setAdapter(adapterServices);
        dbHandler.close();

        serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                serviceChosen = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        dialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ArrayList<Integer> findTimes(ServiceProvider spChosen){

        String day = extractDayOfWeek();
        ArrayList<Integer> availableList = new ArrayList<>();
        try {
            JSONObject availability = new JSONObject(spChosen.getAvailabilities());
            JSONArray dayAvailability = availability.getJSONArray(day);
            for (int i = 0; i < dayAvailability.length(); i++) {
                availableList.add(dayAvailability.optInt(i));
            }
        }catch (JSONException e){

        }

        return availableList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setDate(View v, final ServiceProvider serviceProvider) {

        int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(this,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String dayOfWeek = DateFormat.format("EEEE", new Date(year, monthOfYear, dayOfMonth-1)).toString();
                String date = " " + dayOfMonth + "-" + (monthOfYear+1) + "-" + year;
                String str = dayOfWeek + date;
                dateSelected.setText(str);
                year1=year;
                month1=monthOfYear;
                day1=dayOfMonth;

                if(timeSpinner!=null) {
                    ArrayList<Integer> listSPtimes = findTimes(serviceProvider);
                    ArrayAdapter adapterServices = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, listSPtimes);
                    timeSpinner.setAdapter(adapterServices);

                    timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            startTime = parentView.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }
                    });
                }
            }
        }, year, month, day);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dpd.show();

    }

    private String extractDayOfWeek(){
        if (dateSelected == null) {
            return null;
        }
        String field = dateSelected.getText().toString();
        return (field.substring(0, field.indexOf("y")+1)).toLowerCase();
    }
}

