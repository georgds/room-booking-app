package com.example.mydsapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class PrintAccommodation extends AppCompatActivity {

    private static final String TAG = "kati: ";
    Button goback;
    TextView message;
    ListView listView;
    ArrayList<String> values = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_accommodation);

        String result = getIntent().getStringExtra("server_result");

        Log.v(TAG, "Received response from server: " + result);

        String[] rooms = result.split("\n");

        for (String room : rooms) {
            Log.v(TAG, "Received response from server: " + room);
            String[] parts = room.split(" is booked for the following days: ");

            String roomName = parts[0].substring(5);
            Log.v(TAG, "Received response from server: " + roomName);

            if (parts[1].equals("[]")){
                values.add(roomName);
                values.add("No reservations");
            } else {
                String dates = parts[1].substring(2, parts[1].length() - 2);
                String[] each_date = dates.split("\\), \\(");

                for (String date : each_date){
                    values.add(roomName);
                    values.add("(" + date + ")");
                }
            }
        }

        for (String kati : values) {
            Log.v(TAG, "Received response from server: " + kati);
        }

        listView = findViewById(R.id.listView);

        MyAccommodationAdapter adapter = new MyAccommodationAdapter(getLayoutInflater(), values);

        listView.setAdapter(adapter);

        message = findViewById(R.id.msg);
        goback = findViewById(R.id.returnbutton);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ManagerMenu.class);
                startActivity(i);
            }
        });
    }
}