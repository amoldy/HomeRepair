package com.example.janieamyot.chippy;

import static org.junit.Assert.*;
import org.junit.Test;

public class AccountCreationTest {

    @Test
    public void checkAdminAccountName(){
        Admin admin = new Admin("Martin", "Luther King", "mlk115", "!nonviolence173", "test@test.com");
        assertEquals("Check the name of the admin account", "Martin", admin.getName());
    }

    @Test
    public void checkAdminAccountLastName(){
        Admin admin = new Admin("Martin", "Luther King", "mlk115", "!nonviolence173", "test@test.com");
        assertEquals("Check the last name of the admin account", "Luther King", admin.getLastName());
    }

    @Test
    public void checkAdminAccountUserName(){
        Admin admin = new Admin("Martin", "Luther King", "mlk115", "!nonviolence173", "test@test.com");
        assertEquals("Check the user name of the admin account", "mlk115", admin.getUserName());
    }

    @Test
    public void checkAdminAccountPassword(){
        Admin admin = new Admin("Martin", "Luther King", "mlk115", "!nonviolence173", "test@test.com");
        assertEquals("Check the password of the admin account", "!nonviolence173", admin.getPassword());
    }

    @Test
    public void checkAdminAccountEmail(){
        Admin admin = new Admin("Martin", "Luther King", "mlk115", "!nonviolence173", "test@test.com");
        assertEquals("Check the email of the admin account", "test@test.com", admin.getEmail());
    }

    @Test
    public void checkServiceProviderAccountName(){
        ServiceProvider aServiceProvider = new ServiceProvider("Mahatma", "Gandhi", "gandhi254", "$non573violence", "gandhi@gmail.com");
        assertEquals("Check the name of the service provider account", "Mahatma", aServiceProvider.getName());
    }

    @Test
    public void checkServiceProviderAccountLastName(){
        ServiceProvider aServiceProvider = new ServiceProvider("Mahatma", "Gandhi", "gandhi254", "$non573violence", "gandhi@gmail.com");
        assertEquals("Check the last name of the service provider account", "Gandhi", aServiceProvider.getLastName());
    }

    @Test
    public void checkServiceProviderAccountUserName(){
        ServiceProvider aServiceProvider = new ServiceProvider("Mahatma", "Gandhi", "gandhi254", "$non573violence", "gandhi@gmail.com");
        assertEquals("Check the user name of the service provider account", "gandhi254", aServiceProvider.getUserName());
    }

    @Test
    public void checkServiceProviderAccountPassword(){
        ServiceProvider aServiceProvider = new ServiceProvider("Mahatma", "Gandhi", "gandhi254", "$non573violence", "gandhi@gmail.com");
        assertEquals("Check the password of the service provider account", "$non573violence", aServiceProvider.getPassword());
    }

    @Test
    public void checkServiceProviderAccountEmail(){
        ServiceProvider aServiceProvider = new ServiceProvider("Mahatma", "Gandhi", "gandhi254", "$non573violence", "gandhi@gmail.com");
        assertEquals("Check the email of the service provider account", "gandhi@gmail.com", aServiceProvider.getEmail());
    }
}
