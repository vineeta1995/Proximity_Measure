package com.example.vineeta.proximitymeasure;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class MainActivity extends AppCompatActivity {
    Button b1, b2, b3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button) findViewById(R.id.fingerprinting);
        b2 = (Button) findViewById(R.id.modified);
        b3 = (Button) findViewById(R.id.transmitting);
        b1.setOnClickListener(b1OnClickListener);
        b2.setOnClickListener(b2OnClickListener);
        b3.setOnClickListener(b2OnClickListener);
    }
    private Context context;
    View.OnClickListener b1OnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent myIntent = new Intent(MainActivity.this,
                            FingerPrintingActivity.class);
                    startActivity(myIntent);
                }
            };
    View.OnClickListener b2OnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent myIntent = new Intent(MainActivity.this,
                            modifiedActivity.class);
                    startActivity(myIntent);
                }
            };
    View.OnClickListener b3OnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent myIntent = new Intent(MainActivity.this,
                            LaterationActivity.class);
                    startActivity(myIntent);
                }
            };
}
