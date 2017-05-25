package com.expansion.lg.kimaru.lggpscollector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by kimaru on 5/25/17.
 */

public class GpsDataTable extends SQLiteOpenHelper {
    public static final String TABLE_NAME="gps_data";
    public static final String JSON_ROOT="gps";
    public static final String DATABASE_NAME= "lg_gps";
    public static final int DATABASE_VERSION= 1;

    Context context;


    public static String varchar_field = " varchar(512) ";
    public static String real_field = " REAL ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String UUID= "id";
    public static final String SUPERVISOR_NAME= "supervisor_name";
    public static final String SUPERVISOR_EMAIL = "supervisor_email";
    public static final String SUPERVISOR_PHONE = "supervisor_phone";
    public static final String CHP_NAME = "chp_name";
    public static final String CHP_PHONE = "chp_phone";
    public static final String CHP_UUID = "chp_uuid";
    public static final String RECORD_UUID = "record_uuid";
    public static final String COUNTRY = "country";
    public static final String DATE_ADDED = "client_time";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lon";

    String [] columns=new String[]{UUID, SUPERVISOR_NAME, SUPERVISOR_EMAIL, SUPERVISOR_PHONE,
            CHP_NAME, CHP_PHONE, CHP_UUID, RECORD_UUID, COUNTRY, DATE_ADDED, LATITUDE, LONGITUDE};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + UUID + varchar_field + ", "
            + SUPERVISOR_NAME + varchar_field + ", "
            + SUPERVISOR_PHONE + varchar_field + ", "
            + SUPERVISOR_EMAIL + varchar_field + ", "
            + CHP_NAME + varchar_field + ", "
            + CHP_PHONE + varchar_field + ", "
            + CHP_UUID + varchar_field + ", "
            + RECORD_UUID + varchar_field + ", "
            + COUNTRY + text_field + ", "
            + DATE_ADDED + real_field + ", "
            + LATITUDE + real_field + ", "
            + LONGITUDE + real_field + "); ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public GpsDataTable(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_DATABASE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w("RegistrationTable", "upgrading database from" + oldVersion + "to" + newVersion);
        if (oldVersion < 2){
            upgradeVersion2(db);
        }
    }

    public long addData(GpsData gpsData) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(UUID, gpsData.getUuid());
        cv.put(SUPERVISOR_EMAIL, gpsData.getSupervisorEmail());
        cv.put(SUPERVISOR_NAME, gpsData.getSupervisorName());
        cv.put(SUPERVISOR_PHONE, gpsData.getSupervisoPhone());
        cv.put(CHP_NAME, gpsData.getChpName());
        cv.put(CHP_PHONE, gpsData.getChpPhone());
        cv.put(CHP_UUID, gpsData.getChpUuid());
        cv.put(RECORD_UUID, gpsData.getChpRecorduuid());
        cv.put(COUNTRY, gpsData.getCountry());
        cv.put(DATE_ADDED, gpsData.getDateAdded());
        cv.put(LATITUDE, gpsData.getLatitude());
        cv.put(LONGITUDE, gpsData.getLongitude());


        long id;
        if (isExist(gpsData)){
            id = db.update(TABLE_NAME, cv, UUID+"='"+gpsData.getUuid()+"'", null);
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
        return id;

    }


    public List<GpsData> getGpsData() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<GpsData> gpsDataList=new ArrayList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            GpsData gpsData=new GpsData();

            gpsData.setUuid(cursor.getString(0));
            gpsData.setSupervisorName(cursor.getString(1));
            gpsData.setSupervisorEmail(cursor.getString(2));
            gpsData.setSupervisoPhone(cursor.getString(3));
            gpsData.setChpName(cursor.getString(4));
            gpsData.setChpPhone(cursor.getString(5));
            gpsData.setChpUuid(cursor.getString(6));
            gpsData.setChpRecorduuid(cursor.getString(7));
            gpsData.setCountry(cursor.getString(8));
            gpsData.setDateAdded(cursor.getLong(9));
            gpsData.setLatitude(cursor.getDouble(10));
            gpsData.setLongitude(cursor.getDouble(11));

            gpsDataList.add(gpsData);
        }
        db.close();

        return gpsDataList;
    }


    public GpsData getGpsById(String id){
        SQLiteDatabase db = getReadableDatabase();

        String whereClause = UUID+" = ?";
        String[] whereArgs = new String[] {
                id,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            GpsData gpsData=new GpsData();
            gpsData.setUuid(cursor.getString(0));
            gpsData.setSupervisorName(cursor.getString(1));
            gpsData.setSupervisorEmail(cursor.getString(2));
            gpsData.setSupervisoPhone(cursor.getString(3));
            gpsData.setChpName(cursor.getString(4));
            gpsData.setChpPhone(cursor.getString(5));
            gpsData.setChpUuid(cursor.getString(6));
            gpsData.setChpRecorduuid(cursor.getString(7));
            gpsData.setCountry(cursor.getString(8));
            gpsData.setDateAdded(cursor.getLong(9));
            gpsData.setLatitude(cursor.getDouble(10));
            gpsData.setLatitude(cursor.getDouble(11));
            return gpsData;
        }

    }



    public boolean isExist(GpsData gpsData) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE "+UUID+" = '" + gpsData.getUuid() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;

    }

    private void upgradeVersion2(SQLiteDatabase db) {

    }
}
