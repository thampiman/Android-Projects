package com.outnin.logger.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;

public class MainActivity extends ActionBarActivity implements LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private LocationManager mLocationManager;
    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Create the LocationRequest object
        mLocationClient = new LocationClient(this, this, this);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.enable_gps_alert_title))
                .setMessage(getResources().getString(R.string.enable_gps_alert))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Redirect user to enable GPS in Android Settings
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
        }

        // add PhoneStateListener
        MyPhoneStateListener myPhoneStateListener = new MyPhoneStateListener();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(myPhoneStateListener,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        populateView();
    }

    private void populateView() {
        // Location Spinner
        Spinner locationSpinner = (Spinner) findViewById(R.id.location);
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this,
                R.array.location_array, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        // GPS Provider
        TextView gpsProvider = (TextView) findViewById(R.id.gpsProvider);
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsProvider.setText(getResources().getString(R.string.gps_provider_enabled));
        } else {
            gpsProvider.setText(getResources().getString(R.string.gps_provider_disabled));
        }

        // Update GPS Coordinates and Accuracy with Last Known Location
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            updateViewWithLocation(location);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        updateViewWithLocation(location);
    }

    private void updateViewWithLocation(Location location) {
        float accuracy = location.getAccuracy();
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        String coordinates = Double.toString(lat) + "," + Double.toString(lon);
        TextView gpsCoordinates = (TextView) findViewById(R.id.gpsCoordinates);
        gpsCoordinates.setText(coordinates);

        TextView gpsAccuracy = (TextView) findViewById(R.id.gpsAccuracy);
        gpsAccuracy.setText(Float.toString(accuracy));
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength){
            super.onSignalStrengthsChanged(signalStrength);
            int gsmSignalStrengthIntValue = signalStrength.getGsmSignalStrength();
            int gsmSignalStrengthDbmValue = 2*gsmSignalStrengthIntValue - 113; // Based on TS 27.007, section 8.5
            String gsmSignalStrength = Integer.toString(gsmSignalStrengthDbmValue);
            TextView cellSignalStrength = (TextView) findViewById(R.id.cellSignalStrength);
            cellSignalStrength.setText(gsmSignalStrength + " dBm");
        }

        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            super.onServiceStateChanged(serviceState);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }

    @Override
    protected void onResume() {
        if (!mLocationClient.isConnected()) {
            mLocationClient.connect();
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        // If the client is connected
        if (mLocationClient.isConnected()) {
            // remove location updates
            mLocationClient.removeLocationUpdates(this);
        }
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        TextView gpsCoordinates = (TextView) findViewById(R.id.gpsCoordinates);
        gpsCoordinates.setText(getResources().getString(R.string.unknown));

        TextView gpsAccuracy = (TextView) findViewById(R.id.gpsAccuracy);
        gpsAccuracy.setText(getResources().getString(R.string.unknown));
    }
}
