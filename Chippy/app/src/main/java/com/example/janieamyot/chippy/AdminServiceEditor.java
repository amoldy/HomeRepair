package com.example.janieamyot.chippy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AdminServiceEditor extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Bundle bundle;
    public String categorySel;


    public static final Map<String, Category> catMap;
    static {
        Map<String, Category> tmp = new HashMap<>();
        tmp.put("Plumbing", new Category("Plumbing"));
        tmp.put("Auto", new Category("Auto"));
        tmp.put("Electrical", new Category("Electrical"));
        tmp.put("Yard-work", new Category("Yard-work"));
        tmp.put("Renovations", new Category("Renovations"));
        tmp.put("Heating", new Category("Heating"));
        tmp.put("Housekeeping", new Category("Housekeeping"));
        tmp.put("Care-taking", new Category("Care-taking"));
        tmp.put("Miscellaneous", new Category("Miscellaneous"));
        catMap = Collections.unmodifiableMap(tmp);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_service_editor);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        Intent intent = this.getIntent();
        bundle = intent.getExtras();

        if (bundle != null) {
            Service service = (Service) bundle.get("Service");
            EditText field = findViewById(R.id.name);
            field.setText(service.getName());
            field = findViewById(R.id.rate);
            field.setText(Double.toString(service.getHourlyRate()));
        }
        //Create Spinner
        Spinner spinnerCat = findViewById(R.id.category_drop);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.CategoryArr, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCat.setAdapter(adapter);
        spinnerCat.setOnItemSelectedListener(this);

        if (bundle != null){
            Service service = (Service) bundle.get("Service");
            int spinnerPosition = adapter.getPosition(service.getCategory().getLabel());
            spinnerCat.setSelection(spinnerPosition);
        }
    }
    public void onClickSave(View view) {
        boolean edit;

        Service service = null;
        if (bundle != null) {
            edit = true;
            service = (Service) bundle.get("Service");
        } else {
            edit = false;
        }

        EditText field;
        MyDBHandler dbHandler = new MyDBHandler(this);
        boolean validInputs = true;



        field = findViewById(R.id.name);
        String name = field.getText().toString();
        name = name.trim();
        if (dbHandler.findAccountByUserName(name) != null && !edit) {
            field.getText().clear();
            Toast.makeText(getApplicationContext(), "Service already exists.", Toast.LENGTH_LONG).show();
            validInputs = false;
        }
        if (name.equals("")) {
            field.getText().clear();
            Toast.makeText(getApplicationContext(), "Service name cannot be empty.", Toast.LENGTH_LONG).show();
            validInputs = false;
        }
        if (name.contains("[") || name.contains("]")){
            field.getText().clear();
            Toast.makeText(getApplicationContext(), "Service name cannot contain [ or ].", Toast.LENGTH_LONG).show();
            validInputs = false;
        }

        field = findViewById(R.id.rate);
        Double rate = null;
        try {
            rate = Double.valueOf(field.getText().toString());
            if (rate < 0){
                field.getText().clear();
                Toast.makeText(getApplicationContext(), "Invalid rate.", Toast.LENGTH_LONG).show();
                validInputs = false;
            }
        } catch (NumberFormatException e) {
            field.getText().clear();
            Toast.makeText(getApplicationContext(), "Invalid rate.", Toast.LENGTH_LONG).show();
            validInputs = false;
        }

        if (catMap.get(categorySel) == null) {
            Toast.makeText(getApplicationContext(), "Please select a service category.", Toast.LENGTH_LONG).show();
            validInputs = false;
        }

        dbHandler.close();

        if (validInputs) {
            if (edit) {
                dbHandler.deleteService(service.getName());
            }
            createService(name, rate);
            Account admin = dbHandler.findAccountByUserName("admin");
            Bundle bundle = new Bundle();
            Intent intent = new Intent(getApplicationContext(), AdminWelcomePage.class);
            bundle.putSerializable("Account", admin);
            dbHandler.close();
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    private void createService(String name, double rate) {
        MyDBHandler dbHandler = new MyDBHandler(this);


        Service service = new Service(rate, name, categorySel);
        dbHandler.addService(service);
        dbHandler.close();
    }

    public void onClickCancel(View view){
        MyDBHandler dbHandler = new MyDBHandler(this);
        Account admin = dbHandler.findAccountByUserName("admin");
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getApplicationContext(), AdminWelcomePage.class);
        bundle.putSerializable("Account", admin);
        dbHandler.close();
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String catSelected = parent.getItemAtPosition(position).toString();
        categorySel = catSelected;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
