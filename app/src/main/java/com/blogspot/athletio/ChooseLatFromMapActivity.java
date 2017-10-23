package com.blogspot.athletio;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class ChooseLatFromMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mgoogleMap;
    LatLng latLng=new LatLng(0.0,0.0);

    Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_lat_from_map);

        setupUI();

        if(gservicesavailable())
            initMap();



    }

    private void setupUI() {
        submitButton =(Button)findViewById(R.id.choose_lat_from_map_layout_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("lat",latLng.latitude);
        data.putExtra("lng",latLng.longitude);
        setResult(RESULT_OK, data);
        super.finish();
    }



    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.choose_lat_from_map_layout_map);
        mapFragment.getMapAsync(this);

    }

    public boolean gservicesavailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isavailable = api.isGooglePlayServicesAvailable(this);
        if (isavailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isavailable)) {
            Dialog dialog = api.getErrorDialog(this, isavailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Could not connect to Play Services", Toast.LENGTH_LONG).show();
        }
        return false;

    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(ChooseLatFromMapActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            return add;
        } catch (Exception e) {

        }
        return null;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap=googleMap;
        mgoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            public void onMapClick(LatLng point){
                latLng=point;
                mgoogleMap.clear();
                MarkerOptions markerStart=new MarkerOptions().position(point);
                mgoogleMap.addMarker(markerStart);
                Log.d("add",getAddress(point.latitude,point.longitude));
            }
        });
    }

}
