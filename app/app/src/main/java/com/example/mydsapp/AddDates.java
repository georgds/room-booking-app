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

public class AddDates extends AppCompatActivity {

    EditText name, date1, date2;
    TextView textView;
    Button next, goback;
    ArrayList request = new ArrayList();
    private static final String TAG = "MainActivity";

    Handler responseHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            Log.v(TAG, "Received response from server: " + result);
            Intent i = new Intent(getApplicationContext(), AddDatesResult.class);
            i.putExtra("server_result", result);
            startActivity(i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dates);

        textView = findViewById(R.id.message);
        name = findViewById(R.id.name);
        date1 = findViewById(R.id.date1);
        date2 = findViewById(R.id.date2);
        next = findViewById(R.id.next);
        goback = findViewById(R.id.goback);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myHandler handler = new myHandler(responseHandler);
                String roomName = name.getText().toString();
                String firstdate = date1.getText().toString();
                String lastdate = date2.getText().toString();

                request.add("Manager");
                request.add("2");
                request.add(roomName);
                request.add(firstdate);
                request.add(lastdate);
                handler.sendToServer(request);
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ManagerMenu.class);
                startActivity(i);
            }
        });

    }
}