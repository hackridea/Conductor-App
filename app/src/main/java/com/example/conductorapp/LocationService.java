package com.example.conductorapp;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.conductorapp.utils.StringCreator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LocationService extends Service {
    LocationManager manager;
    private String rid;


    public LocationService() {

    }

    @Override
    public void onCreate() {
        if (manager == null) {
            manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        manager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 5000, 10f, new Locationlistener());
        Location loc=manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if(loc!=null)
        System.out.println(loc.getLatitude()+loc.getLongitude());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        rid=JourneyActivity.rid;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private class Locationlistener implements LocationListener{
        Location currentLocation;
        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(LocationService.this, location.getLatitude()+location.getLongitude()+"", Toast.LENGTH_SHORT).show();
            System.out.println(location.getLatitude()+location.getLongitude());
            OkHttpClient client = new OkHttpClient();
            String content;
            StringCreator helper = new StringCreator();
            helper.addParam("id",getSharedPreferences("USER",Context.MODE_PRIVATE).getString("NUM",""));
            helper.addParam("routeid",rid);
            helper.addParam("lat",location.getLatitude()+"");
            helper.addParam("lng",location.getLongitude()+"");
            helper.addParam("crowd_status",JourneyActivity.seekValue+"");
            content = helper.getPOST_PARAMS();
            Request request = new Request.Builder().url("http://192.168.137.1:3001/bus/currentdata").post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), content)).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){

                    }
                }
            });
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
