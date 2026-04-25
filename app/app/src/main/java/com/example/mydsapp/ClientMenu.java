package com.example.mydsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ClientMenu extends AppCompatActivity {

    TextView textView;
    Button searchbutton, reservebutton, ratebutton, returnbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_menu);

        textView = findViewById(R.id.option);
        searchbutton = findViewById(R.id.searchbutton);
        reservebutton = findViewById(R.id.reservebutton);
        ratebutton = findViewById(R.id.ratebutton);
        returnbutton = findViewById(R.id.returnbutton);

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Search.class);
                startActivity(i);
            }
        });

        reservebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BookRoom.class);
                startActivity(i);
            }
        });

        ratebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RateRoom.class);
                startActivity(i);
            }
        });

        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }
}