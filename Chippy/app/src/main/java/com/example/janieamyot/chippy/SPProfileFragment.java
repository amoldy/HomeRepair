package com.example.janieamyot.chippy;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SPProfileFragment extends Fragment{

    Bundle bundle;
    private ServiceProvider serviceProvider;
    private Button editButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_sp_profile, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        bundle = intent.getExtras();
        serviceProvider = (ServiceProvider) bundle.get("Account");
        
        TextView name = getView().findViewById(R.id.spNameField);
        name.setText(extractAccount(serviceProvider)[0]);
        TextView lastName = getView().findViewById(R.id.spLastNameField);
        lastName.setText(extractAccount(serviceProvider)[1]);
        TextView userName = getView().findViewById(R.id.spUserNameField);
        userName.setText(extractAccount(serviceProvider)[2]);
        TextView email = getView().findViewById(R.id.spEmailField);
        email.setText(extractAccount(serviceProvider)[3]);
        TextView address = getView().findViewById(R.id.spAddressField);
        if (!extractAccount(serviceProvider)[5].equals("")) {
            String setText = extractAccount(serviceProvider)[4] +" " + extractAccount(serviceProvider)[6] + " Apartment " + extractAccount(serviceProvider)[5] + " " + extractAccount(serviceProvider)[7] + " " + extractAccount(serviceProvider)[8];
            address.setText(setText);
        }
        else{
            String setText = extractAccount(serviceProvider)[4] +" " + extractAccount(serviceProvider)[6] + " " + extractAccount(serviceProvider)[7] + " " + extractAccount(serviceProvider)[8];
            address.setText(setText);
        }
        TextView phone = getView().findViewById(R.id.spPhoneField);
        phone.setText(extractAccount(serviceProvider)[9]);
        TextView company = getView().findViewById(R.id.spCompanyField);
        company.setText(extractAccount(serviceProvider)[10]);
        TextView description = getView().findViewById(R.id.spDescriptionField);
        description.setText(extractAccount(serviceProvider)[11]);
        TextView licensed = getView().findViewById(R.id.spLicensedField);
        licensed.setText(extractAccount(serviceProvider)[12]);

        editButton = getActivity().findViewById(R.id.spEditButton);
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onEditClick(v);
            }
        });
    }

    private String[] extractAccount(ServiceProvider serviceProvider){
        String[] str = new String[13];
        str[0] = serviceProvider.getName();
        str[1] = serviceProvider.getLastName();
        str[2] = serviceProvider.getUserName();
        str[3] = serviceProvider.getEmail();
        str[4] = serviceProvider.getStreetNumber().toString();
        str[5] = serviceProvider.getApartmentNumber();
        str[6] = serviceProvider.getStreetName();
        str[7] = serviceProvider.getCity();
        str[8] = serviceProvider.getCountry();
        str[9] = serviceProvider.getPhoneNumber();
        str[10] = serviceProvider.getCompany();
        str[11] = serviceProvider.getDescription();
        if(serviceProvider.isLicensed() == true){
            str[12] = "Yes";
        }
        else{
            str[12] = "No";
        }
        return str;
    }

    public void onEditClick(View view){
        Intent intent = new Intent(getActivity(), ServiceProviderProfile.class);
        bundle.putSerializable("Account", this.serviceProvider);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
