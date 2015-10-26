package com.example.naveen.gps_tracking_with_sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Naveen on 15-10-2015.
 */
public class MyDBHandler extends SQLiteOpenHelper {
    private final String TAG = "com.example.naveen.gps_tracking_with_sql";

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME = "GPSLocation.db";
    public static final String TABLE_LOCATION = "LOCATIONS";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_TIME = "timestamp";



    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "ON CREATE OF DB HANDLER");

        String query= "CREATE TABLE "+TABLE_LOCATION+"( "+
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LATITUDE+" LONG, "+
                COLUMN_LONGITUDE+" LONG, "+
                COLUMN_TIME+" TEXT );";
        db.execSQL(query);
    }
    public void addLocation(Location1 location1){
        ContentValues values = new ContentValues();
        values.put(COLUMN_LATITUDE,location1.get_latitude());
        values.put(COLUMN_LONGITUDE,location1.get_longitude());
        values.put(COLUMN_TIME,location1.get_time());

        SQLiteDatabase db = getReadableDatabase();
        db.insert(TABLE_LOCATION, null, values);
        db.close();
        Log.i(TAG, "ADDTION HAPPENED");

    }
    public String databaseToString(){
        String dbString="";
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_LOCATION+" WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        Log.i(TAG, "DATABASE TO STRING REACHED");

        while (!c.isAfterLast()){
            Log.i(TAG,"IN THE WHILE LOOP");
            if(c.getString(c.getColumnIndex("timestamp"))!=null){
                dbString +=c.getString(c.getColumnIndex(COLUMN_ID))+"\t"+c.getString(c.getColumnIndex("timestamp"));
                dbString+="\n";

            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXITS "+TABLE_LOCATION );
        onCreate(db);
    }
}
