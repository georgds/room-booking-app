package com.example.mydsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class Search extends AppCompatActivity {

    TextView textView1, textView2, textView3, textView4, textView5, textView6;
    Button next, goback;
    EditText area, dates, people, price, stars;
    ArrayList request = new ArrayList();
    private static final String TAG = "MainActivity";

    Handler responseHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            Log.v(TAG, "Received response from server: " + result);

            if (result.equals("No rooms found!")){
                Intent i = new Intent(getApplicationContext(), NSearchResult.class);
                startActivity(i);
            } else {
                Intent i = new Intent(getApplicationContext(), SearchResult.class);
                i.putExtra("server_result", result);
                startActivity(i);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        textView1 = findViewById(R.id.tfilters);
        textView2 = findViewById(R.id.tarea);
        textView3 = findViewById(R.id.tdates);
        textView4 = findViewById(R.id.tpeople);
        textView5 = findViewById(R.id.tprice);
        textView6 = findViewById(R.id.tstars);
        next = findViewById(R.id.next);
        goback = findViewById(R.id.returnbutton);
        area = findViewById(R.id.area);
        dates = findViewById(R.id.dates);
        people = findViewById(R.id.people);
        price = findViewById(R.id.price);
        stars = findViewById(R.id.stars);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myHandler handler = new myHandler(responseHandler);
                String rarea = area.getText().toString();
                String rdates = dates.getText().toString();
                String rpeople = people.getText().toString();
                String rprice = price.getText().toString();
                String rstars = stars.getText().toString();

                request.add("Client");
                request.add("1");
                if (!TextUtils.isEmpty(rarea)){
                    request.add("1");
                    request.add(rarea);
                }
                if (!TextUtils.isEmpty(rdates)){
                    request.add("2");
                    request.add(rdates);
                }
                if (!TextUtils.isEmpty(rpeople)){
                    request.add("3");
                    request.add(rpeople);
                }
                if (!TextUtils.isEmpty(rprice)){
                    request.add("4");
                    request.add(rprice);
                }
                if (!TextUtils.isEmpty(rstars)){
                    request.add("5");
                    request.add(rstars);
                }
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