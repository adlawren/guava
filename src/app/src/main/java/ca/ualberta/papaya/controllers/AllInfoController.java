package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import java.math.BigDecimal;

import ca.ualberta.papaya.DisplayLocationActivity;
import ca.ualberta.papaya.ThingSearchActivity;
import ca.ualberta.papaya.ViewPictureActivity;
import ca.ualberta.papaya.exceptions.ThingUnavailableException;
import ca.ualberta.papaya.models.Bid;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.LocalUser;
import ca.ualberta.papaya.util.Observer;

/**
 * Created by adlawren on 14/03/16.
 *
 * Main controller for displaying Thing objects that are searched. It is a singleton that contains the instance and
 * methods for the ThingSearchDetailActivity
 *
 * @see ca.ualberta.papaya.ThingSearchDetailActivity
 *
 */
public class AllInfoController {
    private static AllInfoController ourInstance = new AllInfoController();

    public static AllInfoController getInstance() {
        return ourInstance;
    }

    private void transitionToActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    private AllInfoController() {
    }

    private class DisplayLocationOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;

        private Thing thing;

        public DisplayLocationOnClickListener(Context initialContext, Thing initialThing) {
            context = initialContext;
            thing = initialThing;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            Intent intent = new Intent(context, DisplayLocationActivity.class);
            LatLng location = thing.getLocation();

            intent.putExtra(DisplayLocationActivity.LATLNG_EXTRA, location);

            context.startActivity(intent);

            return true;
        }
    }

    // return the onClickListener for getPicture
    public DisplayLocationOnClickListener getDisplayLocationOnClickListener(Context initialContext, Thing initialThing) {
        return new DisplayLocationOnClickListener(initialContext, initialThing);
    }

    // The onClickMenuItem for placing a bid
    private class ImageOnClickListener implements MenuItem.OnMenuItemClickListener {

        private Context context;

        private Thing thing;

        public ImageOnClickListener(Context thisContext, Thing theThing) {
            thing = theThing;
            context = thisContext;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            Intent intent = new Intent(context, ViewPictureActivity.class);
            intent.putExtra(ViewPictureActivity.PICTURE_EXTRA, thing.getPhoto().getImage());

            context.startActivity(intent);
            return true;
        }


    }

    // return the onMenuItemListener for user bids
    public ImageOnClickListener getImageOnClickListener(Context thisContext, Thing theThing) {
        return new ImageOnClickListener(thisContext, theThing);
    }


}
