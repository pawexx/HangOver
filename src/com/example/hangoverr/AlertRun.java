package com.example.hangoverr;

import java.util.concurrent.ThreadFactory;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

public class AlertRun implements Runnable {
	 
    private Handler handler;
 
    public AlertRun( final Context context) {
        
        this.handler = new Handler() {
   		 
            @Override
            public void handleMessage(Message msg) {
    	        super.handleMessage(msg);
    	        //showDialog(Integer.valueOf(msg.obj.toString())); 
    	        if(!MainActivity.dialogAsk.isShowing()){
    	        	MainActivity.dialogAsk.show();
    	        	try {
    	        	    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    	        	    Ringtone r = RingtoneManager.getRingtone(context, notification);
    	        	    r.play();
    	        	} catch (Exception e) {
    	        	    e.printStackTrace();
    	        	}
    	        }
            }
        };
        
    }
    
     
    public Handler getHandler(){
    	return handler;
    }
    
    
    @Override
    public void run() {       
            int dialog = MainActivity.DIALOG_PHOTO;
            try {
                while (true) {
    	            Thread.sleep(MainActivity.settings.getTimeAsk()*60000);	 
    	            Message wiadomosc = new Message();
    	            wiadomosc.obj = dialog;
    	            handler.sendMessage(wiadomosc);
                }
     
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
