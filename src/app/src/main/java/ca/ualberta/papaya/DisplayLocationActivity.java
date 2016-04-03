package ca.ualberta.papaya;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

    private MapView mMapView;
    private GoogleMap mMap;
    private Location location;

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

        //getSupportFragmentManager().beginTransaction()
          //      .add(R.id.content, new MapFragment())
            //    .commit();

//
    }

    @Override
    protected void onStart(){
        super.onStart();
        //Todo

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        //Todo define toolbar buttons



        return true;
    }


    /*
    private boolean initMap(){
        if(MAP == null){
            MapFragment mapFrag = getFragmentManager().findFragmentById(R.id.map);
            MAP = mapFrag.getMap();
        }
        return (MAP != null);
    }


    private void Display(){
        if(MAP){
            CameraUpdate update = CameraUpdateFactory.
                    newLacLngZoom(location.getLatLng(), 5);
            MAP.moveCamera(update);

            mMap.addMarker(new MarkerOptions()
                .position(location.getLatLng())
                .anchor(.5f, .5f)
                    .icon(BitmapDescriptorfactory.
                            fromReasource(R.drawable.ic_starmarker)));

        }
    }

*/
    public class MapFragment extends Fragment {
        //class from http://stackoverflow.com/questions/29505087/cannot-add-map-in-android-studio-as-stated-in-google-getting-started-page-and
        MapView mMapView;
        private GoogleMap googleMap;
        Location location;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // inflate and return the layout
            View v = inflater.inflate(R.layout.content_display_location, container,
                    false);
            mMapView = (MapView) v.findViewById(R.id.mapView);
            mMapView.onCreate(savedInstanceState);

            mMapView.onResume();// needed to get the map to display immediately

            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            googleMap = mMapView.getMap();
            googleMap.setMyLocationEnabled(true);

            location = googleMap.getMyLocation();

            if (location != null) {

                // latitude and longitude
                LatLng lat = new LatLng(location.getLatitude(), location.getLongitude());

                //double latitude = 17.385044;
                //double longitude = 78.486671;
                //LatLng lat = new LatLng(latitude,longitude);


                // create marker

                MarkerOptions marker = new MarkerOptions().position(
                        lat).title("Hello Maps");

                // Changing marker icon
                marker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

                // adding marker
                googleMap.addMarker(marker);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(lat).zoom(12).build();


                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

            }

            // Perform any camera updates here
            return v;
        }



    }
}
