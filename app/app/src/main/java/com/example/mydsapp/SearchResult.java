package com.example.mydsapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class SearchResult extends AppCompatActivity {

    Button goback;
    TextView message;
    ListView listView;
    ArrayList<String> values = new ArrayList();
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        String result = getIntent().getStringExtra("server_result");

        Log.v(TAG, "Received response from server: " + result);

        String[] obj = result.split(";");

        for (String i : obj){
            String[] parts = i.split(",");
            for (String j : parts) {
                values.add(j);
            }
        }

        listView = findViewById(R.id.listView);

        MyAdapter adapter = new MyAdapter(this, getLayoutInflater(), values);

        listView.setAdapter(adapter);

        message = findViewById(R.id.msg);
        goback = findViewById(R.id.returnbutton);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SearchResult.this, "Clicked: " + values.get(i*7), Toast.LENGTH_SHORT).show();
                Intent in = new Intent(getApplicationContext(), SBookRoom.class);
                in.putExtra("clicked", values.get(i*7));
                startActivity(in);
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