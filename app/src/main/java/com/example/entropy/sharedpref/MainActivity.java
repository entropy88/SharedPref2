package com.example.entropy.sharedpref;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String USER_PREF = "USER_PREF";
    public static final String USER_DATA = "USER_DATA";
    public static final String TAG = "MainActivity";


    TextView previouslyStored;
    SharedPreferences sharedPreferences;

    LocationListener locationListener;
    LocationManager locationManager;
    Button fetchCoordinates;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        previouslyStored = (TextView) findViewById(R.id.tv_previously_stored);
        sharedPreferences = getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        previouslyStored.setText(sharedPreferences.getString(USER_DATA, ""));

        fetchCoordinates = (Button) findViewById(R.id.btn_get_coordinates);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(USER_DATA, sharedPreferences.getString(USER_DATA,"")+"\n"+ location.getLongitude() + "" + location.getLatitude());
                editor.commit();
                previouslyStored.setText(sharedPreferences.getString(USER_DATA,""));

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fetchCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates("gps", 10000, 0, locationListener);
            }
        });

            }


    public void clearData (View view){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(USER_DATA, "");
        editor.commit();
        previouslyStored.setText(sharedPreferences.getString(USER_DATA,""));
        Log.v(TAG, "data is pushed");
        Toast.makeText(this, "data cleared", Toast.LENGTH_LONG).show();

    }



}
