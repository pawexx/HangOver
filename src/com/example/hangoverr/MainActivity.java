package com.example.hangoverr;

import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.app.AlertDialog.Builder;


public class MainActivity extends ActionBarActivity {

	protected static Settings settings;
	private SeekBar seekFreq;
	private SeekBar seekAsk;
	private TextView textFreq;
	private TextView textAsk;
	static TextView textGps;
	private TodoDbAdapter SqlAdapter;
	static GPSLocation gpsLocation;
	private Context context;	
	private static final int DIALOG_ALERT = 10;
	static final int DIALOG_PHOTO = 11;
	private static final String TAG ="RecordVideo";
	private Handler handler;
	private AlertDialog dialog;
	static AlertDialog dialogAsk;
	private Camera camera;
	private SurfaceHolder holder = null;
	private TextView recordingMsg = null;
	private VideoView videoView = null;
	private static final int CAMERA_REQUEST = 1888; 
	private ImageView imageView;
	private AlertRun alertRun;
	private SaveLocationRun saveLocationRun;
	private Thread threadAlert;
	private Thread threadSave;
	
	protected void onDestroy(){
		threadAlert.destroy();
		threadSave.destroy();
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		settings = new Settings();
		seekFreq = (SeekBar)findViewById(R.id.seekFreq);
		seekAsk = (SeekBar)findViewById(R.id.seekAsk);
		textFreq = (TextView)findViewById(R.id.textFreq);
		textAsk = (TextView)findViewById(R.id.textAsk);
		textGps = (TextView)findViewById(R.id.textGps);
		gpsLocation = new GPSLocation(textGps, this.getBaseContext());
		context = getBaseContext();
		alertRun = new AlertRun(getApplicationContext());
		saveLocationRun = new SaveLocationRun(getApplicationContext());
		threadAlert = new Thread(alertRun);
		threadSave = new Thread(saveLocationRun);

		//SqlAdapter = new TodoDbAdapter(this.getBaseContext());
		
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
		seekFreq.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
				textFreq.setText("Pobieraj lokalizacjê co: " + String.valueOf(progress)+" min");
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
						
			}
		});
		
		seekAsk.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
				textAsk.setText("Przypominaj o zdjêciu co: " + String.valueOf(progress)+" min");
		
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}	
			
		});
		
		//findViewById(R.id.seekFreq).
		findViewById(R.id.okButton).setOnClickListener(getFreq);

	}

	


	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_ALERT:
				Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Gotowe?");
				builder.setCancelable(true);
				builder.setPositiveButton("Tak", new OkOnClickListener());
				builder.setNegativeButton("Nie", new CancelOnClickListener());
				dialog = builder.create();
				//dialog.show();
			case DIALOG_PHOTO:
				Builder builder1 = new AlertDialog.Builder(this);
				builder1.setMessage("Chcesz teraz zrobiæ zdjêcie?");
				builder1.setCancelable(true);
				builder1.setPositiveButton("Tak", new OkPhotoOnClickListener());
				builder1.setNegativeButton("Nie", new CancelPhotoOnClickListener());
				dialogAsk = builder1.create();
				//dialog1.show();
		}
	return super.onCreateDialog(id);
	}
	 
	private final class CancelOnClickListener implements
	DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
		Toast.makeText(getApplicationContext(), "Nie to nie", Toast.LENGTH_LONG).show();
		}
	}
	 
	private final class OkOnClickListener implements
	DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			settings.setTimeAsk(seekAsk.getProgress());
			settings.setTimeFreq(seekFreq.getProgress());
			Toast.makeText(getApplicationContext(), "Udanej imprezy!! :)", Toast.LENGTH_LONG).show();		
			showDialog(DIALOG_PHOTO);
			if(!threadAlert.isAlive()) threadAlert.start();
			if(!threadSave.isAlive()) threadSave.start();
		
		}
	}
	
	private final class CancelPhotoOnClickListener implements
	DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
		Toast.makeText(getApplicationContext(), "Nastêpne przypomnienie za: "+ seekAsk.getProgress()+ " minut", Toast.LENGTH_LONG).show();
		}
	}
	 
	private final class OkPhotoOnClickListener implements
	DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
		//Toast.makeText(getApplicationContext(), "Ustawienia zapisane", Toast.LENGTH_LONG).show();
			
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
            startActivityForResult(cameraIntent, CAMERA_REQUEST); 
		
		
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            imageView.setImageBitmap(photo);
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_main, container,
					false);
			return rootView;
		}
	}
	

	View.OnClickListener getFreq = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(gpsLocation.checkGps()){
				showDialog(DIALOG_ALERT);
				dialog.show();
			 }

		}
				
	};
	
	
}
