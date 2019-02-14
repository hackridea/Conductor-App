package com.example.conductorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class BusRegisterEvent extends AppCompatActivity implements View.OnClickListener {
    EditText name, no, pass, cpass;
    TextView login;
    Button register;
    private String busNo, busName, password, cpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_register_event);
        name = findViewById(R.id.name);
        no = findViewById(R.id.no);
        pass = findViewById(R.id.pass);
        cpass = findViewById(R.id.cpass);
        login = findViewById(R.id.logn);
        register = findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        busNo = no.getText().toString();
        busName = name.getText().toString();
        password = pass.getText().toString();
        cpassword = cpass.getText().toString();

        if (busNo != "" || busName != "" || password != "" || cpassword != "") {
            if (!TextUtils.equals(cpassword, password)) {
                Toast.makeText(BusRegisterEvent.this, "Password should be same.", Toast.LENGTH_SHORT).show();
            } else if (!busNo.matches("[a-zA-Z]{2}[ -][0-9]{2}[- ][a-zA-Z]{2}[- ][0-9]{4}")) {
                Toast.makeText(BusRegisterEvent.this, "Enter valid vehicle register no.", Toast.LENGTH_SHORT).show();
            } else if (cpassword.length() < 6) {
                Toast.makeText(BusRegisterEvent.this, "Password should be of at least 6 length", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    //AJAX
                    OkHttpClient client = new OkHttpClient();
                    String content;
                    StringCreator helper = new StringCreator();
                    helper.addParam("id", busNo);
                    helper.addParam("name", busName);
                    helper.addParam("password", password);
                    content = helper.getPOST_PARAMS();
                    Request request = new Request.Builder().url("http://192.168.137.1:3001/bus/register").post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), content)).build();
                    client.newCall(request).enqueue(new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            System.out.println(e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            try {
                                JSONObject res = new JSONObject(response.body().string());
                                if (response.isSuccessful()) {
                                    if (res.getString("success").equals("true")) {
                                        System.out.println("Registered!!");
                                        SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("TOKEN", res.getString("jwt")).apply();
                                        editor.putString("NAME", res.getString("id")).apply();
                                        editor.putString("NUM", res.getString("id")).apply();
                                        Intent intent = new Intent(BusRegisterEvent.this, HomeActivity.class);
                                        intent.putExtra("NAME", res.getString("name"));
                                        intent.putExtra("NUM", res.getString("id"));
                                        startActivity(intent);
                                    }
                                } else {
                                    Intent intent = new Intent(BusRegisterEvent.this, ErrorActivity.class);
                                    intent.putExtra("head", "Bus Details Registered");
                                    intent.putExtra("description", "Please Check your bus number");
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
        } else {
            Toast.makeText(BusRegisterEvent.this, "Enter all fields...", Toast.LENGTH_SHORT).show();
        }
    }
}
