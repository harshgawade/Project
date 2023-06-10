package com.example.plant_leaf_disase_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class home extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        bottomNavigationView = findViewById(R.id.bottom);




        Button btnlocation = findViewById(R.id.btnlocation);
        btnlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),location.class);
                startActivity(i);

            }
        });


        Button btnweather = findViewById(R.id.btnweather);
        btnweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i1 = new Intent(getApplicationContext(),weather.class);
                startActivity(i1);

            }
        });


        Button btndetection = findViewById(R.id.btndetection);
        btndetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i2 = new Intent(getApplicationContext(),detection.class);
                startActivity(i2);

            }
        });

    }
}