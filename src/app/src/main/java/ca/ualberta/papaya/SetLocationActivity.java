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
 *
 * Main activity for setting a pickup location to a thing.
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
        Bundle bundle = intent.getBundleExtra("bundle");

        location = bundle.getParcelable(LATLNG_EXTRA);
        //location = new LatLng(13,12);


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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveLocation:
                // EITHER CALL THE METHOD HERE OR DO THE FUNCTION DIRECTLY
                save();
                return true;

            case R.id.deleteLocation:
                delete();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//
//        menu.findItem(R.id.saveLocation)
//                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        save();
//                        return true;
//                    }
//                });
//        deleteLoc = menu.findItem(R.id.deleteLocation);
//        menu.findItem(R.id.deleteLocation)
//                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                delete();
//                return true;
//            }
//        });
//
//
//
//        return true;
//    }



    //save location to object
    private void save(){
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SetLocationActivity.LATLNG_EXTRA, location);
        returnIntent.putExtra("bundle",bundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }

    //delete location
    private void delete(){

        location = null;


        updateView();
    }

    private void updateView(){
        if(location != null) {
            mMap.clear();
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 15);
            mMap.moveCamera(update);
            mMap.addMarker(new MarkerOptions().position(location));
        } else{
            mMap.clear();
        }
    }

}
