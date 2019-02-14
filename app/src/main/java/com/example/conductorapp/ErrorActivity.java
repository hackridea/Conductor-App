package com.example.conductorapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ErrorActivity extends AppCompatActivity{
    TextView head,des;
    FloatingActionButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        head=findViewById(R.id.errorhead);
        des=findViewById(R.id.recover);
        Intent intt=getIntent();
        head.setText(intt.getStringExtra("head"));
        des.setText(intt.getStringExtra("description"));
        back=findViewById(R.id.errorbackbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
