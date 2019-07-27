package com.example.vineeta.proximitymeasure;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class LaterationActivity extends AppCompatActivity {
    EditText editTextAddress, editTextPort, editTextMsg;
    Button buttonConnect;
    TextView pong,time,strength,distance1,distance2;
    long startTime,endTime;
    double differ,dbm;
    double dist,dist1;
    double speed=3*Math.pow(10,8);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lateration);
        editTextAddress = (EditText) findViewById(R.id.address);
        editTextPort = (EditText) findViewById(R.id.port);
        editTextMsg = (EditText) findViewById(R.id.msgtosend);
        buttonConnect = (Button) findViewById(R.id.connect);
        pong = (TextView) findViewById(R.id.pong);
        time= (TextView)findViewById(R.id.diff);
        strength=(TextView)findViewById(R.id.strength);
        distance1=(TextView)findViewById(R.id.distance1);
        distance2=(TextView)findViewById(R.id.distance2);
        buttonConnect.setOnClickListener(buttonConnectOnClickListener);
    }

    private Context context;
    View.OnClickListener buttonConnectOnClickListener =
            new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0) {
                    startTime = System.nanoTime();
                    MyClientTask myClientTask = new MyClientTask(
                            editTextAddress.getText().toString(),
                            Integer.parseInt(editTextPort.getText().toString()));
                    myClientTask.execute(editTextMsg.getText().toString());
                }
            };

    public class MyClientTask extends AsyncTask<String, Void, String>
    {

        String dstAddress;
        int dstPort;
        String response;

        MyClientTask(String addr, int port)
        {
            dstAddress = addr;
            dstPort = port;
        }


        @Override
        protected String doInBackground(String... params) // uses string parameter of Async(here address and port number)
        {
            String message = "";
            try {
                Socket socket = new Socket(dstAddress, dstPort);
                // System.out.println(dstAddress + "check this" + dstPort);
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                String sendMessage = editTextMsg.getText().toString() + "\n";
                bw.write(sendMessage);
                bw.flush();
                System.out.println("Message sent to the server : " + sendMessage);

                //Get the return message from the server
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                message += br.readLine();
                System.out.println("Message received from the server : " + message);
                final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                int rssi = wifiManager.getConnectionInfo().getRssi();
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if(wifiInfo != null)
                {
                    dbm = wifiInfo.getRssi();
                    System.out.println("yooo"+dbm);
                    int strengthInPercentage = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 100);
                    System.out.println("Level is " + strengthInPercentage+ " out of 100");
                    double freqInMHz=2437;
                    double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(dbm)) / 20.0;// correct one
                    dist= Math.pow(10.0, exp);
                    System.out.println("distn"+dist);
                    double fspl=1496+2-(Math.abs(dbm))-22;
                    double x=(fspl-27.55-(20*Math.log10(freqInMHz)))/20.0;
                    double dist2=Math.pow(10,x);
                    System.out.println("new dist"+dist2);
                }
            }

            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return message;
        }

        @Override
        protected void onPostExecute(String result)
        {
            pong.setText(result);
            endTime=System.nanoTime();
            differ= (double) (endTime-startTime);
            time.setText(String.valueOf(differ));
            strength.setText(String.valueOf("strength" + dbm));
            distance1.setText(String.valueOf("fspl" + dist));
            distance2.setText(String.valueOf("lateration"+ differ*speed));
        }
    }
}

