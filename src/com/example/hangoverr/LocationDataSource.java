package com.example.hangoverr;



import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LocationDataSource {

	  // Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
	      MySQLiteHelper.COLUMN_Dlugosc, MySQLiteHelper.COLUMN_Szerokosc };

	  public LocationDataSource(Context context) {
	    dbHelper = new MySQLiteHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public Location createLocation(int dlugosc,int szerokosc ) {
	    ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.COLUMN_Dlugosc, dlugosc);
	    values.put(MySQLiteHelper.COLUMN_Szerokosc, szerokosc);
	    long insertId = database.insert(MySQLiteHelper.TABLE_Location, null,
	        values);
	    Cursor cursor = database.query(MySQLiteHelper.TABLE_Location,
	        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Location newLocation = cursorToLocation(cursor);
	    cursor.close();
	    return newLocation;
	  }

	  public void deleteLocation(Location location) {
	    long id = location.getId();
	    System.out.println("Comment deleted with id: " + id);
	    database.delete(MySQLiteHelper.TABLE_Location, MySQLiteHelper.COLUMN_ID
	        + " = " + id, null);
	  }

	  public List<Location> getAllLocations() {
	    List<Location> locations = new ArrayList<Location>();

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_Location,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Location location = cursorToLocation(cursor);
	      locations.add(location);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return locations;
	  }

	  private Location cursorToLocation(Cursor cursor) {
	    Location location = new Location();
	    location.setId(cursor.getLong(0));
	    location.setDlugosc(Integer.parseInt(cursor.getString(1)));
	    location.setSzerokosc(Integer.parseInt(cursor.getString(2)));
	    return location;
	  }
	} 