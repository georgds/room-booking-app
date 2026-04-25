package com.example.mydsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NAddRoomsResult extends AppCompatActivity {

    TextView message;
    ImageView image;
    Button goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nadd_rooms_result);

        message = findViewById(R.id.msg);
        image = findViewById(R.id.imageView);
        goback = findViewById(R.id.returnbutton);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddRooms.class);
                startActivity(i);
            }
        });
    }
}