package com.example.for_exam1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MenusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);
        ImageView imageViewBurgMenu = findViewById(R.id.imageViewBurgMenu);
        TextView textViewHistory = findViewById(R.id.textViewHistory);
        TextView textViewSettings = findViewById(R.id.textViewSettings);
        TextView textViewSurnameName = findViewById(R.id.textViewSurnameName);
        Intent this_page = getIntent();
        int token = this_page.getIntExtra("token", 0);
        imageViewBurgMenu.setOnClickListener(e ->
        {
            Intent intent = new Intent(this, StartScreenActivity.class);
            intent.putExtra("token", token);
            startActivity(intent);
            finish();

        });

        textViewHistory.setOnClickListener(e ->
        {
            Intent intent = new Intent(this, HistoryActivity.class);
            intent.putExtra("token", token);
            startActivity(intent);
            finish();
        });


        textViewSettings.setOnClickListener(e ->
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("token", token);
            startActivity(intent);
            finish();
        });

        String url = "http://cars.areas.su/profile";
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            JSONObject data = new JSONObject();
            data.put("token", token);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = data.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            String text = "";
            while((line = reader1.readLine()) != null)
            {
                text = line;
            }
            reader1.close();
            Log.i("Information", text);
            JSONObject jsonObj = new JSONObject(text);
            String fullname = jsonObj.getString("secondname") + " " + jsonObj.getString("firstname");
            textViewSurnameName.setText(fullname);

        } catch (IOException | JSONException d) {
            Toast.makeText(this, "Не удалось получить данные о пользователе", Toast.LENGTH_SHORT).show();
            Log.e("Error", d.toString());
        }
    }
}