package com.example.for_exam1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        TextView textViewExit = findViewById(R.id.textViewExit);
        TextView textViewEmail = findViewById(R.id.textViewEmail);
        TextView textViewHours = findViewById(R.id.textViewHours);
        TextView textViewPaidDollar = findViewById(R.id.textViewPaidDollar);
        TextView textViewSurnameName = findViewById(R.id.textViewSurnameName);
        ImageView imageViewBurgMenuSettings = findViewById(R.id.imageViewBurgMenuSettings);

        Intent this_page = getIntent();
        int token = this_page.getIntExtra("token", 0);
        String username = "";
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
            String text_response = "";
            while((line = reader1.readLine()) != null)
            {
                text_response = line;
            }
            reader1.close();
            Log.i("Information", text_response);
            JSONObject jsonObj = new JSONObject(text_response);
            String fullname = jsonObj.getString("secondname") + " " + jsonObj.getString("firstname");
            String email = textViewEmail.getText().toString() + " " + jsonObj.getString("email");
            String timeDrive = jsonObj.getString("timeDrive")+ " hours" ;
            String cash = "$" + jsonObj.getString("cash");
            username = jsonObj.getString("username");

            textViewSurnameName.setText(fullname);
            textViewEmail.setText(email);
            textViewHours.setText(timeDrive);
            textViewPaidDollar.setText(cash);

        } catch (IOException | JSONException d) {
            Toast.makeText(this, "Не удалось получить данные о пользователе", Toast.LENGTH_SHORT);
            Log.e("Error", d.toString());
        }

        imageViewBurgMenuSettings.setOnClickListener( e ->
        {
            Intent intent = new Intent(this, MenusActivity.class);
            intent.putExtra("token", token);
            startActivity(intent);
            finish();
        });


        String finalUsername = username;
        textViewExit.setOnClickListener(e ->
        {
            String url_logout = "http://cars.areas.su/logout";
            try
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpURLConnection connection = (HttpURLConnection) new URL(url_logout).openConnection();
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                JSONObject data = new JSONObject();
                data.put("username", finalUsername);
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
                String message = jsonObj.getJSONObject("notice").getString("text");
                if(message.equals("User log out"))
                {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                    {
                        Toast.makeText(this, "Произошла неизвестная ошибка", Toast.LENGTH_SHORT).show();
                    }
            } catch (IOException | JSONException d) {
                Toast.makeText(this, "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                Log.e("Error", d.toString());

            }

        });

    }
}