package com.example.conductorapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.example.conductorapp.utils.Route;
import com.example.conductorapp.utils.RouteListAdapter;
import com.example.conductorapp.utils.StringCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HomeActivity extends AppCompatActivity {
    TextView text;
    ListView routeListView;
    ArrayList<Route> routeList;
    RouteListAdapter routeListAdapter;
    String token;
    Button button;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routeList=new ArrayList<Route>();
        if (!loggedIn()) {
            Intent loginintent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(loginintent);
        } else {
            routeListAdapter = new RouteListAdapter(routeList,HomeActivity.this);

            setContentView(R.layout.activity_home);
            text = findViewById(R.id.text);
            button=findViewById(R.id.button);
            updateList();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gotoRoute=new Intent(HomeActivity.this,AddRouteActivity.class);
                    startActivity(gotoRoute);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     getMenuInflater().inflate(R.menu.settingmenu,menu);
        return true;
    }

    private void updateList() {
        OkHttpClient client = new OkHttpClient();
        routeListView = findViewById(R.id.routelist);
        routeListView.setAdapter(routeListAdapter);


        final String content;
        StringCreator helper = new StringCreator();
        String busId=getSharedPreferences("USER", Context.MODE_PRIVATE).getString("NUM","");
        content = helper.getPOST_PARAMS();
        Request request = new Request.Builder().url("http://192.168.137.1:3001/user/getbus/"+busId).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject res = new JSONObject(response.body().string());
                    System.out.println(res.toString());
                    if (response.isSuccessful()) {
                        String busName=res.getString("name");
                        JSONArray arr=res.getJSONArray("routes");
                        for(int i=0;i<arr.length();i++){
                            JSONObject temp=arr.getJSONObject(i);
                            JSONArray arrtemp = arr.getJSONObject(i).getJSONArray("locations");
                            JSONObject first = arrtemp.getJSONObject(0);
                            JSONObject last = arrtemp.getJSONObject(arrtemp.length()-1);
                            routeList.add(new Route(  temp.getString("id"),first.getString("name"),last.getString("name" ) ,temp.getString("routeid")));
                        }
                       HomeActivity.this.runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               routeListAdapter.notifyDataSetChanged();
                           }
                       });
                    } else {
                        //TODO:connection problem
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });


    }

    private boolean loggedIn() {
        SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
        if (!sp.contains("TOKEN")) {
            return false;
        } else {
            token = sp.getString("TOKEN", "");
            return true;
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case R.id.setting_edit:
                Intent settignHome=new Intent(HomeActivity.this,SettingsActivity.class);
                startActivity(settignHome);
               return true;

           case R.id.settins_logout:
                SharedPreferences sp=getSharedPreferences("USER",Context.MODE_PRIVATE);
                sp.edit().clear().commit();
               finish();

               return true;

           default:return false;

       }
    }
}
