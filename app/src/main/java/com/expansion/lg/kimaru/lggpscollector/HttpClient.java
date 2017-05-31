package com.expansion.lg.kimaru.lggpscollector;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.body.JSONObjectBody;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kimaru on 5/31/17.
 */


public class HttpClient {
    Context context;
    private static String url ="https://expansion.lg-apps.com/api/v1/sync/gpsdata";

    public HttpClient(Context context){
        this.context = context;
    }


    // POST MY RECORDS
    public String postGPSData() throws Exception {
        String postResults = "";
        GpsDataTable gpsDataTable = new GpsDataTable(context);

        try {
            postResults = this.ApiClient(gpsDataTable.getGpsDataAsJson(),
                    GpsDataTable.JSON_ROOT, url);
        } catch (Exception e){
            Log.d("ERROR : Sync Recs", e.getMessage());
            postResults = null;
        }


//        List<GpsData> gpsDataList = gpsDataTable.getGpsData();
//        for (GpsData gps : gpsDataList){
//            JSONObject gpsJson = new JSONObject();
//            gpsJson.put("tag", "gps_visit");
//            gpsJson.put(GpsDataTable.UUID, gps.getUuid());
//            gpsJson.put(GpsDataTable.CHP_PHONE, gps.getChpPhone());
//            gpsJson.put(GpsDataTable.REFERENCE_ID, gps.getReferenceId());
//            gpsJson.put(GpsDataTable.COUNTRY, gps.getCountry());
//            gpsJson.put(GpsDataTable.DATE_ADDED, gps.getDateAdded());
//            gpsJson.put(GpsDataTable.LATITUDE, gps.getLatitude());
//            gpsJson.put(GpsDataTable.LONGITUDE, gps.getLongitude());
//            gpsJson.put(GpsDataTable.GPS_ON, gps.isGpsOn());
//            gpsJson.put(GpsDataTable.ACTIVITY_TYPE, gps.getActivityType());
//            gpsJson.put(GpsDataTable.TIME_TO_RESOLVE, gps.getApproximateTimeToResolve());
//            try {
//                postResults = this.ApiClient(gpsJson, "", url);
//                Log.d("Testing", postResults);
//                Log.d("RESULTS : Sync", "");
//                Log.d("API  : Url", url);
//            }catch (Exception e){
//                Log.d("ERROR : Sync", e.getMessage());
//                postResults = null;
//            }
//        }
        return postResults;
    }


    private String ApiClient(JSONObject json, String JsonRoot, String apiEndpoint) throws Exception {
        //  get the server URL
        AsyncHttpPost p = new AsyncHttpPost(url);
        p.setBody(new JSONObjectBody(json));
        JSONObject ret = AsyncHttpClient.getDefaultInstance().executeJSONObject(p, null).get();
        return ret.toString();

    }


    // GET RECORDS FROM THE SERVER
    public void startClient(){
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //registrations
                        new ProcessGpsData().execute(url);
                    }
                });
            }
        };
        timer.schedule(task, 0, 60 * 1000);
        // timer.schedule(task, 0, 60*1000);
    }

    private class ProcessGpsData extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            try{
                postGPSData();
            }catch (Exception e){}

            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
            if(stream !=null){
                Toast.makeText(context, "Posted the record", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
