package com.example.janieamyot.chippy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class ServiceProviderProfile extends AppCompatActivity {
    private Bundle bundle;
    private ServiceProvider serviceProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_profile);
        Intent intent = this.getIntent();
        bundle = intent.getExtras();
        serviceProvider = (ServiceProvider) bundle.get("Account");
        MyDBHandler dbHandler = new MyDBHandler(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        countriesList(serviceProvider);
        if ((bundle != null) && (dbHandler.spProfileExists(serviceProvider.getUserName()))) {
            ServiceProvider sP = (ServiceProvider) bundle.get("Account");
            EditText field = findViewById(R.id.spEditStreetNumber);
            field.setText(Integer.toString(sP.getStreetNumber()));
            field = findViewById(R.id.spEditApartmentNumber);
            field.setText(sP.getApartmentNumber());
            field = findViewById(R.id.spEditStreetName);
            field.setText(sP.getStreetName());
            field = findViewById(R.id.spEditCity);
            field.setText(sP.getCity());
            field = findViewById(R.id.spEditPhone);
            field.setText(sP.getPhoneNumber());
            field = findViewById(R.id.spEditCompany);
            field.setText(sP.getCompany());
            field = findViewById(R.id.spEditDescription);
            field.setText(sP.getDescription());
            RadioGroup licensed = findViewById(R.id.licensedGroup);
            if(sP.isLicensed()){
                licensed.check(R.id.spLicensedYes);
            } else {
                licensed.check(R.id.spLicensedNo);
            }
        }
    }

    public void countriesList(ServiceProvider serviceProvider){
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }

        Collections.sort(countries);
        countries.remove("Canada");
        countries.add(0,"Canada");
        countries.remove("United States");
        countries.add(1,"United States");

        Spinner spinner = findViewById(R.id.spinnerCountries);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(countryAdapter);

        int spinnerPosition = countryAdapter.getPosition(serviceProvider.getCountry());
        spinner.setSelection(spinnerPosition);
    }
    public void onSaveClick(View view){
        EditText field;
        boolean flag= true;
        MyDBHandler dbHandler = new MyDBHandler(this);
        field = findViewById(R.id.spEditStreetName);
        String streetName = field.getText().toString();
        if (!streetName.equals("")){
            if(dbHandler.spProfileExists(serviceProvider.getUserName())) {
                dbHandler.editStreetName(serviceProvider.getUserName(), streetName);
            }
                serviceProvider.setStreetName(streetName);
        }
        else{
            field.getText().clear();
            Toast.makeText(getApplicationContext(), "Street name cannot be empty.", Toast.LENGTH_LONG).show();
            flag = false;
        }
        field = findViewById(R.id.spEditStreetNumber);
        String streetNumber = field.getText().toString();
        if (streetNumber.equals("")){
            field.getText().clear();
            Toast.makeText(getApplicationContext(), "Street number cannot be empty.", Toast.LENGTH_LONG).show();
            flag = false;
        }
        else {
            if (TextUtils.isDigitsOnly(streetNumber)) {
                if (dbHandler.spProfileExists(serviceProvider.getUserName())) {
                    dbHandler.editStreetNumber(serviceProvider.getUserName(), Integer.parseInt(streetNumber));
                }
                serviceProvider.setStreetNumber(Integer.parseInt(streetNumber));

            } else {
                field.getText().clear();
                Toast.makeText(getApplicationContext(), "Street number cannot be have non-numerical characters", Toast.LENGTH_LONG).show();
                flag = false;
            }
        }

        //optional apartment number so it can be blank
        field = findViewById(R.id.spEditApartmentNumber);
        String apartmentNumber = field.getText().toString();
        if(dbHandler.spProfileExists(serviceProvider.getUserName())) {
            dbHandler.editApartmentNumber(serviceProvider.getUserName(), apartmentNumber);
        }
        serviceProvider.setApartmentNumber(apartmentNumber);

        field = findViewById(R.id.spEditCity);
        String city = field.getText().toString();
        if (!city.equals("")){
            if(dbHandler.spProfileExists(serviceProvider.getUserName())) {
            dbHandler.editCity(serviceProvider.getUserName(), city);
            }
            serviceProvider.setCity(city);
        }
        else{
            field.getText().clear();
            Toast.makeText(getApplicationContext(), "City cannot be empty.", Toast.LENGTH_LONG).show();
            flag = false;
        }
        Spinner spinner = findViewById(R.id.spinnerCountries);
        String country = spinner.getSelectedItem().toString();
        if (!country.equals("")){
            if(dbHandler.spProfileExists(serviceProvider.getUserName())) {
                dbHandler.editCountry(serviceProvider.getUserName(), country);
            }
            serviceProvider.setCountry(country);
        }
        else{
            field.getText().clear();
            Toast.makeText(getApplicationContext(), "Country cannot be empty.", Toast.LENGTH_LONG).show();
            flag = false;
        }
        field = findViewById(R.id.spEditPhone);
        String phoneNumber = field.getText().toString();
        if (!phoneNumber.equals("") ){
            String str1 = phoneNumber.substring(0,phoneNumber.length());
            if(TextUtils.isDigitsOnly(str1) ) {
                if(dbHandler.spProfileExists(serviceProvider.getUserName())) {
                    dbHandler.editPhoneNumber(serviceProvider.getUserName(), phoneNumber);
                }
                serviceProvider.setPhoneNumber(phoneNumber);
            }
            else{
                field.getText().clear();
                Toast.makeText(getApplicationContext(), "Phone number should only have digits.", Toast.LENGTH_LONG).show();
                flag = false;
            }
        }
        else{
            field.getText().clear();
            Toast.makeText(getApplicationContext(), "Phone number should be entered.", Toast.LENGTH_LONG).show();
            flag = false;
        }
        field = findViewById(R.id.spEditCompany);
        String company = field.getText().toString();
        if (!company.equals("")){
            if(dbHandler.spProfileExists(serviceProvider.getUserName())) {
                dbHandler.editCompany(serviceProvider.getUserName(), company);
            }
            serviceProvider.setCompany(company);
        }
        else{
            field.getText().clear();
            Toast.makeText(getApplicationContext(), "Company cannot be empty.", Toast.LENGTH_LONG).show();
            flag = false;
        }

        //description is optional so it can be blank
        field = findViewById(R.id.spEditDescription);
        String description = field.getText().toString();
        if(dbHandler.spProfileExists(serviceProvider.getUserName())) {
            dbHandler.editDescription(serviceProvider.getUserName(), description);
        }
        serviceProvider.setDescription(description);

        //license is optional so it can be blank
        RadioButton licensedYes = findViewById(R.id.spLicensedYes);
        RadioButton licensedNo = findViewById(R.id.spLicensedNo);
        if (licensedYes.isChecked()){
            if(dbHandler.spProfileExists(serviceProvider.getUserName())) {
                dbHandler.editIsLicensed(serviceProvider.getUserName(), true);
            }
            serviceProvider.setLicensed(true);
        }
        if (licensedNo.isChecked()){
            if(dbHandler.spProfileExists(serviceProvider.getUserName())) {
                dbHandler.editIsLicensed(serviceProvider.getUserName(), false);
            }
            serviceProvider.setLicensed(false);
        }
        if(flag) {
            if (!dbHandler.spProfileExists(serviceProvider.getUserName())) {
                dbHandler.completeSpProfile(serviceProvider);
                if (dbHandler.spProfileExists(serviceProvider.getUserName())) {
                    Intent intent = new Intent(getApplicationContext(), ServiceProviderWelcomePage.class);
                    bundle.putSerializable("Account", serviceProvider);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Does not exist", Toast.LENGTH_LONG).show();
                }
            } else {
                Intent intent = new Intent(getApplicationContext(), ServiceProviderWelcomePage.class);
                bundle.putSerializable("Account", serviceProvider);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
        dbHandler.close();
    }
    public void onCancelClick(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this);
        if (!dbHandler.spProfileExists(serviceProvider.getUserName())) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(getApplicationContext(), ServiceProviderWelcomePage.class);
            bundle.putSerializable("Account", serviceProvider);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        dbHandler.close();
    }

}
