package ca.ualberta.papaya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by bgodley on 02/04/16.
 */
public class SetLocationActivity extends AbstractPapayaActivity {
    //some code taken from https://www.youtube.com/watch?v=5UsaP8JmTRg

    //private MapView mMapView;
    private GoogleMap mMap;
    private LatLng location;
    private LatLng undoLatLng;
    public final static String LATLNG_EXTRA = "ca.papaya.ualberta.edit.Location";

    private int UNDO = 0;
    private MenuItem deleteLoc;

    public static final int LOCATION_RESULT = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        Intent intent = getIntent();

        //try {
            location = intent.getParcelableExtra(LATLNG_EXTRA);
        //}catch(Exception e){
            location = null;
       // }

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                location = latLng;
                updateView();
            }
        });

        
        updateView();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_location, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.saveLocation)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        save();
                        return true;
                    }
                });
        deleteLoc = menu.findItem(R.id.deleteLocation);
        menu.findItem(R.id.deleteLocation)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                delete();
                return true;
            }
        });



        return true;
    }



    //save location to object
    private void save(){
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SetLocationActivity.LATLNG_EXTRA, location);
        returnIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK, returnIntent);
        if(location != null){
            finish();
        }
    }

    //delete location
    private void delete(){
        if (UNDO == 0) {
            //delete
            undoLatLng = location;
            location = null;

            UNDO = 1;
            deleteLoc.setTitle("Undo");
        } else {
            //redo
            location = undoLatLng;
            undoLatLng = null;
            UNDO = 0;
            deleteLoc.setTitle("Delete");
        }
        //picture = null;
        updateView();
    }

    private void updateView(){
        if(location != null) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 15);
            mMap.moveCamera(update);
            mMap.addMarker(new MarkerOptions().position(location));
        } else{
            mMap.clear();
        }
    }

}
