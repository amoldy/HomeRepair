package com.example.janieamyot.chippy;

import static org.junit.Assert.*;
import org.junit.Test;

public class ServiceCreationTest {

    @Test
    public void checkServiceName(){
        Category aCategory = new Category("Electrical Work");
        Service aService = new Service(180.0, "Light bulb replacement","Electrical" );
        assertEquals("Check the name of the service", "Light bulb replacement", aService.getName());
    }

    @Test
    public void checkServiceHourlyRate(){
        Category aCategory = new Category("Electrical Work");
        Service aService = new Service(180.0, "Light bulb replacement", "Electrical");
        assertEquals("Check the houly rate of the service", "180.0", Double.toString(aService.getHourlyRate()));
    }

    @Test
    public void checkServiceCategory(){
        Category aCategory = new Category("Electrical");
        Service aService = new Service(180.0, "Light bulb replacement", "Electrical");
        assertEquals("Check the category of the service", "Electrical", aService.getCategory().getLabel());
    }
}
