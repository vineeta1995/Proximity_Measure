package com.example.vineeta.proximitymeasure;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;
import static java.lang.Math.abs;

public class FingerPrintingActivity extends AppCompatActivity {
    Button update, find;
    databaseHelper myDb;
    TextView sign, distance, meanSignal;
    String distanceValue;
    int dbm,means;
    float val,temp;
    int signalAverage = 0;
    String value;
    int clickcount = 0;
    float min = 99;
    int dist = 0;
    int temp1 = 0;
    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_printing);
        setContentView(R.layout.activity_finger_printing);
        update = (Button) findViewById(R.id.update);
        find = (Button) findViewById(R.id.distan);
        sign = (TextView) findViewById(R.id.sig);
        distance = (TextView) findViewById(R.id.dist);
        meanSignal = (TextView) findViewById(R.id.mean);
        update.setOnClickListener(updateOnClickListener);
        find.setOnClickListener(findOnClickListener);
        myDb = new databaseHelper(this);

    }

    private Context context;
    View.OnClickListener updateOnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent myIntent = new Intent(FingerPrintingActivity.this,
                            fingerPrintUpdateActivity.class);
                    startActivity(myIntent);
                }
            };
    View.OnClickListener findOnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    int rssi = wifiManager.getConnectionInfo().getRssi();
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo != null) {
                        dbm = wifiInfo.getRssi();
                        sign.setText(String.valueOf(dbm));
                        clickcount = clickcount + 1;
                        signalAverage += dbm;
                        if (clickcount == 1) {
                            //first time clicked to do this
                            Toast.makeText(getApplicationContext(), "Click button two more time", Toast.LENGTH_LONG).show();
                        } else if (clickcount == 2) {
                            //first time clicked to do this
                            Toast.makeText(getApplicationContext(), "Click button one more time", Toast.LENGTH_LONG).show();
                        } else if (clickcount == 3) {
                            Toast.makeText(getApplicationContext(), "Good Job", Toast.LENGTH_LONG).show();
                            means = (int)(signalAverage / 3);
                            meanSignal.setText(String.valueOf(means));
                            flag=0;
                            if (means <= -20 && means >= -30)
                            {
                                flag=1;
                                compute(-20, -30);
                            }
                            else if (means <= -31 && means >= -40) {
                                flag=1;
                                compute(-31, -40);
                            } else if (means <= -41 && means >= -50) {
                                flag=1;

                                compute(-41, -50);

                            } else if (means <= -51 && means >= -60) {
                                flag=1;

                                compute(-51, -60);

                            } else if (means <= -61 && means >= -70) {
                                flag=1;

                                compute(-61, -70);

                            }
                            else if (means <= -71 && means >= -80) {
                                flag=1;

                                compute(-71, -80);

                            }
                        }
                    }
                }
            };

    void compute(int sign1, int sign2) {
        Cursor res = myDb.getData(sign1, sign2);
        while (res.moveToNext()) {
            Log.d("tag", res.getString(0));
            Log.d("means", String.valueOf(means));
                val = Float.parseFloat(res.getString(0));
                temp = abs(val - means);
                temp1 = Integer.parseInt(res.getString(1));
                System.out.println(temp1);
                Log.d("distance", String.valueOf(temp1));
                if (temp < min) {
                    min = temp;
                    dist = temp1;
                }
        }
        distance.setText(String.valueOf(dist));
    }
}