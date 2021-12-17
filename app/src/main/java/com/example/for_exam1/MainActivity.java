package com.example.for_exam1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private EditText etLogin, etPassword;
    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);
        TextView textViewCreateAccount = findViewById(R.id.textViewCreateAccount);
        Button btnSubmit = findViewById(R.id.buttonSubmitSignIn);
        etLogin = findViewById(R.id.editTextLogin);
        etPassword = findViewById(R.id.editTextPassword);
        btnSubmit.setOnClickListener(e ->
        {
            if(!etLogin.getText().toString().trim().equals("") && !etPassword.getText().toString().trim().equals(""))
            {
                String url = "http://cars.areas.su/login";
                try
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestProperty("Content-Type", "application/json; utf-8");
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    JSONObject data = new JSONObject();
                    data.put("username", etLogin.getText());
                    data.put("password", etPassword.getText());
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
                    int token = jsonObj.getJSONObject("notice").getInt("token");
                    Log.d("Bred", jsonObj.toString());
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("token", token);
                    startActivity(intent);
                    finish();
                } catch (IOException | JSONException d) {
                    Log.e("Error", d.toString());
                    Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT);
                }
            }
            else
                {
                    Toast.makeText(this, "Заполните все поля!!!", Toast.LENGTH_SHORT);
                }
        });


        textViewCreateAccount.setOnClickListener(e->
        {
            Intent intent = new Intent(this, MainActivityRegister.class);
            startActivity(intent);
            finish();
        });
    }
}