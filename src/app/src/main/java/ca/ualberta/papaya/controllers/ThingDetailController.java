package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import ca.ualberta.papaya.AddPictureActivity;
import ca.ualberta.papaya.BidProfileActivity;
import ca.ualberta.papaya.DisplayLocationActivity;
import ca.ualberta.papaya.EditThingActivity;
import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.ViewPictureActivity;
import ca.ualberta.papaya.data.MyThingsDataManager;
import ca.ualberta.papaya.data.ThrowawayDataManager;
import ca.ualberta.papaya.interfaces.IObserver;
import ca.ualberta.papaya.models.Bid;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.util.Observable;
import ca.ualberta.papaya.util.Observer;

/**
 * Created by adlawren on 13/03/16.
 *
 * Main controller for displaying a single item. It is a singleton that contains the instance and
 * methods for the ThingDetailActivity
 *
 * @see ca.ualberta.papaya.ThingDetailActivity
 */
public class ThingDetailController {
    private static ThingDetailController ourInstance = new ThingDetailController();

    public static ThingDetailController getInstance() {
        return ourInstance;
    }

    private ThingDetailController() {
    }

    //Change to the activity
    private void transitionToActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    // button for editing the item
    private class EditItemOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;

        private Thing thing;

        public EditItemOnClickListener(Context initialContext, Thing initialThing) {
            context = initialContext;
            thing = initialThing;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            Intent intent = new Intent(context, EditThingActivity.class);
            intent.putExtra(EditThingActivity.THING_EXTRA, thing);

            context.startActivity(intent);

            return true;
        }
    }

    // return the onClickListener for edit
    public EditItemOnClickListener getEditItemOnClickListener(Context initialContext, Thing initialThing) {
        return new EditItemOnClickListener(initialContext, initialThing);
    }

    // Button for Deleting the item
    private class DeleteItemOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;
        private Thing thing;

        public DeleteItemOnClickListener(Context initialContext, Thing initialThing) {
            context = initialContext;
            thing = initialThing;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            Observable<Thing> thingObservable = new Observable<>();
            thingObservable.setData(thing);
            thingObservable.addObserver(new IObserver<Thing>() {
                @Override
                public void update(Thing data) {
                    transitionToActivity(context, ThingListActivity.class);
                }
            });

            MyThingsDataManager.getInstance().delete(thingObservable);

            return true;
        }
    }

    // return the onClickListener for delete
    public DeleteItemOnClickListener getDeleteItemOnClickListener(Context initialContext,
                                                                  Thing initialThing) {
        return new DeleteItemOnClickListener(initialContext, initialThing);
    }

    // The onClickMenuItem for viewing the picture
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

    // return the onMenuItemListener for viewing the thing's picture
    public ImageOnClickListener getImageOnClickListener(Context thisContext, Thing theThing) {
        return new ImageOnClickListener(thisContext, theThing);
    }






    // The onClickMenuItem for viewing the picture
    private class BidOnClickListener implements View.OnClickListener {

        private Thing thing;
        private Bid bid;

        public BidOnClickListener(Thing theThing, Bid theBid) {
            thing = theThing;
            bid = theBid;

        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, BidProfileActivity.class);
            intent.putExtra(BidProfileActivity.THING_EXTRA, thing);
            intent.putExtra(BidProfileActivity.BID_EXTRA, bid);

            context.startActivity(intent);
        }
    }

    // return the onMenuItemListener for user bids
    public BidOnClickListener getBidOnClickListener(Thing theThing, Bid theBid) {
        return new BidOnClickListener( theThing, theBid);
    }


/*
    //filters
    //switch to all filter
    private class AllFilterOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;

        private Thing thing;

        public AllFilterOnClickListener(Context initialContext, Thing initialThing) {
            context = initialContext;
            thing = initialThing;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            Intent intent = new Intent(context, ThingListActivity.class);
            intent.putExtra(ViewPictureActivity.PICTURE_EXTRA, thing.getPhoto().getImage());

            context.startActivity(intent);

            return true;
        }
    }

    */

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
            Bundle bundle = new Bundle();

            bundle.putParcelable(DisplayLocationActivity.LATLNG_EXTRA, location);
            intent.putExtras(bundle);
            context.startActivity(intent);

            return true;
        }
    }

    // return the onClickListener for getPicture
    public DisplayLocationOnClickListener getDisplayLocationOnClickListener(Context initialContext, Thing initialThing) {
        return new DisplayLocationOnClickListener(initialContext, initialThing);
    }

}
