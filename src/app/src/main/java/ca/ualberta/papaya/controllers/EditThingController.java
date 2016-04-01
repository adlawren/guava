package ca.ualberta.papaya.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import ca.ualberta.papaya.AddPictureActivity;
import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.data.MyThingsDataManager;
import ca.ualberta.papaya.models.Photo;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;

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


        private EditText itemNameEditText, descriptionEditText;
        private ImageView imageView;

        public EditItemOnClickListener(Context initialContext,
                                       Thing initialThing,
                                       EditText initialItemNameEditText,
                                       EditText initialDescriptionEditText,
                                       ImageButton initialImageView ) {
            context = initialContext;

            thing = initialThing;

            itemNameEditText = initialItemNameEditText;
            descriptionEditText = initialDescriptionEditText;
            imageView = initialImageView;

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




            thing.publish();

            transitionToActivity(context, ThingListActivity.class);

            MyThingsDataManager.getInstance().update(thing);

            return true;
        }
    }

    // return the onClickListener for edit save
    public EditItemOnClickListener getEditItemOnClickListener(Context initialContext,
                                                              Thing initialThing,
                                                              EditText initialItemNameEditText,
                                                              EditText initialDescriptionEditText,
                                                              ImageButton initialImageView) {
        return new EditItemOnClickListener(initialContext, initialThing, initialItemNameEditText,
                initialDescriptionEditText, initialImageView);
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

            // ...

            return true;
        }
    }

    // return the onClickListener for available
    public AvailableOnClickListener getAvailableOnClickListener(Context initialContext, Thing initialThing) {
        return new AvailableOnClickListener(initialContext, initialThing);
    }


}
