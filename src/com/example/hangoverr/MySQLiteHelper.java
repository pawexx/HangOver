package com.example.hangoverr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	  public static final String TABLE_Location = "Locations";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_Dlugosc = "dlugosc";
	  public static final String COLUMN_Szerokosc = "szerokosc";

	  private static final String DATABASE_NAME = "locations.db";
	  private static final int DATABASE_VERSION = 1;

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_Location + "(" + COLUMN_ID
	      + " integer primary key autoincrement," 
	      + COLUMN_Dlugosc + " INTEGER not null,"  
	      + COLUMN_Szerokosc + " INTEGER not null);";

	  public MySQLiteHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(MySQLiteHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_Location);
	    onCreate(db);
	  }

	} 
