package com.example.mydsapp;

import android.content.Intent;
import android.media.Image;
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

public class NSearchResult extends AppCompatActivity {

    TextView message1, message2;
    ImageView image;
    Button goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nsearch_result);

        message1 = findViewById(R.id.msg);
        message2 = findViewById(R.id.msg2);
        image = findViewById(R.id.imageView);

        goback = findViewById(R.id.returnbutton);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Search.class);
                startActivity(i);
            }
        });
    }
}