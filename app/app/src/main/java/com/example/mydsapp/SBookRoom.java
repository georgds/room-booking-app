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

public class SBookRoom extends AppCompatActivity {

    TextView name, textView;
    EditText date1, date2;
    Button next, goback;
    ArrayList request = new ArrayList();
    private static final String TAG = "MainActivity";

    Handler responseHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            Log.v(TAG, "Received response from server: " + result);
            if (result.contains("done")) {
                Intent i = new Intent(getApplicationContext(), BookRoomResult.class);
                i.putExtra("server_result", result);
                startActivity(i);
            } else if (result.contains("available")){
                Intent i = new Intent(getApplicationContext(), NBookRoomResult.class);
                i.putExtra("server_result", result);
                startActivity(i);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbook_room);

        String sname = getIntent().getStringExtra("clicked");

        textView = findViewById(R.id.message);
        name = findViewById(R.id.name);
        name.setText(sname);
        date1 = findViewById(R.id.date1);
        date2 = findViewById(R.id.date2);
        next = findViewById(R.id.next);
        goback = findViewById(R.id.goback);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myHandler handler = new myHandler(responseHandler);
                String firstdate = date1.getText().toString();
                String lastdate = date2.getText().toString();

                request.add("Client");
                request.add("2");
                request.add(sname);
                request.add(firstdate);
                request.add(lastdate);
                handler.sendToServer(request);
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchResult.class);
                startActivity(i);
            }
        });
    }
}