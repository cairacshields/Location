package com.example.paul.location;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {

    /*
    **In this practice application, I will show how to get a users location
    **And show it in the app!
    **Be sure that you've added the correct permissions in the manifest.xml file
    */

    private Button locate;
    private TextView currentLoc, currentLocation;

    //The locationManager enables us to use location tools
    LocationManager locationManager;

    //The locationListener listens for when a users location changes
    LocationListener locationListener;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locate = (Button) findViewById(R.id.locate);
        currentLoc = (TextView) findViewById(R.id.currentLoc);
        currentLocation = (TextView)findViewById(R.id.currentLocation);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                String loc = String.valueOf(location.getLatitude());
                String location2 = String.valueOf(location.getLongitude());
                currentLoc.setText(loc);
                currentLocation.setText(location2);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        //Here we will setup our location manager completely, so that
        // it will listen for location changes every 5 seconds or 5000 ms

        //Note that a permission check is required for certain versions of android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                return;
            }
        }else{
            configureButton();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode){
            case 10:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    configureButton();

                }
        }
    }

    private void configureButton(){

        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            }
        });
    }
}
