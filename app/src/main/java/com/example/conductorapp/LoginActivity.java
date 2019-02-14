package com.example.conductorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.conductorapp.utils.StringCreator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText usn, pass;
    Button login;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usn = findViewById(R.id.busno);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);

        register = findViewById(R.id.reg);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, BusRegisterEvent.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        String busNo = usn.getText().toString();
        String password = pass.getText().toString();
        try {
            //AJAX
            OkHttpClient client = new OkHttpClient();
            String content;
            StringCreator helper = new StringCreator();
            helper.addParam("id", busNo);
            helper.addParam("password", password);
            content = helper.getPOST_PARAMS();
            Request request = new Request.Builder().url("http://192.168.137.1:3001/bus/login").post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), content)).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    JSONObject res = null;
                    try {
                        res = new JSONObject(response.body().string());
                        if (response.isSuccessful()) {
                            if (res.getString("success").equals("true")) {
                                System.out.println("Registered!!");
                                SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("TOKEN", res.getString("jwt")).apply();
                                editor.putString("NAME", res.getString("id")).apply();
                                editor.putString("NUM", res.getString("id")).apply();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra("head", "Login Failed");
                            intent.putExtra("description", "Check your number and password");
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
