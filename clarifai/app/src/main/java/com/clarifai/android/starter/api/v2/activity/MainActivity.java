package com.clarifai.android.starter.api.v2.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.clarifai.android.starter.api.v2.R;
import com.clarifai.android.starter.api.v2.service.ShakeService;


public class MainActivity extends AppCompatActivity {

    Button upperButton;
    Button lowerButton;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("com.clarifai.android.starter.api.v2", MODE_PRIVATE);

        Log.d("MainActivity", "in mainActivity onCreate()");
        Intent intent = new Intent(MainActivity.this, ShakeService.class);
        startService(intent);

        upperButton = (Button) findViewById(R.id.upper_button);
        lowerButton = (Button) findViewById(R.id.lower_button);

        upperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "short press upper", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(MainActivity.this, RecognizeConceptsActivity.class);
                startActivity(intent1);
            }
        });

        upperButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(MainActivity.this, "Long click upper", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(MainActivity.this, TextRecognizerMainActivity.class);
                startActivity(intent1);
                return true;
            }
        });

        lowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "short press lower", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(MainActivity.this, LocationRecognizerMainActivity.class);
                startActivity(intent1);
            }
        });

        lowerButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(MainActivity.this, "Long click lower", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(MainActivity.this, Registration.class);
        startActivity(intent);

        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs

            //Intent intent = new Intent(MainActivity.this, Registration.class);
            //startActivity(intent);

            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }
}
