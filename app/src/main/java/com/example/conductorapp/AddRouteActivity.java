package com.example.conductorapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.example.conductorapp.adapter.PointListAdapter;
import com.example.conductorapp.models.Point;
import com.example.conductorapp.utils.Route;
import com.example.conductorapp.utils.RouteListAdapter;
import com.example.conductorapp.utils.StringCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddRouteActivity extends AppCompatActivity {

    public static ArrayList<Point> pointList;

    Button doneBtn;
    FloatingActionButton btn;
    EditText from,to;
    ListView listView;

    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;

    int mHour;
    int mMinute;



    public static PointListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);

        listView=findViewById(R.id.pathListView);
        pointList=new ArrayList<Point>();

        adapter = new PointListAdapter(AddRouteActivity.this,pointList);
        listView.setAdapter(adapter);

        btn=findViewById(R.id.addPathBtn);
        from=findViewById(R.id.routeFromTime);
        to=findViewById(R.id.routeToTime);

        doneBtn=findViewById(R.id.doneBtn);

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(from);
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(to);
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent addpoint=new Intent(AddRouteActivity.this,AddPlaceActivity.class);
                startActivity(addpoint);


            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fromDate=from.getText().toString();
                String toDate=to.getText().toString();

                String isoDatePattern = "yyyy-MM-dd HH:mm:ss";
                String formatPattern = "dd-MM-yyyy HH:mm";

                SimpleDateFormat parser = new SimpleDateFormat(formatPattern);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(isoDatePattern);
                try {
                Date start=parser.parse(fromDate);
                Date end=parser.parse(toDate);

                fromDate = simpleDateFormat.format(start);
                toDate = simpleDateFormat.format(end);



                    updateList(fromDate,toDate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (ParseException e){
                    e.printStackTrace();
                }


            }
        });


    }

    private void updateList(String fromTime,String toTime) throws JSONException {
        OkHttpClient client = new OkHttpClient();
        final String content;
        JSONArray arr=new JSONArray();
        StringCreator helper = new StringCreator();
        helper.addParam("id", getSharedPreferences("USER", Context.MODE_PRIVATE).getString("NUM",""));
        helper.addParam("init_time",fromTime);
        helper.addParam("end_time",toTime);

        String temp="";

        for(Point p:pointList){
            StringCreator pathCreator=new StringCreator();
            pathCreator.addParam("name",p.getName());
            pathCreator.addParam("lat",p.getLat()+"");
            pathCreator.addParam("lon",p.getLon()+"");
            pathCreator.addParam("cost",p.getCost());
            temp+= ( pathCreator.getPOST_PARAMS() + "," );
        }
        temp=temp.substring(0,temp.length()-1);


        helper.POST_PARAMS += ",\"paths\":["+temp+"]";

        String tempString=new JSONObject(helper.getPOST_PARAMS()).toString();


        content = tempString;
        System.out.println("Text:"+helper.POST_PARAMS);

        Request request = new Request.Builder().url("http://192.168.137.1:3001/bus/addpath/").post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), content)).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject res = new JSONObject(response.body().string());
                    JSONArray arr = res.names();
                    if (response.isSuccessful()) {
                        if(!res.getString("success").equals("true"))
                        {
                            Intent intent = new Intent(AddRouteActivity.this, ErrorActivity.class);
                            intent.putExtra("head", "Error");
                            intent.putExtra("description", "Failed to add route.");
                            startActivity(intent);
                        }
                        else{
                            //TODO:connection problem
                            Intent intent = new Intent(AddRouteActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(AddRouteActivity.this, ErrorActivity.class);
                        intent.putExtra("head", "Error");
                        intent.putExtra("description", "Failed to add route.");
                        startActivity(intent);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void datePicker(final EditText button){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        //*************Call Time Picker Here ********************
                        timePicker(button);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timePicker(final EditText b){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;

                        b.setText(date_time+" "+hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


}
