package com.expansion.lg.kimaru.lggpscollector;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by kimaru on 5/25/17.
 */

public class SessionManagement {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "expansion";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USER_NAME = "supervisor_name";
    public static final String KEY_USER_EMAIL = "supervisor_email";
    public static final String KEY_USER_ID = "supervisor_id";
    public static final String KEY_USER_PHONE = "supervisor__phone";
    public static final String KEY_USER_COUNTRY = "supervisor_country";
    public static final String KEY_CHP_NAME = "chp_name";
    public static final String KEY_CHP_ID = "chp_id";
    public static final String KEY_CHP_PHONE = "chp_phone";
    public static final String KEY_CHP_COUNTRY = "chp_country";
    public static final String KEY_GPSDATA = "gps";



    public static final String IS_INITIAL_RUN = "initialRun";


    //constructor
    public SessionManagement (Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public void createSupervisorDetails (String name, String email, String phone, String country){
        //storing login values as TRUE
        editor.putBoolean(IS_LOGIN, true);

        //store name in pref
        editor.putString(KEY_USER_NAME, name);

        //put email in pref
        editor.putString(KEY_USER_EMAIL, email);

        //put userid
        editor.putString(KEY_USER_PHONE, phone);

        //put user country
        editor.putString(KEY_USER_COUNTRY, country);

        //commit / Save the values

        editor.commit();
    }


    /**
     * Get the stored session Data
     *
     *
     * */
    public HashMap<String, String> getUserDetails (){
        HashMap<String, String> user = new HashMap<String, String>();
        //UserName
        user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, null));
        user.put(KEY_USER_EMAIL, pref.getString(KEY_USER_EMAIL, null));
        user.put(KEY_USER_PHONE, pref.getString(KEY_USER_PHONE, null));
        user.put(KEY_USER_COUNTRY, String.valueOf(pref.getString(KEY_USER_COUNTRY, "UG")));

        //return user
        return user;
    }

    public void createChpDetails (String uuid, String name, String phone, String country){
        //storing login values as TRUE

        //store name in pref
        editor.putString(KEY_CHP_NAME, name);

        //put email in pref
        editor.putString(KEY_CHP_ID, uuid);

        //put userid
        editor.putString(KEY_CHP_PHONE, phone);

        //put user country
        editor.putString(KEY_CHP_COUNTRY, country);

        //commit / Save the values

        editor.commit();
    }

    public GpsData getSavedGpsData (){

        GpsData gpsData;
        Gson gson = new Gson();
        String gpsDetails = pref.getString(KEY_GPSDATA, "");
        gpsData = gson.fromJson(gpsDetails, GpsData.class);
        return gpsData;
    }

    public void saveGpsData(GpsData gps){
        Gson gson = new Gson();
        String gpsObject = gson.toJson(gps);
        editor.putString(KEY_GPSDATA, gpsObject);
        editor.commit();
    }

    /**
     * Get the stored session Data
     *
     *
     * */
    public HashMap<String, String> getChpDetails (){
        HashMap<String, String> chp = new HashMap<String, String>();
        //UserName
        chp.put(KEY_CHP_NAME, pref.getString(KEY_CHP_NAME, null));
        //email
        chp.put(KEY_CHP_PHONE, pref.getString(KEY_CHP_PHONE, null));
        //userId
        chp.put(KEY_CHP_ID, String.valueOf(pref.getString(KEY_CHP_ID, null)));

        chp.put(KEY_CHP_COUNTRY, String.valueOf(pref.getString(KEY_CHP_COUNTRY, "ug")));

        //return chp
        return chp;
    }


    /**
     * When user logs out, clear the sessionData
     */
    public void logoutUser(){
        //cleat all data from SharedPreferences
        editor.clear();
        editor.commit();
    }

    /**
     * Check for Login
     * It is reference in checkLogin()
     * THis is a boolean flag
     */
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }


    public boolean isInitialRun() {return pref.getBoolean(IS_INITIAL_RUN, true);}
    public void setInitialRun(boolean status) {
        editor.putBoolean(IS_INITIAL_RUN, status);
        editor.commit();
    }
}
