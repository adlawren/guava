package ca.ualberta.papaya.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import ca.ualberta.papaya.AddPictureActivity;
import ca.ualberta.papaya.DisplayLocationActivity;
import ca.ualberta.papaya.SetLocationActivity;
import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.ViewPictureActivity;
import ca.ualberta.papaya.data.MyThingsDataManager;
import ca.ualberta.papaya.interfaces.IObserver;
import ca.ualberta.papaya.models.Photo;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.Observable;
import ca.ualberta.papaya.util.Observer;

/**
 * Created by adlawren on 13/03/16.
 *
 * Main controller for Editing Thing objects. It is a singleton that contains the instance and
 * methods for the EditThingActivity
 *
 * @see ca.ualberta.papaya.EditThingActivity
 *
 */
public class EditThingController {
    private static EditThingController ourInstance = new EditThingController();
    public static final int PHOTO_RESULT = 10;

    public static EditThingController getInstance() {
        return ourInstance;
    }

    private EditThingController() {
    }


    private void transitionToActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    // button to save the changes to the Thing
    private class EditItemOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;

        private Thing thing;

        private LatLng location;


        // private Bitmap image;
        private ImageView imageView;


        private EditText itemNameEditText, descriptionEditText;

        public EditItemOnClickListener(Context initialContext,
                                       Thing initialThing,
                                       EditText initialItemNameEditText,
                                       EditText initialDescriptionEditText,
                                       ImageButton initialImageView,
                                       LatLng initialLocation) {
            context = initialContext;

            thing = initialThing;
            location = initialLocation;


            context = initialContext;

            thing = initialThing;
            // image = initialImage;
            imageView = initialImageView;

            itemNameEditText = initialItemNameEditText;
            descriptionEditText = initialDescriptionEditText;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            thing.setTitle(itemNameEditText.getText().toString());
            thing.setDescription(descriptionEditText.getText().toString());
            Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            Photo photo = new Photo();
            photo.setImage(image);
            thing.setPhoto(photo);
            thing.setLocation(location);

            Observable<Thing> thingObservable = new Observable<>();
            thingObservable.setData(thing);
            thingObservable.addObserver(new IObserver<Thing>() {
                @Override
                public void update(Thing data) {
                    transitionToActivity(context, ThingListActivity.class);
                }
            });

            MyThingsDataManager.getInstance().update(thingObservable);

            return true;
        }
    }

    // return the onClickListener for edit save
    public EditItemOnClickListener getEditItemOnClickListener(Context initialContext,
                                                              Thing initialThing,
                                                              EditText initialItemNameEditText,
                                                              EditText initialDescriptionEditText,
                                                              ImageButton initialImageView, LatLng initialLocation) {
        return new EditItemOnClickListener(initialContext, initialThing, initialItemNameEditText,
                initialDescriptionEditText, initialImageView, initialLocation);

    }

    // Button to change a Thing back to available once it is no being borrowed anymore
    private class AvailableOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;

        private Thing thing;

        public AvailableOnClickListener(Context initialContext, Thing initialThing) {
            context = initialContext;
            thing = initialThing;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {

            // TODO: Implement
            System.err.println("TODO: Implement AvailableOnClickListener");

            System.err.println("[EditThingController] Thing uuid: " + thing.getUuid());

            thing.setStatus(Thing.Status.AVAILABLE);
            thing.setBorrowerId(null);

            Observable<Thing> observable = new Observable<>();
            observable.setData(thing);
            observable.addObserver(new IObserver<Thing>() {
                @Override
                public void update(Thing data) {
                    transitionToActivity(context, ThingListActivity.class);
                }
            });

            MyThingsDataManager.getInstance().update(observable);

            return true;
        }
    }

    // return the onClickListener for available
    public AvailableOnClickListener getAvailableOnClickListener(Context initialContext, Thing initialThing) {
        return new AvailableOnClickListener(initialContext, initialThing);
    }


    private class SetPictureOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;
        private Thing thing;

        public SetPictureOnClickListener(Context initialContext, Thing initialThing) {
            context = initialContext;
            thing = initialThing;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {

            Intent intent = new Intent(context, AddPictureActivity.class);
            intent.putExtra(AddPictureActivity.PICTURE_EXTRA, thing.getPhoto().getImage());

            ((Activity) context).startActivityForResult(intent, PHOTO_RESULT);


            return true;
        }

        // return the onClickListener for setPicture
        public SetPictureOnClickListener getSetPictureOnClickListener(Context initialContext, Thing initialThing) {
            return new SetPictureOnClickListener(initialContext, initialThing);
        }

    }


    // return the onClickListener for available
    public SetLocationOnClickListener getSetLocationOnClickListener(Context initialContext, Thing initialThing) {
        return new SetLocationOnClickListener(initialContext, initialThing);
    }




    private class SetLocationOnClickListener implements MenuItem.OnMenuItemClickListener {
        private Context context;
        private Thing thing;


        public SetLocationOnClickListener(Context initialContext, Thing initialThing) {
            context = initialContext;
            thing = initialThing;
        }


        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {

            Intent intent = new Intent(context, SetLocationActivity.class);
            LatLng location = thing.getLocation();
            Bundle bundle = new Bundle();
            bundle.putParcelable(SetLocationActivity.LATLNG_EXTRA, location);
            intent.putExtras(bundle);


            context.startActivity(intent);
            return true;
        }
    }

//    private class SubOnClickListener implements View.OnClickListener { // implements View.OnClickListener {
//
//        private Context context;
//
//        private Thing thing;
//
//        public SubOnClickListener() {
//        }
//
//        @Override
//        // public void onClick(View view) {
//        public boolean onMenuItemClick(MenuItem item) {
//
//
//
//            return true;
//        }
//    }
//
//    // return the onClickListener for available
//    public SubOnClickListener getSubOnClickListener() {
//        return new SubOnClickListener();
//    }
}