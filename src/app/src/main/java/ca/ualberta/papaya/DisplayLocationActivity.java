package ca.ualberta.papaya;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by bgodley on 02/04/16.
 */
public class DisplayLocationActivity extends AbstractPapayaActivity {
    //some code taken from https://www.youtube.com/watch?v=5UsaP8JmTRg

    //private MapView mMapView;
    private GoogleMap mMap;
    private LatLng location;
    public final static String LATLNG_EXTRA = "ca.papaya.ualberta.edit.Location";

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_location);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }



        Intent intent = getIntent();
        location = intent.getParcelableExtra(LATLNG_EXTRA);
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();


        try {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 15);
            mMap.moveCamera(update);
            mMap.addMarker(new MarkerOptions().position(location));
        }catch (Exception e){
            //no location
            finish();
        }



    }


}
