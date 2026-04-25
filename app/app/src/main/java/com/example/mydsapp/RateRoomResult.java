package com.example.mydsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RateRoomResult extends AppCompatActivity {

    Button goback;
    TextView textView1, resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_room_result);

        String result = getIntent().getStringExtra("server_result");

        textView1 = findViewById(R.id.msg);
        resultTextView = findViewById(R.id.resultTextView2);
        resultTextView.setText(result);
        goback = findViewById(R.id.returnbutton);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ClientMenu.class);
                startActivity(i);
            }
        });
    }
}