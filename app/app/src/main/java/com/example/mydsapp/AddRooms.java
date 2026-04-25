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

public class AddRooms extends AppCompatActivity {

    TextView textview;
    EditText path;
    Button next, goback;
    ArrayList request = new ArrayList();
    private static final String TAG = "MainActivity";

    Handler responseHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            Log.v(TAG, "Received response from server: " + result);
            if (!result.equals("")) {
                Intent i = new Intent(getApplicationContext(), AddRoomsResult.class);
                startActivity(i);
            } else {
                Intent i = new Intent(getApplicationContext(), NAddRoomsResult.class);
                startActivity(i);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rooms);

        textview = findViewById(R.id.message);
        path = findViewById(R.id.path);
        next = findViewById(R.id.next);
        goback = findViewById(R.id.goback);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myHandler handler = new myHandler(responseHandler);
                String jsonpath = path.getText().toString();

                request.add("Manager");
                request.add("1");
                request.add(jsonpath);
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