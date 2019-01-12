package com.example.janieamyot.chippy;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ServiceProviderTest {
    @Test
    public void checkSpStreetNumber(){
        Service aService = new Service(180.0, "Light bulb replacement","Electrical" );
        ArrayList<Service> services = new ArrayList<Service>();
        services.add(aService);
        ServiceProvider aServiceProvider = new ServiceProvider("Martin", "Luther King", "mlk115", "!nonviolence173", "test@test.com", 123, "3A", "Laurier", "Ottawa", "Canada", "Chippy", "Test", true, "123-123-1234", "availabilities test", services);
        assertEquals("Check the street number of the service provider", "123", Integer.toString(aServiceProvider.getStreetNumber()));
    }

    @Test
    public void checkSpApartmentNumber(){
        Service aService = new Service(180.0, "Light bulb replacement","Electrical" );
        ArrayList<Service> services = new ArrayList<Service>();
        services.add(aService);
        ServiceProvider aServiceProvider = new ServiceProvider("Martin", "Luther King", "mlk115", "!nonviolence173", "test@test.com", 123, "3A", "Laurier", "Ottawa", "Canada", "Chippy", "Test", true, "123-123-1234", "availabilities test", services);
        assertEquals("Check the apartment number of the service provider", "3A", aServiceProvider.getApartmentNumber());
    }

    @Test
    public void checkSpStreetName(){
        Service aService = new Service(180.0, "Light bulb replacement","Electrical" );
        ArrayList<Service> services = new ArrayList<Service>();
        services.add(aService);
        ServiceProvider aServiceProvider = new ServiceProvider("Martin", "Luther King", "mlk115", "!nonviolence173", "test@test.com", 123, "3A", "Laurier", "Ottawa", "Canada", "Chippy", "Test", true, "123-123-1234", "availabilities test", services);
        assertEquals("Check the street name of the service provider", "Laurier", aServiceProvider.getStreetName());
    }

    @Test
    public void checkSpCity(){
        Service aService = new Service(180.0, "Light bulb replacement","Electrical" );
        ArrayList<Service> services = new ArrayList<Service>();
        services.add(aService);
        ServiceProvider aServiceProvider = new ServiceProvider("Martin", "Luther King", "mlk115", "!nonviolence173", "test@test.com", 123, "3A", "Laurier", "Ottawa", "Canada", "Chippy", "Test", true, "123-123-1234", "availabilities test", services);
        assertEquals("Check the city of the service provider", "Ottawa", aServiceProvider.getCity());
    }

    @Test
    public void checkSpCountry(){
        Service aService = new Service(180.0, "Light bulb replacement","Electrical" );
        ArrayList<Service> services = new ArrayList<Service>();
        services.add(aService);
        ServiceProvider aServiceProvider = new ServiceProvider("Martin", "Luther King", "mlk115", "!nonviolence173", "test@test.com", 123, "3A", "Laurier", "Ottawa", "Canada", "Chippy", "Test", true, "123-123-1234", "availabilities test", services);
        assertEquals("Check the country of the service provider", "Canada", aServiceProvider.getCountry());
    }

    @Test
    public void checkSpCompany(){
        Service aService = new Service(180.0, "Light bulb replacement","Electrical" );
        ArrayList<Service> services = new ArrayList<Service>();
        services.add(aService);
        ServiceProvider aServiceProvider = new ServiceProvider("Martin", "Luther King", "mlk115", "!nonviolence173", "test@test.com", 123, "3A", "Laurier", "Ottawa", "Canada", "Chippy", "Test", true, "123-123-1234", "availabilities test", services);
        assertEquals("Check the company name of the service provider", "Chippy", aServiceProvider.getCompany());
    }

    @Test
    public void checkSpDescription(){
        Service aService = new Service(180.0, "Light bulb replacement","Electrical" );
        ArrayList<Service> services = new ArrayList<Service>();
        services.add(aService);
        ServiceProvider aServiceProvider = new ServiceProvider("Martin", "Luther King", "mlk115", "!nonviolence173", "test@test.com", 123, "3A", "Laurier", "Ottawa", "Canada", "Chippy", "Test", true, "123-123-1234", "availabilities test", services);
        assertEquals("Check the description of the service provider", "Test", aServiceProvider.getDescription());
    }

    @Test
    public void checkSpIsLicensed(){
        Service aService = new Service(180.0, "Light bulb replacement","Electrical" );
        ArrayList<Service> services = new ArrayList<Service>();
        services.add(aService);
        ServiceProvider aServiceProvider = new ServiceProvider("Martin", "Luther King", "mlk115", "!nonviolence173", "test@test.com", 123, "3A", "Laurier", "Ottawa", "Canada", "Chippy", "Test", true, "123-123-1234", "availabilities test", services);
        assertEquals("Check if the service provider is licensed", "true", Boolean.toString(aServiceProvider.isLicensed()));
    }

    @Test
    public void checkSpPhoneNumber(){
        Service aService = new Service(180.0, "Light bulb replacement","Electrical" );
        ArrayList<Service> services = new ArrayList<Service>();
        services.add(aService);
        ServiceProvider aServiceProvider = new ServiceProvider("Martin", "Luther King", "mlk115", "!nonviolence173", "test@test.com", 123, "3A", "Laurier", "Ottawa", "Canada", "Chippy", "Test", true, "123-123-1234", "availabilities test", services);
        assertEquals("Check the phone number of the service provider", "123-123-1234", aServiceProvider.getPhoneNumber());
    }
}
