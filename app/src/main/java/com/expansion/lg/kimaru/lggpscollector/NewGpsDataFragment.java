package com.expansion.lg.kimaru.lggpscollector;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by kimaru on 5/25/17.
 */


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewGpsDataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewGpsDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewGpsDataFragment extends Fragment implements View.OnClickListener, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView editGpsLocation;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText mChpPhone, mChpRecordUuid;
    Spinner mCountry, mActivityType, mTimeToResolve, mGpsOn;

    Button buttonSave, buttonList;

    //location
    double latitude, longitude;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    // The minimum distance to change Updates in meters
    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    // The minimum time between updates in milliseconds
    public static final long MIN_TIME_BETWEEN_UPDATE = 1000 * 60 / 2; // 30 seconds
    protected LocationManager locationManager;

    //perms
    private static final int PERMISSION_CALLBACK_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    GpsData editingGps = null;


    SessionManagement sessionManagement;
    HashMap<String, String> user;
    List<String> countries = new ArrayList<>();
    List<String> activityType = new ArrayList<>();
    List<String> timess = new ArrayList<>();
    List<String> yesno = new ArrayList<>();


    public NewGpsDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewGpsDataFragment newInstance(String param1, String param2) {
        NewGpsDataFragment fragment = new NewGpsDataFragment();
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
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.new_gps_fragment, container, false);
        MainActivity.backFragment = new HomeFragment();
        countries = Arrays.asList(getResources().getStringArray(R.array.selectCountries));
        activityType = Arrays.asList(getResources().getStringArray(R.array.activityType));
        timess = Arrays.asList(getResources().getStringArray(R.array.times));
        yesno = Arrays.asList(getResources().getStringArray(R.array.yesno));

        //Initialize the UI Components
        //EditText mChpPhone, mChpRecordUuid;
        // Spinner mCountry, mActivityType, mTimeToResolve;
        editGpsLocation = (TextView) v.findViewById(R.id.editGpsLocation);
        mChpPhone = (EditText) v.findViewById(R.id.editChpPhone);
        mChpRecordUuid = (EditText) v.findViewById(R.id.editChpRecordUuid);
        mCountry = (Spinner) v.findViewById(R.id.selectCountries);
        mActivityType = (Spinner) v.findViewById(R.id.selectActivityType);
        mTimeToResolve = (Spinner) v.findViewById(R.id.selectTimeToResolve);
        mGpsOn = (Spinner) v.findViewById(R.id.selectGpsOn);


        sessionManagement = new SessionManagement(getContext());
        user = sessionManagement.getUserDetails();
        //to reduce repetition
        mChpPhone.setText(sessionManagement.getChpDetails().get(SessionManagement.KEY_CHP_PHONE));


        setUpEditingMode();
        buttonList = (Button) v.findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this);

        buttonSave = (Button) v.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);
        checkPermissions();

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onClick(View view){
        Fragment fragment;
        FragmentTransaction fragmentTransaction;
        switch (view.getId()){
            case R.id.buttonList:
                fragment = new HomeFragment();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "new_gps");
                fragmentTransaction.commitAllowingStateLoss();
                break;

            case R.id.buttonSave:
                // set date as integers

                String chpPhone = mChpPhone.getText().toString();
                String referenceId = mChpRecordUuid.getText().toString();
                String country = countries.get(mCountry.getSelectedItemPosition());
                String activity = activityType.get(mActivityType.getSelectedItemPosition());
                String timeResolve = timess.get(mTimeToResolve.getSelectedItemPosition());
                boolean gpsOn = yesno.get(mGpsOn.getSelectedItemPosition()).equalsIgnoreCase("Yes");

                Long currentDate =  new Date().getTime();
                String uuid;
                if (editingGps == null){
                    uuid = UUID.randomUUID().toString();
                }else{
                    uuid = editingGps.getUuid();
                }


                // Do some validations

                if (chpPhone.trim().equals("")){
                    Toast.makeText(getContext(), "Enter the CHP Phone", Toast.LENGTH_SHORT).show();
                    mChpPhone.requestFocus();
                    return;
                }

                if (referenceId.trim().equals("")){
                    Toast.makeText(getContext(), "Enter the CHP Record UUID", Toast.LENGTH_SHORT).show();
                    mChpRecordUuid.requestFocus();
                    return;
                }
                if (!referenceId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
                    Toast.makeText(getContext(), "Enter a valid UUID for CHP Record", Toast.LENGTH_SHORT).show();
                    mChpRecordUuid.requestFocus();
                    return;
                }

                // Save the record
                GpsData gpsData = new GpsData(uuid, chpPhone, referenceId, country,
                        currentDate, gpsOn, activity,
                        latitude, longitude, timeResolve);


                GpsDataTable gpsDataTable = new GpsDataTable(getContext());
                long id = gpsDataTable.addData(gpsData);

                if (id ==-1){
                    Toast.makeText(getContext(), "Could not save the results", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
                    fragment = new HomeFragment();
                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, "");
                    fragmentTransaction.commitAllowingStateLoss();
                }
                break;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if(locationManager != null){
            try{
                locationManager.removeUpdates(this);
            }catch (SecurityException se){}

        }
    }

    public void getLocation(){
        //get the location
        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            //is GPS Enabled or not?
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // Is network enabled
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            //if both are off, we cannot get the GPS
            if (!isGPSEnabled && !isNetworkEnabled){
                //ask user to enable location
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                // Setting Dialog Title
                alertDialog.setTitle("GPS settings");

                // Setting Dialog Message
                alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

                // On pressing Settings button
                alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getContext().startActivity(intent);
                    }
                });

                // on pressing cancel button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

            }else{
                this.canGetLocation = true;
                // check for permissions
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    //show explanation for permissions
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Needs GPS Permission");
                    builder.setMessage("In order to work properly, this app needs GPS permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(getActivity(), new String[]{
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            }, PERMISSION_CALLBACK_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else{
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, PERMISSION_CALLBACK_CONSTANT);
                }

                //we have the permissions now
                // Get location from Network provider
                if (isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BETWEEN_UPDATE,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // we try getting the location form GPS
                if (isGPSEnabled){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BETWEEN_UPDATE,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

            }

        } catch (Exception e){}
    }
    public void checkPermissions(){
        try{
            if(ActivityCompat.checkSelfPermission(getActivity(), permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(), permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),permissionsRequired[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),permissionsRequired[1])){
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Multiple Permissions Request");
                    builder.setMessage("This app needs Location permissions");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(getActivity(),permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }else if (permissionStatus.getBoolean(permissionsRequired[0], false)){
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Multiple Permissions Request");
                    builder.setMessage("This app needs Location permissions");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            Toast.makeText(getContext(), "Go to Permissions to Grant Location Permissions",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }else {
                    ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                }
                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(permissionsRequired[0],true);
                editor.commit();
            }else{
                proceedAfterPermission();
            }
        }catch (Exception e){}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                proceedAfterPermission();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),permissionsRequired[1])){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(getActivity(),permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(getContext(), permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(getContext(), permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
    private void proceedAfterPermission() {
        getLocation();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void setUpEditingMode(){

        if (editingGps != null){

            mChpPhone.setText(editingGps.getChpPhone());
            mChpRecordUuid.setText(editingGps.getReferenceId());
            //mCountry
        }
    }
    //Location Methods
    @Override
    public void onLocationChanged(Location location){
        if (location != null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
//            Snackbar.make(getView(), "Lat, Lon "+String.valueOf(latitude) +", "+String.valueOf(longitude), Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
            editGpsLocation.setText("Current Position:\n Lat: "+String.valueOf(latitude) +", Lon "
                    +String.valueOf(longitude));
        }
    }

    @Override
    public void onProviderEnabled(String provider){

    }

    @Override
    public void onProviderDisabled(String provider){

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
