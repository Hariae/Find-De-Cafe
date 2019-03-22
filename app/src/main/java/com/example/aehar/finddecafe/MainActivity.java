package com.example.aehar.finddecafe;
import android.Manifest.permission;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;



import java.io.IOException;
import java.util.List;
import android.content.Intent;

/*
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.Snackbar;
*/

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    int PROXIMITY_RADIUS=10000;
    double latitude,longitude;
    private View contextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
         .findFragmentById(R.id.g_map);
        mapFragment.getMapAsync(this);
        Log.d("onCre", "Oncreate");





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("per", "Oncreate1");
        if(requestCode ==1){
            if(grantResults.length >0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                if((ContextCompat.checkSelfPermission(this,permission.ACCESS_FINE_LOCATION))== PackageManager.PERMISSION_GRANTED)
                    Log.d("per", "Oncreate");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Log.d("outside", "outside1");
        //mMap.setMinZoomPreference(5);
        //mMap.setMaxZoomPreference(10);
        //seattle coordinates


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Inside", "Inside");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d("lat", latitude+"");
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            //buildAlertMessageNoGps();
            Log.v("gps", "disabled");
        /*    contextView = findViewById(R.id.g_map);
           Snackbar snackbar = Snackbar
                    .make(contextView, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);

            snackbar.show();*/
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("GPS is not switched on!");
            builder1.setCancelable(true);
            builder1.setNegativeButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }


        if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{permission.ACCESS_FINE_LOCATION},1);
            Log.d("fail", "fail");
        } else{
            Log.d("pass", "pass");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
        }


        //seattle = new LatLng(47.6062095, -122.3320708);
           /* seattle = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(seattle).title("Seattle"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(seattle));*/
        //}



    }

    public void goTo(View v){
        //Log.d(LOG_TAG, "Button Clicked!");
        Intent intent = new Intent(this, SecondActivity.class);
        //String message = mMessageEditText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("latitude", latitude+"");
        intent.putExtra("longitude", longitude+"");
        startActivity(intent);

    }

   public void onClick(View v){
        Object dataTransfer[]=new Object[2];
        GetNearByPlacesData getNearByPlacesData= new GetNearByPlacesData();
        if(v.getId()== R.id.B_search)
        {
            EditText tf_location = (EditText)findViewById(R.id.TF_location);
            String location =tf_location.getText().toString();
            Log.v("loc", "location");
            //String location ="Chromatic Cafe";
            List<Address> addressList = null;
            MarkerOptions mo = new MarkerOptions();

            if(!location.equals("")){
                Geocoder geocoder= new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(location,5);
                    if(addressList != null)
                    {
                        for(int i=0;i<addressList.size();i++)
                        {
                            Address myAddress= addressList.get(i);
                            latitude = myAddress.getLatitude();
                            longitude = myAddress.getLongitude();
                            LatLng latLng = new LatLng(myAddress.getLatitude(),myAddress.getLongitude());
                            mo.position(latLng);
                            mo.title(location);
                            mMap.addMarker(mo);
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                        }
                        String Cafe="restaurant";
                       // latitude = myAddress.getLatitude();
                        //longitude = myAddress.getLongitude();
                        String url= getUrl(latitude,longitude,Cafe);
                        dataTransfer[0]=mMap;
                        dataTransfer[1]=url;
                        getNearByPlacesData.execute(dataTransfer);
                        Toast.makeText(MainActivity.this, "Showing Nearby cafes", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        else if(v.getId() == R.id.B_Cafe)
        {
            mMap.clear();
            String Cafe="restaurant";
            String url= getUrl(latitude,longitude,Cafe);
            dataTransfer[0]=mMap;
            dataTransfer[1]=url;
            getNearByPlacesData.execute(dataTransfer);
            Toast.makeText(MainActivity.this, "Showing Nearby cafes", Toast.LENGTH_SHORT).show();

        }
    }
    private String getUrl(double latitude, double longitude, String nearbyPlace)
    {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyCE2dQSvyWLtDQQYkOfBCY2cPRhUQQbxRI");
        Log.v("MapsActivity", "url = "+googlePlaceUrl.toString());
        return googlePlaceUrl.toString();

    }
}
