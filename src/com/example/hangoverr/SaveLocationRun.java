package com.example.hangoverr;

import java.util.List;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class SaveLocationRun implements Runnable {
	 
    private Handler handler;
    private TodoDbAdapter db;
    private LocationDataSource dataSource;
    private GPSLocation gpsLocation;
    
    
 
    public SaveLocationRun( final Context context) {
    	
    	dataSource = new LocationDataSource(context);		
    	gpsLocation = MainActivity.gpsLocation;
        
        handler = new Handler() {
   		 
            @Override
            public void handleMessage(Message msg) {
    	        super.handleMessage(msg);
    	        //obsluga zapisu do bazy
    	        dataSource.open();
    	        dataSource.createLocation(gpsLocation.getLongitude(),gpsLocation.getLatitude());   
    	    	dataSource.close();
    	    	Toast.makeText(context, "Lokalizacja zapisana do bazy", Toast.LENGTH_LONG).show();
            }
        };
        
    }
    
    @Override
    public void run() {       
    	int x = 69;
            try {
                while (true) {
    	            Thread.sleep(MainActivity.settings.getTimeFreq()*60000);	 
    	            Message wiadomosc = new Message();
    	            wiadomosc.obj = x;
    	            handler.sendMessage(wiadomosc);
                }
     
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
