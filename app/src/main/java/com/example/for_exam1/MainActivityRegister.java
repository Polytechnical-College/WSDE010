package com.example.for_exam1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivityRegister extends AppCompatActivity {

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);
        EditText editTextLogin = findViewById(R.id.editTextLogin);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        EditText editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        Button btnSignUp = findViewById(R.id.buttonSubmitSignUp);
        btnSignUp.setOnClickListener(e->
        {
            if(!editTextConfirmPassword.getText().toString().trim().equals("") && !editTextEmail.getText().toString().trim().equals("") &&
                    !editTextLogin.getText().toString().trim().equals("") && !editTextPassword.getText().toString().trim().equals(""))
            {
                if(editTextConfirmPassword.getText().toString().equals(editTextPassword.getText().toString()))
                {
                    String url = "http://cars.areas.su/signup";
                    try
                    {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                        connection.setRequestProperty("Content-Type", "application/json; utf-8");
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);
                        JSONObject data = new JSONObject();
                        data.put("username", editTextLogin.getText());
                        data.put("password", editTextPassword.getText());
                        data.put("email", editTextEmail.getText());
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
                        Log.d("Bred", jsonObj.toString());
                        JSONObject token = (JSONObject) jsonObj.get("notice");
                        String answer = token.get("answer").toString();
                        if(answer.equals("Success"))
                        {
                            Toast.makeText(this, "Регистрация прошла успешно", Toast.LENGTH_SHORT);
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(this, "Что-то пошло не так, попробуйте ввести другие данные в поля", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException d) {
                        Toast.makeText(this, "Произошла неизвестная ошибка", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "Произошла ошибка");
                    }
                }
                else
                    {
                        Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                    }
            }
            else
                {
                    Toast.makeText(this, "Заполните все поля!!!", Toast.LENGTH_SHORT).show();
                }
        });
    }
}