package com.example.mydsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AddRoomsResult extends AppCompatActivity {

    Button goback;
    TextView textView1;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rooms_result);

        String result = getIntent().getStringExtra("server_result");

        textView1 = findViewById(R.id.msg);
        image = findViewById(R.id.imageView2);
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