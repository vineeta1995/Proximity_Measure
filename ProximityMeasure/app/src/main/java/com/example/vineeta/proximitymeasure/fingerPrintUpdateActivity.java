package com.example.vineeta.proximitymeasure;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class fingerPrintUpdateActivity extends AppCompatActivity {
    databaseHelper myDb;
    EditText distance, place;
    TextView signal, meanSignal;
    Button b1,b2,b3,b4;
    int dbm;
    float mean;
    int[] array = new int[3];
    int signalAverage = 0;
    String value;
    int clickcount = 0;
    boolean isInserted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print_update);
        distance = (EditText) findViewById(R.id.distance);
        place = (EditText) findViewById(R.id.place);
        signal = (TextView) findViewById(R.id.signal);
        meanSignal = (TextView) findViewById(R.id.mean);
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.Update);
        b3 = (Button) findViewById(R.id.view);
        b4 = (Button) findViewById(R.id.delete);
        b1.setOnClickListener(b1OnClickListener);
        b2.setOnClickListener(b2OnClickListener);
        b3.setOnClickListener(b3OnClickListener);
        b4.setOnClickListener(b4OnClickListener);
        myDb = new databaseHelper(this);
    }
    private Context context;
    View.OnClickListener b1OnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    int rssi = wifiManager.getConnectionInfo().getRssi();
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo != null) {
                        dbm = wifiInfo.getRssi();
                        signal.setText(String.valueOf(dbm));
                        clickcount = clickcount + 1;
                        value = signal.getText().toString();
                        signalAverage += Integer.parseInt(value);
                        if (clickcount == 1) {
                            //first time clicked to do this
                            Toast.makeText(getApplicationContext(), "Click button two more time", Toast.LENGTH_LONG).show();
                        } else if (clickcount == 2) {
                            //first time clicked to do this
                            Toast.makeText(getApplicationContext(), "Click button one more time", Toast.LENGTH_LONG).show();
                        } else if (clickcount == 3) {
                            Toast.makeText(getApplicationContext(), "Good Job", Toast.LENGTH_LONG).show();
//                            b1.setEnabled(false);
                            mean = signalAverage / 3;
                            meanSignal.setText(String.valueOf(mean));
                        }
                    }
                }
            };

    View.OnClickListener b2OnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Log.d("TAG","print");

                    isInserted = myDb.insertData(place.getText().toString(),
                                meanSignal.getText().toString(),
                                distance.getText().toString() );
                        if(isInserted)
                            Toast.makeText(fingerPrintUpdateActivity.this,"Data Inserted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(fingerPrintUpdateActivity.this,"Data not Inserted",Toast.LENGTH_LONG).show();
                    }
                };
  View.OnClickListener b3OnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                        Cursor res = myDb.getAllData();
                        if(res.getCount() == 0) {
                            // show message
                            showMessage("Error","Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Place :"+ res.getString(0)+"\n");
                            buffer.append("Signal :"+ res.getString(1)+"\n");
                            buffer.append("Distance :"+ res.getString(2)+"\n");
                        }

                        // Show all data
                        showMessage("Data",buffer.toString());
                    }
                };

    View.OnClickListener b4OnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                        Integer deletedRows = myDb.deleteData(place.getText().toString());
                        if(deletedRows > 0)
                            Toast.makeText(fingerPrintUpdateActivity.this,"Data Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(fingerPrintUpdateActivity.this,"Data not Deleted",Toast.LENGTH_LONG).show();
                    }
                };

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
