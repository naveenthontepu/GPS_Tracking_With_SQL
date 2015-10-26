package com.example.naveen.gps_tracking_with_sql;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GPS_Tracking extends AppCompatActivity {
//    TextView updated_Data;
    TextView database_printing;
    MyDBHandler dbhandler1;
    private int servicevariable=1;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps__tracking);
        database_printing = (TextView)findViewById(R.id.database_printing);
        dbhandler1 = new MyDBHandler(this,null,null,1);
        pref=getSharedPreferences("GPS_Tracking",MODE_PRIVATE);
        editor = pref.edit();

    }

    public void startTracking(View view){
        if(pref.getInt("servicevariable",1)==1){
            Intent intent = new Intent(this,Gps_Tracking_Service.class);
            startService(intent);
            editor.putInt("servicevariable", 2);
            editor.commit();
        }

    }
    public void stopTracking(View view){
        Intent intent = new Intent(this,Gps_Tracking_Service.class);
        stopService(intent);
        editor.putInt("servicevariable",1);
        editor.commit();
    }

    public void display_database(View view){
        String dbitems;
        dbitems = dbhandler1.databaseToString();
        database_printing.setText(dbitems);


    }
    public void destroy_database(View view){

        dbhandler1.deletingDatabase();
        database_printing.setText("Previous Data Deleted");
    }

    /*public void update_text(String s){
        updated_Data = (TextView)findViewById(R.id.updated_Data);
        updated_Data.setText(s);

    }*/
}
