package com.expansion.lg.kimaru.lggpscollector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kimaru on 5/25/17.
 */

public class GpsDataViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    GpsData gpsData = null;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView editUuid, editLocationData;
    TextView chpName, chpPhone, supervisorName, supervisorEmail, supervisorPhone; // phone


    AppCompatActivity a = new AppCompatActivity();

    SessionManagement session;
    HashMap<String, String> user;


    public static GpsDataViewFragment newInstance(String param1, String param2) {
        GpsDataViewFragment fragment = new GpsDataViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.gps_view, container, false);

        session = new SessionManagement(getContext());
        user = session.getUserDetails();
        MainActivity.backFragment = new HomeFragment();

        editUuid = (TextView) v.findViewById(R.id.editUuid);
        editLocationData = (TextView) v.findViewById(R.id.editLocationData);
        chpName = (TextView) v.findViewById(R.id.chpName);
        chpPhone = (TextView) v.findViewById(R.id.chpPhone);
        supervisorName = (TextView) v.findViewById(R.id.supervisorName);
        supervisorEmail = (TextView) v.findViewById(R.id.supervisorEmail);
        supervisorPhone = (TextView) v.findViewById(R.id.supervisorPhone);
        gpsData = session.getSavedGpsData();

        editUuid.setText(gpsData.getReferenceId());
        editLocationData.setText("Lat: " +String.valueOf(gpsData.getLatitude()) + ",  Lon: " + String.valueOf(gpsData.getLongitude()));
        chpName.setText(gpsData.getChpPhone());
        chpPhone.setText(gpsData.getChpPhone());
        supervisorName.setText(gpsData.getReferenceId());
        supervisorEmail.setText(gpsData.isGpsOn() ? "GPS ON ": " GPS OFF");
        supervisorPhone.setText(gpsData.getActivityType());
        return v;
    }
}
