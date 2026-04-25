package com.example.mydsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class RateRoom extends AppCompatActivity {

    TextView textview;
    EditText name, stars;
    Button next, goback;
    ArrayList request = new ArrayList();
    private static final String TAG = "MainActivity";

    Handler responseHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            Log.v(TAG, "Received response from server: " + result);
            Intent i = new Intent(getApplicationContext(), RateRoomResult.class);
            i.putExtra("server_result", result);
            startActivity(i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_room);

        textview = findViewById(R.id.message);
        name = findViewById(R.id.name);
        stars = findViewById(R.id.stars);
        next = findViewById(R.id.next);
        goback = findViewById(R.id.goback);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myHandler handler = new myHandler(responseHandler);
                String roomName = name.getText().toString();
                String rstars = stars.getText().toString();

                request.add("Client");
                request.add("3");
                request.add(roomName);
                request.add(rstars);
                handler.sendToServer(request);
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ClientMenu.class);
                startActivity(i);
            }
        });
    }
}