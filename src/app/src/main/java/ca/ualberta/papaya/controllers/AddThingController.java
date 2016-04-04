package ca.ualberta.papaya.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import ca.ualberta.papaya.AddPictureActivity;
import ca.ualberta.papaya.SetLocationActivity;
import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.data.MyThingsDataManager;
import ca.ualberta.papaya.interfaces.IObserver;
import ca.ualberta.papaya.models.Photo;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.LocalUser;
import ca.ualberta.papaya.util.Observable;
import ca.ualberta.papaya.util.Observer;

/**
 * Created by adlawren on 10/03/16.
 *
 * Main controller for creating Thing objects. It is a singleton that contains the instance and
 * methods for the AddThingActivity
 *
 * @see ca.ualberta.papaya.AddThingActivity
 *
 */
public class AddThingController {
    private static AddThingController ourInstance = new AddThingController();
    public static final int PHOTO_RESULT = 10;

    public static AddThingController getInstance() {
        return ourInstance;
    }

    private AddThingController() {
    }

    // Save Button
    private class SaveOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;
        private EditText itemNameEditText, descriptionEditText;
        private ImageView imageView;
        private LatLng location;

        public SaveOnClickListener(Context initialContext, EditText initialItemNameEditText,
                                   EditText initialDescriptionEditText, ImageView initialImageView,
                                    LatLng initialLocation) {
            context = initialContext;
            itemNameEditText = initialItemNameEditText;
            descriptionEditText = initialDescriptionEditText;
            imageView = initialImageView;
            location = initialLocation;

        }

        private void transitionToActivity(Context context, Class activityClass) {
            Intent intent = new Intent(context, activityClass);
            context.startActivity(intent);
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            final String itemName = itemNameEditText.getText().toString();
            final String description = descriptionEditText.getText().toString();

<<<<<<< HEAD
            LocalUser.getUser(new Observer<User>() {
                @Override
                public void update(User user) {

                    Thing thing = new Thing(user);
                    thing.setTitle(itemName);
                    thing.setDescription(description);

                    Photo photo = new Photo();
                    //photo.setImage(image);
                    thing.setPhoto(photo);
                    thing.setLocation(location);

                    thing.publish();

                    transitionToActivity(context, ThingListActivity.class);
                }
            });

            return true;
=======
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
            final Bitmap image = (bitmapDrawable == null) ? null : bitmapDrawable.getBitmap();

//            LocalUser.getUser(new Observer() {
//                @Override
//                public void update(Object data) {
//                    Thing thing = new Thing((User) data);
//                    thing.setTitle(itemName);
//                    thing.setDescription(description);
//
//                    Observable<Thing> observable = new Observable<>();
//                    observable.setData(thing);
//                    observable.addObserver(new IObserver<Thing>() {
//                        @Override
//                        public void update(Thing data) {
//                            transitionToActivity(context, ThingListActivity.class);
//                        }
//                    });
//
//                    MyThingsDataManager.getInstance().update(observable);
//                }
//            });

	    User user = new User();
        user.setId(LocalUser.getId());

        Thing thing = new Thing(user);
        thing.setTitle(itemName);
        thing.setDescription(description);

        Photo photo = new Photo();
        photo.setImage(image);
        thing.setPhoto(photo);


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
>>>>>>> 306940ff05a2c79ed1aebef2602165fc28c144f0
        }
    }

    // return the onClickListener
    public SaveOnClickListener getSaveOnClickListener(Context initialContext,
                                                      EditText initialItemNameEditText,
                                                      EditText initialDescriptionEditText,
                                                      ImageView imageView , LatLng initialLocation) {
        return new SaveOnClickListener(initialContext, initialItemNameEditText,
                initialDescriptionEditText, imageView, initialLocation);
    }

    private class SetPictureOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;

        private Bitmap image;

        public SetPictureOnClickListener(Context initialContext, Bitmap initialImage) {
            context = initialContext;
            image = initialImage;

        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            Intent intent = new Intent(context, AddPictureActivity.class);
            intent.putExtra(AddPictureActivity.PICTURE_EXTRA, image);

            ((Activity)context).startActivityForResult(intent, PHOTO_RESULT);

            return true;
        }
    }

    // return the onClickListener for setPicture
    public SetPictureOnClickListener getSetPictureOnClickListener(Context initialContext, Bitmap initialImage) {
        return new SetPictureOnClickListener(initialContext, initialImage);
    }

    // Button to change a Thing back to available once it is no being borrowed anymore
    private class SetLocationOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;

        private LatLng location;

        public SetLocationOnClickListener(Context initialContext, LatLng initialLocation) {
            context = initialContext;
            location = initialLocation;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            Intent intent = new Intent(context, SetLocationActivity.class);
            intent.putExtra(SetLocationActivity.LATLNG_EXTRA, location);

            context.startActivity(intent);

            return true;
        }
    }

    // return the onClickListener for available
    public SetLocationOnClickListener getSetLocationOnClickListener(Context initialContext, LatLng initialLocation) {
        return new SetLocationOnClickListener(initialContext, initialLocation);
    }
}
