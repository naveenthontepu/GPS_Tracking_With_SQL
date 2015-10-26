package com.example.naveen.gps_tracking_with_sql;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;


public class Gps_Tracking_Service extends Service implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,LocationListener{
    public Gps_Tracking_Service() {
    }

    private final String TAG="com.example.naveen.gps_tracking_with_sql";
    protected final long LOCATION_UPDATE_INTERVAL = 10000;
    protected final long FASTEST_LOCATION_UPDATE_INTERVAL = LOCATION_UPDATE_INTERVAL/2;
    protected final int LOCATION_REQUEST_MINIMUM_DISTANCE=10;

    protected Location mCurrentLocation;
    protected LocationRequest mLocationRequest;
    GPS_Tracking gpsobject = new GPS_Tracking();
    MyDBHandler dbHandler;

    GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHandler = new MyDBHandler(this,null,null,1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG,"onStartCommand");

        Runnable r = new Runnable() {
            @Override
            public void run() {

                buildGoogleApiClient();
                mGoogleApiClient.connect();
            }
        };
        Thread GoogleClientThread = new Thread(r);
        GoogleClientThread.start();
        return Service.START_STICKY;
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
        Log.i(TAG,"buildGoogleApiClient();");

    }

    private void createLocationRequest(){
        Log.i(TAG,"createLocationRequest");
        mLocationRequest = new LocationRequest();
        //mLocationRequest.setSmallestDisplacement(LOCATION_REQUEST_MINIMUM_DISTANCE);
        mLocationRequest.setInterval(LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onConnected(Bundle bundle) {

        mCurrentLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        startLocationUpdate();

    }

    public void startLocationUpdate(){
        Log.i(TAG,"startLocationUpdates");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
        Log.i(TAG,"startLocationUpdates completed");

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onLocationChanged(Location location) {

        mCurrentLocation=location;
        Log.i(TAG, "onLocationChanged = " + String.valueOf(mCurrentLocation));

        String mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        Location1 location1 = new Location1(mCurrentLocation.getLatitude()
                ,mCurrentLocation.getLongitude()
                ,mLastUpdateTime);
        dbHandler.addLocation(location1);


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);

    }
}
