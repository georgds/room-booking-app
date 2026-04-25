package com.example.mydsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


public class ManagerMenu extends AppCompatActivity {

    private TextView textView;
    private Button adddatebutton, addroomsbutton, reservationsbutton, statisticsbutton, returnbutton;
    ArrayList request = new ArrayList();
    private static final String TAG = "MainActivity";

    Handler responseHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            Log.v(TAG, "Received response from server: " + result);
            Intent i = new Intent(getApplicationContext(), PrintAccommodation.class);
            i.putExtra("server_result", result);
            startActivity(i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_menu);

        textView = findViewById(R.id.options);

        adddatebutton = findViewById(R.id.adddatebutton);
        addroomsbutton = findViewById(R.id.addroomsbutton);
        reservationsbutton = findViewById(R.id.reservationsbutton);
        statisticsbutton = findViewById(R.id.statisticsbutton);
        returnbutton = findViewById(R.id.returnbutton);

        addroomsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddRooms.class);
                startActivity(i);
            }
        });

        adddatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddDates.class);
                startActivity(i);
            }
        });

        reservationsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myHandler handler = new myHandler(responseHandler);

                request.add("Manager");
                request.add("3");
                handler.sendToServer(request);
            }
        });

        statisticsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Statistics.class);
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