package com.example.janieamyot.chippy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MyDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ChippyDB.db";

    //Accounts table
    private static final String TABLE_ACCOUNT = "accounts";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LASTNAME = "lastName";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USERNAME = "userName";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ACCOUNT_TYPE = "accountType";

    //Services table
    private static final String TABLE_SERVICE = "services";
    private static final String COLUMN_SERVICE_ID = "serviceId";
    private static final String COLUMN_SERVICE_NAME = "name";
    private static final String COLUMN_HOURLY_RATE = "hourlyRate";
    private static final String COLUMN_CATEGORY = "category";

    //Service provider profile table
    private static final String TABLE_SP_PROFILE = "serviceProviderProfile";
    private static final String COLUMN_STREET_NUMBER = "streetNumber";
    private static final String COLUMN_APARTMENT_NUMBER = "apartmentNumber";
    private static final String COLUMN_STREET_NAME = "streetName";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_COUNTRY = "country";
    private static final String COLUMN_COMPANY = "company";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IS_LICENSED = "isLicensed";
    private static final String COLUMN_PHONE_NUMBER = "phoneNumber";
    private static final String COLUMN_AVAILABILITIES = "availabilities";
    private static final String COLUMN_AVERAGE_RATING = "averageRating";

    //Service provider services table
    private static final String TABLE_SP_SERVICES = "serviceProviderServices";

    //Bookings table
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String COLUMN_BOOKING_ID = "bookingId";
    private static final String COLUMN_START_TIME = "startTime";
    private static final String COLUMN_END_TIME = "endTime";
    private static final String COLUMN_USERNAME_HO = "usernameHO";
    private static final String COLUMN_USERNAME_SP = "usernameSP";

    //Ratings table
    private static final String TABLE_RATINGS = "ratings";
    private static final String COLUMN_RATING_ID = "ratingId";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_COMMENT = "comment";


    public MyDBHandler(Context context){
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //Creates the accounts table
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE "+TABLE_ACCOUNT+"("+COLUMN_ACCOUNT_TYPE+" TEXT,"+COLUMN_USERNAME+" TEXT PRIMARY KEY,"+COLUMN_NAME+" TEXT,"+COLUMN_LASTNAME+" TEXT,"+COLUMN_EMAIL+" TEXT,"+COLUMN_PASSWORD+" TEXT"+")";
        db.execSQL(CREATE_ACCOUNTS_TABLE);

        //Creates the services table
        String CREATE_SERVICES_TABLE = "CREATE TABLE "+TABLE_SERVICE+"("+COLUMN_SERVICE_ID+" INTEGER PRIMARY KEY,"+COLUMN_SERVICE_NAME+" TEXT,"+COLUMN_HOURLY_RATE+" TEXT,"+COLUMN_CATEGORY+" TEXT"+")";
        db.execSQL(CREATE_SERVICES_TABLE);

        //Creates the service provider profile table
        String CREATE_SP_PROFILE_TABLE = "CREATE TABLE "+TABLE_SP_PROFILE+"("+COLUMN_USERNAME+" TEXT PRIMARY KEY,"+COLUMN_STREET_NUMBER+" INTEGER,"+COLUMN_APARTMENT_NUMBER+" TEXT,"+COLUMN_STREET_NAME+" TEXT,"+COLUMN_CITY+" TEXT,"+COLUMN_COUNTRY+" TEXT,"+COLUMN_COMPANY+" TEXT,"+COLUMN_DESCRIPTION+" TEXT,"+COLUMN_IS_LICENSED+" TEXT,"+COLUMN_PHONE_NUMBER+" TEXT,"+COLUMN_AVAILABILITIES+" TEXT,"+COLUMN_AVERAGE_RATING+" TEXT,"+"FOREIGN KEY("+COLUMN_USERNAME+") REFERENCES "+TABLE_ACCOUNT+"("+COLUMN_USERNAME+")"+")";
        db.execSQL(CREATE_SP_PROFILE_TABLE);

        //Creates the service provider services table
        String CREATE_SP_SERVICES_TABLE = "CREATE TABLE "+TABLE_SP_SERVICES+"("+COLUMN_USERNAME+" TEXT,"+COLUMN_SERVICE_ID+" INTEGER,"+"FOREIGN KEY("+COLUMN_USERNAME+") REFERENCES "+TABLE_SP_PROFILE+"("+COLUMN_USERNAME+")"+")";
        db.execSQL(CREATE_SP_SERVICES_TABLE);

        //Creates the bookings table
        String CREATE_BOOKINGS_TABLE = "CREATE TABLE "+TABLE_BOOKINGS+"("+COLUMN_BOOKING_ID+" INTEGER PRIMARY KEY,"+COLUMN_START_TIME+" TEXT,"+COLUMN_END_TIME+" TEXT,"+COLUMN_SERVICE_ID+" INTEGER,"+COLUMN_USERNAME_HO+" TEXT,"+COLUMN_USERNAME_SP+" TEXT,"+"FOREIGN KEY("+COLUMN_SERVICE_ID+") REFERENCES "+TABLE_SERVICE+"("+COLUMN_SERVICE_ID+"),"+"FOREIGN KEY("+COLUMN_USERNAME_HO+") REFERENCES "+TABLE_ACCOUNT+"("+COLUMN_USERNAME+"),"+"FOREIGN KEY("+COLUMN_USERNAME_SP+") REFERENCES "+TABLE_SP_PROFILE+"("+COLUMN_USERNAME+")"+")";
        db.execSQL(CREATE_BOOKINGS_TABLE);

        //Creates the ratings table
        String CREATE_RATINGS_TABLE = "CREATE TABLE "+TABLE_RATINGS+"("+COLUMN_RATING_ID+" INTEGER PRIMARY KEY,"+COLUMN_RATING+" INTEGER,"+COLUMN_COMMENT+" TEXT,"+COLUMN_BOOKING_ID+" INTEGER,"+"FOREIGN KEY("+COLUMN_BOOKING_ID+") REFERENCES "+TABLE_BOOKINGS+"("+COLUMN_BOOKING_ID+")"+")";
        db.execSQL(CREATE_RATINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SP_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SP_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);
        onCreate(db);
    }

    //ACCOUNTS TABLE FUNCTIONS

    //To add an account to the database
    public void addAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        String type = "";

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, account.getUserName());
        values.put(COLUMN_NAME, account.getName());
        values.put(COLUMN_LASTNAME, account.getLastName());
        values.put(COLUMN_EMAIL, account.getEmail());
        values.put(COLUMN_PASSWORD, account.getPassword());
        if(account instanceof Admin){
            type = "Admin";
        }else if(account instanceof ServiceProvider){
            type = "ServiceProvider";
        }else if(account instanceof HomeOwner){
            type = "HomeOwner";
        }
        values.put(COLUMN_ACCOUNT_TYPE, type);

        db.insert(TABLE_ACCOUNT, null, values);
        db.close();
    }

    //To find an account by username
    public Account findAccountByUserName(String username){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM "+ TABLE_ACCOUNT +" WHERE "+COLUMN_USERNAME+" = \""+username+"\"";
        Cursor cursor = db.rawQuery(query, null);

        Account account;
        if(cursor.moveToFirst()){
            String type = cursor.getString(0);
            if(type.equals("Admin")){
                account = new Admin();
            }else if(type.equals("ServiceProvider")){
                account = new ServiceProvider();
            }else if(type.equals("HomeOwner")){
                account = new HomeOwner();
            }else{
                account = null;
            }
            if(account != null){
                account.setUserName(cursor.getString(1));
                account.setName(cursor.getString(2));
                account.setLastName(cursor.getString(3));
                account.setEmail(cursor.getString(4));
                account.setPassword(cursor.getString(5));
            }
            cursor.close();
        } else{
            account = null;
        }
        db.close();
        return account;
    }

    //To determine if the admin account already exits
    public boolean adminExists(){
        SQLiteDatabase db = this.getReadableDatabase();

        String accountType = "Admin";
        boolean flag;

        String query = "Select * FROM "+ TABLE_ACCOUNT +" WHERE "+COLUMN_ACCOUNT_TYPE+" = \""+accountType+"\"";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            flag = true;
            cursor.close();
        } else{
            flag = false;
        }
        db.close();
        return flag;
    }

    //To find all the accounts in the database
    public ArrayList<Account> findAllAccounts(){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM "+ TABLE_ACCOUNT;
        Cursor cursor = db.rawQuery(query, null);

        Account account;
        ArrayList<Account> accounts = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                String type = cursor.getString(0);
                if(type.equals("Admin")){
                    account = new Admin();
                }else if(type.equals("ServiceProvider")){
                    account = new ServiceProvider();
                }else if(type.equals("HomeOwner")){
                    account = new HomeOwner();
                }else{
                    account = null;
                }
                if(account != null){
                    account.setUserName(cursor.getString(1));
                    account.setName(cursor.getString(2));
                    account.setLastName(cursor.getString(3));
                    account.setEmail(cursor.getString(4));
                    account.setPassword(cursor.getString(5));
                    accounts.add(account);
                }

            }while (cursor.moveToNext());
            cursor.close();
        } else{
            accounts = null;
        }
        db.close();
        return accounts;
    }

    //To delete an account in the database based on its user name
    public boolean deleteAccountByUserName(String accountUserName){
        boolean result = false;

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "Select * FROM " + TABLE_ACCOUNT + " WHERE " + COLUMN_USERNAME + " = \"" + accountUserName + "\"";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            String idStr = cursor.getString(1);
            db.delete(TABLE_ACCOUNT, COLUMN_USERNAME + " = " + idStr, null);
            cursor.close();
            result = true;
        }

        db.close();
        return result;
    }

    //SERVICES TABLE FUNCTIONS

    //To add a new service to the database
    public void addService(Service service){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SERVICE_NAME, service.getName());
        values.put(COLUMN_HOURLY_RATE, service.getHourlyRate());
        values.put(COLUMN_CATEGORY, service.getCategory().getLabel());

        db.insert(TABLE_SERVICE, null, values);
        db.close();
    }

    //To delete a service in the database based on its name
    public void deleteService(String serviceName){

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "Select * FROM " + TABLE_SERVICE + " WHERE " + COLUMN_SERVICE_NAME + " = \"" + serviceName + "\"";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            String idStr = cursor.getString(0);
            db.delete(TABLE_SP_SERVICES,COLUMN_SERVICE_ID + "=?", new String[]{idStr});
            db.delete(TABLE_SERVICE, COLUMN_SERVICE_ID + " = " + idStr, null);
            cursor.close();
        }

        db.close();
    }

    //To find all services in the database
    public ArrayList<Service> findAllServices(){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM "+ TABLE_SERVICE;
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Service> services = new ArrayList<Service>();
        if(cursor.moveToFirst()){
            do{
                services.add(new Service(Double.parseDouble(cursor.getString(2)), cursor.getString(1), cursor.getString(3)));
            }while (cursor.moveToNext());
            cursor.close();
        } else{
            services = null;
        }

        db.close();
        return services;
    }

    //To find all services in the database in a given category
    public ArrayList<Service> findServicesByCategory(Category category){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM "+ TABLE_SERVICE+" WHERE "+COLUMN_CATEGORY+" = \""+category.getLabel()+"\"";
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Service> services = new ArrayList<Service>();
        if(cursor.moveToFirst()){
            do{
                services.add(new Service(Double.parseDouble(cursor.getString(2)), cursor.getString(1), cursor.getString(3)));
            }while (cursor.moveToNext());
            cursor.close();
        } else{
            services = null;
        }

        db.close();
        return services;
    }

    //To edit the hourly rate of a service
    public void editServiceHourlyRate(String name, double hourlyRate){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_HOURLY_RATE, hourlyRate);

        db.update(TABLE_SERVICE, values, COLUMN_SERVICE_NAME + "= " + name, null);
        db.close();
    }

    //To find a specific service in the database based on its name
    public Service findService(String serviceName){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM "+ TABLE_SERVICE +" WHERE "+COLUMN_NAME+" = \""+serviceName+"\"";
        Cursor cursor = db.rawQuery(query, null);

        Service service;
        if(cursor.moveToFirst()){
            service = new Service(Double.parseDouble(cursor.getString(2)), cursor.getString(1), cursor.getString(3));
            cursor.close();
        } else{
            service = null;
        }

        db.close();
        return service;
    }

    //To find the service ID related to a specific service name
    private int findServiceId(String serviceName){
        int serviceID = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM "+ TABLE_SERVICE +" WHERE "+COLUMN_SERVICE_NAME+" = \""+serviceName+"\"";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
             serviceID = cursor.getInt(0);
        }else{
            serviceID = 0;
        }
        cursor.close();
        db.close();
        return serviceID;
    }

    //To find a service name from the service ID
    private String findServiceName(int id){
        String serviceName = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM "+ TABLE_SERVICE +" WHERE "+COLUMN_SERVICE_ID+" = \""+id+"\"";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            serviceName = cursor.getString(1);
        }
        cursor.close();
        db.close();
        return serviceName;
    }

    //SERVICE PROVIDER PROFILE FUNCTIONS

    //To add a column in the service provider profile table when the service provider completes his profile
    public void completeSpProfile(ServiceProvider sp){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, sp.getUserName());
        values.put(COLUMN_STREET_NUMBER, sp.getStreetNumber());
        values.put(COLUMN_APARTMENT_NUMBER, sp.getApartmentNumber());
        values.put(COLUMN_STREET_NAME, sp.getStreetName());
        values.put(COLUMN_CITY, sp.getCity());
        values.put(COLUMN_COUNTRY, sp.getCountry());
        values.put(COLUMN_COMPANY, sp.getCompany());
        values.put(COLUMN_DESCRIPTION, sp.getDescription());
        values.put(COLUMN_IS_LICENSED, Boolean.toString(sp.isLicensed()));
        values.put(COLUMN_PHONE_NUMBER, sp.getPhoneNumber());

        db.insert(TABLE_SP_PROFILE, null, values);
        db.close();
    }

    //To edit service provider profile
    public void editSpProfile(ServiceProvider sp){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_STREET_NUMBER, sp.getStreetNumber());
        values.put(COLUMN_APARTMENT_NUMBER, sp.getApartmentNumber());
        values.put(COLUMN_STREET_NAME, sp.getStreetName());
        values.put(COLUMN_CITY, sp.getCity());
        values.put(COLUMN_COUNTRY, sp.getCountry());
        values.put(COLUMN_COMPANY, sp.getCompany());
        values.put(COLUMN_DESCRIPTION, sp.getDescription());
        values.put(COLUMN_IS_LICENSED, Boolean.toString(sp.isLicensed()));
        values.put(COLUMN_PHONE_NUMBER, sp.getPhoneNumber());

        db.update(TABLE_SP_PROFILE, values, COLUMN_USERNAME + "= " + sp.getUserName(), null);
        db.close();
    }

    //To edit the street number
    public void editStreetNumber(String userName, int newStreetNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_SP_PROFILE + " SET " + COLUMN_STREET_NUMBER + " = ? WHERE " + COLUMN_USERNAME + " = ?";
        db.execSQL(query, new Object[]{newStreetNumber, userName});
        db.close();
    }

    //To edit the apartment number
    public void editApartmentNumber(String userName, String newApartmentNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_SP_PROFILE + " SET " + COLUMN_APARTMENT_NUMBER + " = ? WHERE " + COLUMN_USERNAME + " = ?";
        db.execSQL(query, new String[]{newApartmentNumber, userName});
        db.close();
    }

    //To edit the street name
    public void editStreetName(String userName, String newStreetName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_SP_PROFILE + " SET " + COLUMN_STREET_NAME + " = ? WHERE " + COLUMN_USERNAME + " = ?";
        db.execSQL(query, new String[]{newStreetName, userName});
        db.close();
    }

    //To edit the city
    public void editCity(String userName, String newCity){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_SP_PROFILE + " SET " + COLUMN_CITY + " = ? WHERE " + COLUMN_USERNAME + " = ?";
        db.execSQL(query, new String[]{newCity, userName});
        db.close();
    }

    //To edit the country
    public void editCountry(String userName, String newCountry){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_SP_PROFILE + " SET " + COLUMN_COUNTRY + " = ? WHERE " + COLUMN_USERNAME + " = ?";
        db.execSQL(query, new String[]{newCountry, userName});
        db.close();
    }

    //To edit the description
    public void editDescription(String userName, String newDescription){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_SP_PROFILE + " SET " + COLUMN_DESCRIPTION + " = ? WHERE " + COLUMN_USERNAME + " = ?";
        db.execSQL(query, new String[]{newDescription, userName});
        db.close();
    }

    //To edit the company
    public void editCompany(String userName, String newCompany){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_SP_PROFILE + " SET " + COLUMN_COMPANY + " = ? WHERE " + COLUMN_USERNAME + " = ?";
        db.execSQL(query, new String[]{newCompany, userName});
        db.close();
    }

    //To edit isLicensed
    public void editIsLicensed(String userName, boolean isLiscensed){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_SP_PROFILE + " SET " + COLUMN_IS_LICENSED + " = ? WHERE " + COLUMN_USERNAME + " = ?";
        db.execSQL(query, new String[]{Boolean.toString(isLiscensed), userName});
        db.close();
    }

    //To edit the phone number
    public void editPhoneNumber(String userName, String phoneNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_SP_PROFILE + " SET " + COLUMN_PHONE_NUMBER + " = ? WHERE " + COLUMN_USERNAME + " = ?";
        db.execSQL(query, new String[]{phoneNumber, userName});
        db.close();
    }

    //To find the profile information of a specific service provider using his user name
    public ServiceProvider findServiceProvider(String userName){
        ServiceProvider account = (ServiceProvider)findAccountByUserName(userName);
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM "+ TABLE_SP_PROFILE +" WHERE "+COLUMN_USERNAME+" = \""+userName+"\"";
        Cursor cursor = db.rawQuery(query, null);

        ServiceProvider sp = new ServiceProvider();
        if(cursor.moveToFirst()){
            sp.setName(account.getName());
            sp.setLastName(account.getLastName());
            sp.setUserName(account.getUserName());
            sp.setEmail(account.getEmail());
            sp.setPassword(account.getPassword());
            sp.setStreetNumber(cursor.getInt(1));
            sp.setApartmentNumber(cursor.getString(2));
            sp.setStreetName(cursor.getString(3));
            sp.setCity(cursor.getString(4));
            sp.setCountry(cursor.getString(5));
            sp.setCompany(cursor.getString(6));
            sp.setDescription(cursor.getString(7));
            sp.setLicensed(Boolean.parseBoolean(cursor.getString(8)));
            sp.setPhoneNumber(cursor.getString(9));
            sp.setAvailabilities(cursor.getString(10));
            cursor.close();
        } else{
            sp = null;
        }

        db.close();
        return sp;
    }

    public ArrayList<ServiceProvider> findAllServiceProviders(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * FROM "+ TABLE_SP_PROFILE;
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<ServiceProvider> serviceProviders = new ArrayList<ServiceProvider>();
        ServiceProvider sp;
        String userName;
        if(cursor.moveToFirst()){
            do{
                userName = cursor.getString(0);
                sp = (ServiceProvider)findAccountByUserName(userName);
                sp.setStreetNumber(cursor.getInt(1));
                sp.setApartmentNumber(cursor.getString(2));
                sp.setStreetName(cursor.getString(3));
                sp.setCity(cursor.getString(4));
                sp.setCountry(cursor.getString(5));
                sp.setCompany(cursor.getString(6));
                sp.setDescription(cursor.getString(7));
                sp.setLicensed(Boolean.parseBoolean(cursor.getString(8)));
                sp.setPhoneNumber(cursor.getString(9));
                sp.setAvailabilities(cursor.getString(10));
                serviceProviders.add(sp);
            }while (cursor.moveToNext());
            cursor.close();
        } else{
            serviceProviders = null;
        }

        db.close();
        return serviceProviders;
    }

    //To find if a specific service provider already has a profile
    public boolean spProfileExists(String userName){
        SQLiteDatabase db = this.getReadableDatabase();
        boolean flag;

        String query = "Select * FROM "+ TABLE_SP_PROFILE +" WHERE "+COLUMN_USERNAME+" = \""+userName+"\"";
        Cursor cursor = db.rawQuery(query, null);

        ServiceProvider sp = new ServiceProvider();
        if(cursor.moveToFirst()){
            flag = true;
            cursor.close();
        } else{
            flag = false;
        }

        db.close();
        return flag;
    }

    public void editAvailabilities(String userName, String availabilities){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_SP_PROFILE + " SET " + COLUMN_AVAILABILITIES + " = ? WHERE " + COLUMN_USERNAME + " = ?";
        db.execSQL(query, new String[]{availabilities, userName});
    }

    //SERVICE PROVIDER SERVICES FUNCTIONS

    //To add a service associated to a specific service provider
    public void addSpService(String userName, String serviceName){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, userName);
        values.put(COLUMN_SERVICE_ID, this.findServiceId(serviceName));

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_SP_SERVICES, null, values);
        db.close();
    }

    //To delete a service associated to a specific service provider
    public void deleteSpService(String userName, String serviceName){

        int serviceID = this.findServiceId(serviceName);

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "Select * FROM " + TABLE_SP_SERVICES + " WHERE " + COLUMN_USERNAME + " = \"" + userName + "\"" + " AND " + COLUMN_SERVICE_ID + " = \"" + serviceID + "\"";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            String idStr = cursor.getString(0);
            db.delete(TABLE_SP_SERVICES, COLUMN_USERNAME + "=? and " + COLUMN_SERVICE_ID + "=?", new String[]{idStr, serviceID+""});
            cursor.close();
        }

        db.close();
    }

    //To find the list of all the services associated to a service provider
    public ArrayList<String> findAllSpServices(String userName){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select " + COLUMN_SERVICE_ID + " FROM "+ TABLE_SP_SERVICES+" WHERE "+COLUMN_USERNAME+" = \""+userName+"\"";
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<String> services = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do{
                services.add(this.findServiceName(cursor.getInt(0)));
            }while (cursor.moveToNext());
            cursor.close();
        } else{
            services = null;
        }

        db.close();
        return services;
    }

    //NEW METHODS FOR DELIVERABLE 4

    //To find all service providers that offer a specific service
    public ArrayList<ServiceProvider> findSPbyService(String serviceName){
        int serviceID = this.findServiceId(serviceName);
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select " + COLUMN_USERNAME + " FROM " + TABLE_SP_SERVICES + " WHERE " + COLUMN_SERVICE_ID + " = \"" + serviceID + "\"";
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<ServiceProvider> serviceProviders = new ArrayList<ServiceProvider>();
        if(cursor.moveToFirst()){
            do{
                serviceProviders.add(this.findServiceProvider(cursor.getString(0)));
            }while (cursor.moveToNext());
            cursor.close();
        } else{
            serviceProviders = null;
        }
        db.close();
        return serviceProviders;
    }

    public double findSPAverageRating(String spUserName){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select " + COLUMN_AVERAGE_RATING + " FROM " + TABLE_SP_PROFILE + " WHERE " + COLUMN_USERNAME + " = \"" + spUserName + "\"";
        Cursor cursor = db.rawQuery(query, null);
        double averageRating;
        if(cursor.moveToFirst()){
            try {
                averageRating = Double.parseDouble(cursor.getString(0));
            }catch(Exception e){
                averageRating =5;
            }
            cursor.close();
        } else{
            averageRating = -1;
        }
        db.close();
        return averageRating;
    }
    
    //To find all service providers having a rating greater or equal to rating entered
    public ArrayList<ServiceProvider> findSPbyRating(int rating){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select " + COLUMN_USERNAME + " FROM " + TABLE_SP_PROFILE + " WHERE " + COLUMN_AVERAGE_RATING + " >= \"" + rating + "\"";
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<ServiceProvider> serviceProviders = new ArrayList<ServiceProvider>();
        if(cursor.moveToFirst()){
            do{
                serviceProviders.add(this.findServiceProvider(cursor.getString(0)));
            }while (cursor.moveToNext());
            cursor.close();
        } else{
            serviceProviders = null;
        }
        db.close();
        return serviceProviders;
    }

    public void addBooking(Booking booking){
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm");
        values.put(COLUMN_START_TIME, sdf.format(booking.getStartTime().getTime()));
        values.put(COLUMN_END_TIME, sdf.format(booking.getEndTime().getTime()));
        values.put(COLUMN_SERVICE_ID, this.findServiceId(booking.getService().getName()));
        values.put(COLUMN_USERNAME_HO, booking.getHomeOwner().getUserName());
        values.put(COLUMN_USERNAME_SP, booking.getServiceProvider().getUserName());
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(TABLE_BOOKINGS, null, values);
        booking.setBookingId(id);
        db.close();
    }

    public ArrayList<Booking> findAllBookingsbyHO(String homeOwnerUserName){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM "+ TABLE_BOOKINGS+" WHERE "+COLUMN_USERNAME_HO+" = \""+homeOwnerUserName+"\"";
        Cursor cursor = db.rawQuery(query, null);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm");
        ArrayList<Booking> bookings = new ArrayList<Booking>();
        int bookingId;
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        Service service;
        ServiceProvider sp;
        HomeOwner ho;
        Rating rating;
        Booking booking;
        if(cursor.moveToFirst()){
            do{
                bookingId = cursor.getInt(0);
                try {
                    startTime.setTime(sdf.parse(cursor.getString(1)));
                    endTime.setTime(sdf.parse(cursor.getString(2)));
                }
                catch(ParseException e){
                    startTime = null;
                }
                try{
                    endTime.setTime(sdf.parse(cursor.getString(2)));
                }
                catch(ParseException e){
                    endTime = null;
                }
                service = this.findService(this.findServiceName(cursor.getInt(3)));
                ho = (HomeOwner)this.findAccountByUserName(cursor.getString(4));
                sp = this.findServiceProvider(cursor.getString(5));
                booking = new Booking(service, sp, ho, startTime, endTime, bookingId);
                rating = findRatingbyBookingId(bookingId);
                booking.setRating(rating);
                bookings.add(booking);
            }while (cursor.moveToNext());
            cursor.close();
        } else{
            bookings = null;
        }

        db.close();
        return bookings;
    }

    public Booking findBookingbyID(long bookingID){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm");

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM "+ TABLE_BOOKINGS +" WHERE "+COLUMN_BOOKING_ID+" = \""+bookingID+"\"";
        Cursor cursor = db.rawQuery(query, null);

        Booking booking;
        if(cursor.moveToFirst()){
            Calendar startTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();
            try {
                startTime.setTime(sdf.parse(cursor.getString(1)));
                endTime.setTime(sdf.parse(cursor.getString(2)));
            }
            catch(ParseException e){
                startTime = null;
            }
            try{
                endTime.setTime(sdf.parse(cursor.getString(2)));
            }
            catch(ParseException e){
                endTime = null;
            }

            Service service = this.findService(this.findServiceName(cursor.getInt(3)));
            HomeOwner ho = (HomeOwner)this.findAccountByUserName(cursor.getString(4));
            ServiceProvider sp = this.findServiceProvider(cursor.getString(5));
            booking = new Booking(service, sp, ho, startTime, endTime, bookingID);
            cursor.close();
        } else{
            booking = null;
        }

        db.close();
        return booking;
    }

    public void addRating(Rating rating){
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATING, rating.getRating());
        values.put(COLUMN_COMMENT, rating.getComment());
        values.put(COLUMN_BOOKING_ID, rating.getBooking().getBookingId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_RATINGS, null, values);
        db.close();
    }

    public void updateServiceProviderRating(String spUserName, double averageRating){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_SP_PROFILE + " SET " + COLUMN_AVERAGE_RATING + " = ? WHERE " + COLUMN_USERNAME + " = ?";
        db.execSQL(query, new String[]{Double.toString(averageRating), spUserName});
        db.close();
    }

    public ArrayList<Integer> findRatingsforSp(String spUserName){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select "+COLUMN_BOOKING_ID+" FROM "+ TABLE_BOOKINGS +" WHERE "+COLUMN_USERNAME_SP+" = \""+spUserName+"\"";
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Integer> bookingIds = new ArrayList<Integer>();
        if(cursor.moveToFirst()){
            do{
                bookingIds.add(cursor.getInt(0));
            }while (cursor.moveToNext());
            cursor.close();
        } else{
            bookingIds = null;
        }
        db.close();

        ArrayList<Integer> ratings = new ArrayList<>();
        //Find all ratings
        for(int i = 0; i < bookingIds.size(); i++){
            int bookingId = bookingIds.get(i);
            ratings.add(this.findRatingbyBookingId(bookingId).getRating());
        }
        return ratings;
    }


    public Rating findRatingbyBookingId(int bookingId){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM "+ TABLE_RATINGS +" WHERE "+COLUMN_BOOKING_ID+" = \""+bookingId+"\"";
        Cursor cursor = db.rawQuery(query, null);

        Rating rating;
        if(cursor.moveToFirst()){
            rating = new Rating(cursor.getInt(1), bookingId, cursor.getString(2));
            cursor.close();
        } else{
            rating = null;
        }

        db.close();
        return rating;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ArrayList<ServiceProvider> findSPbyAvailability(String day, ArrayList<Integer> time) throws JSONException {
        ArrayList<ServiceProvider> allSPs = findAllServiceProviders();
        ArrayList<ServiceProvider> searchRes = new ArrayList<>();

        JSONObject availability;
        JSONArray dayAvailability;
        ArrayList<Integer> spTimes = new ArrayList<>();

        boolean matches;
        for (ServiceProvider sp : allSPs) {
            matches = false;
            availability = new JSONObject(sp.getAvailabilities());
            dayAvailability = availability.getJSONArray(day);

            for (int i = 0; i < dayAvailability.length(); i++) {
                spTimes.add(dayAvailability.optInt(i));
            }

            for (Integer t : spTimes) {
                if (time.contains(t)) {
                    matches = true;
                }
            }

            if (matches) {
                searchRes.add(sp);
            }

        }

        return searchRes;
    }




}
