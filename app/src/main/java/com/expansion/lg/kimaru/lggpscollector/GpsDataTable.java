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
    public static final String CHP_PHONE = "chp_phone";
    public static final String REFERENCE_ID = "record_uuid";
    public static final String COUNTRY = "country";
    public static final String DATE_ADDED = "client_time";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lon";
    public static final String GPS_ON = "gps_on";
    public static final String ACTIVITY_TYPE = "activity_type";
    public static final String TIME_TO_RESOLVE = "time_to_resolve";

    String [] columns=new String[]{UUID, CHP_PHONE, REFERENCE_ID, COUNTRY, DATE_ADDED,
            LATITUDE, LONGITUDE, GPS_ON, ACTIVITY_TYPE, TIME_TO_RESOLVE};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + UUID + varchar_field + ", "
            + CHP_PHONE + varchar_field + ", "
            + REFERENCE_ID + varchar_field + ", "
            + COUNTRY + varchar_field + ", "
            + DATE_ADDED + real_field + ", "
            + LATITUDE + real_field + ", "
            + LONGITUDE + real_field + ", "
            + GPS_ON + integer_field + ", "
            + ACTIVITY_TYPE + varchar_field + ", "
            + TIME_TO_RESOLVE + varchar_field + "); ";

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
        cv.put(CHP_PHONE, gpsData.getChpPhone());
        cv.put(REFERENCE_ID, gpsData.getReferenceId());
        cv.put(COUNTRY, gpsData.getCountry());
        cv.put(DATE_ADDED, gpsData.getDateAdded());
        cv.put(LATITUDE, gpsData.getLatitude());
        cv.put(LONGITUDE, gpsData.getLongitude());
        cv.put(GPS_ON, gpsData.isGpsOn());
        cv.put(ACTIVITY_TYPE, gpsData.getActivityType());
        cv.put(TIME_TO_RESOLVE, gpsData.getApproximateTimeToResolve());


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
            gpsData.setChpPhone(cursor.getString(1));
            gpsData.setReferenceId(cursor.getString(2));
            gpsData.setCountry(cursor.getString(3));
            gpsData.setDateAdded(cursor.getLong(4));
            gpsData.setLatitude(cursor.getDouble(5));
            gpsData.setLongitude(cursor.getDouble(6));
            gpsData.setGpsOn(cursor.getInt(7) == 1);
            gpsData.setActivityType(cursor.getString(8));
            gpsData.setApproximateTimeToResolve(cursor.getString(9));

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
            gpsData.setChpPhone(cursor.getString(1));
            gpsData.setReferenceId(cursor.getString(2));
            gpsData.setCountry(cursor.getString(3));
            gpsData.setDateAdded(cursor.getLong(4));
            gpsData.setLatitude(cursor.getDouble(5));
            gpsData.setLongitude(cursor.getDouble(6));
            gpsData.setGpsOn(cursor.getInt(7) == 1);
            gpsData.setActivityType(cursor.getString(8));
            gpsData.setApproximateTimeToResolve(cursor.getString(9));
            return gpsData;
        }

    }

    public JSONObject getGpsDataAsJson() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns, null, null,null,null,null,null);

        JSONObject results = new JSONObject();

        JSONArray resultSet = new JSONArray();

        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            int totalColumns = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i =0; i < totalColumns; i++){
                if (cursor.getColumnName(i) != null){
                    try {
                        if (cursor.getString(i) != null){
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        }else{
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    }catch (Exception e){
                    }
                }
            }
            resultSet.put(rowObject);
            try {
                results.put(JSON_ROOT, resultSet);
            } catch (JSONException e) {

            }
        }
        cursor.close();
        db.close();
        return results;
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
