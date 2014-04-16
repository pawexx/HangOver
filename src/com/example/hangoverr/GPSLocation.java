package com.example.hangoverr;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class GPSLocation {
	private Intent intent;
	private TextView textGps;
	private LocationManager locationManager;
	private Context context;
	private Location location;
	
	public GPSLocation(TextView textGPS, Context context){
		this.textGps = textGPS;
		this.context = context;
		
	}
	
public boolean checkGps(){
    	 	
    	locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
        	textGps.setText("GPS is off, please turn it on");
        	return false;
        }
        	
        else
        	textGps.setText("GPS is enabled OK!!");
        
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle arg2) { 
                
            }
            
            @Override
            public void onProviderEnabled(String arg0) {
                
            }
            
            @Override
            public void onProviderDisabled(String arg0) {
                
            }
            
            @Override
            public void onLocationChanged(Location location) {
            	
            	    showLocation(location);
            	
            }
        };       
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
                3000, 
                2, 
                locationListener);
        
        
    	showLocation(location);
    	
    	return true;
    }


    public int getLatitude(){
    	
    	return (int) location.getLatitude();
    }
    
    public int getLongitude(){
    	
    	return (int) location.getLongitude();
    }
    
    private void showLocation(Location location) {
        String latitude  = "Latitude: ";
        String longitude = "Longitude: ";
        if(location == null) {
            latitude  += "null";
            longitude += "null";
        } else {
            latitude  += location.getLatitude();
            longitude += location.getLongitude();
        }
        
        //kordy.setText(latitude+" \n"+longitude);
    }

}
