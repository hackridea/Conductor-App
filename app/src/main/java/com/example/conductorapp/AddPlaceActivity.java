package com.example.conductorapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.conductorapp.models.Point;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.HashMap;
import java.util.Map;


public class AddPlaceActivity extends FragmentActivity {
    Place selectedPlace;

    EditText cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        cost=findViewById(R.id.costField);

        PlaceAutocompleteFragment placefrag = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.list);
        placefrag.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Toast.makeText(AddPlaceActivity.this, place.getName(), Toast.LENGTH_SHORT).show();
                selectedPlace = place;
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(AddPlaceActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void add(View v) {
        if (selectedPlace != null) {
            try {
                String name = selectedPlace.getName().toString().replaceAll("[.()_]","");
                double lat = selectedPlace.getLatLng().latitude, lng = selectedPlace.getLatLng().longitude;

                Point p=new Point(name,lat,lng,cost.getText().toString());
                AddRouteActivity.pointList.add(p);
                AddRouteActivity.adapter.notifyDataSetChanged();
                finish();
                Toast.makeText(this, "Successfully added place " + name, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
