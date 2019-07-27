package com.example.vineeta.proximitymeasure;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class modifiedActivity extends AppCompatActivity {
    Button b1;
    TextView strength, distance1, distance2;
    int dbm;
    double dist, dist1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modified);
        strength = (TextView) findViewById(R.id.signal);
        distance1 = (TextView) findViewById(R.id.dist1);
        b1 = (Button) findViewById(R.id.find);
        b1.setOnClickListener(b1OnClickListener);

    }

    Context context;
    View.OnClickListener b1OnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    int rssi = wifiManager.getConnectionInfo().getRssi();
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo != null) {
                        dbm = wifiInfo.getRssi();
                        int strengthInPercentage = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 100);
                        double freqInMHz = 2437;
                        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(dbm)) / 20.0;// correct one
                        dist = Math.pow(10.0, exp);
                        //change TX power
                        double exp1 = (20 - Math.abs(dbm) - 27.55 - 15 - (20 * Math.log10(freqInMHz))) / 20.0;//this isnt correct for now
                        dist1 = Math.pow(10.0, exp1);
                        strength.setText(String.valueOf(dbm));
                        distance1.setText(String.valueOf(dist));
                    }
                }
            };
}
