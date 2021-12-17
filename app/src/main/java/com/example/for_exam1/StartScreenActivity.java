package com.example.for_exam1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;


public class StartScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        Intent this_page = getIntent();
        int token = this_page.getIntExtra("token", 0);
        ImageButton imageButtonMenuBurger = findViewById(R.id.imageButtonMenuBurger);
        imageButtonMenuBurger.setOnClickListener( e ->
        {
            Intent intent = new Intent(this, MenusActivity.class);
            intent.putExtra("token", token);
            startActivity(intent);
            finish();
        });


    }
}