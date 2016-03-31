package ca.ualberta.papaya.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import ca.ualberta.papaya.AddPictureActivity;
import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.models.Photo;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;

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

        public SaveOnClickListener(Context initialContext, EditText initialItemNameEditText,
                                   EditText initialDescriptionEditText, ImageView initialImageView) {
            context = initialContext;
            itemNameEditText = initialItemNameEditText;
            descriptionEditText = initialDescriptionEditText;
            imageView = initialImageView;

        }

        private void transitionToActivity(Context context, Class activityClass) {
            Intent intent = new Intent(context, activityClass);
            context.startActivity(intent);
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            String itemName = itemNameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            Bitmap image = imageView.getDrawingCache();

            User testUser = new User();
            testUser.setFirstName("Testy").setLastName("McTesterface");

            Thing thing = new Thing(testUser);
            thing.setTitle(itemName);
            thing.setDescription(description);


<<<<<<< HEAD
            //if( image != null){
            Photo photo = new Photo();
            photo.setImage(image);
            thing.setPhoto(photo);
            //}

=======
            if( image != null) {
                thing.getPhoto().setImage(image);
            }
>>>>>>> 819e427a0c6c68d5a36022e66baca221c1bc7203

            // testUser.publish();

            thing.publish();

            transitionToActivity(context, ThingListActivity.class);

<<<<<<< HEAD
            /*

            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {

                        String itemName = itemNameEditText.getText().toString();
                        String description = descriptionEditText.getText().toString();

                        User user = new User();
                        user.setFirstName("Emily");
                        user.setLastName("Jones");
                        user.setEmail("ejones@ualberta.ca");

                        user.publish();

                        Thing newThing = new Thing(user);
                        newThing.setTitle(itemName);
                        newThing.setDescription(description);

                        newThing.publish();

                        transitionToActivity(context, ThingListActivity.class);
                    }
                    catch (Exception e) { e.printStackTrace(); }
                }
            }).start();

            */

=======
>>>>>>> 819e427a0c6c68d5a36022e66baca221c1bc7203
            return true;
        }
    }

    // return the onClickListener
    public SaveOnClickListener getSaveOnClickListener(Context initialContext,
                                                      EditText initialItemNameEditText,
                                                      EditText initialDescriptionEditText,
                                                      ImageView imageView ) {
        return new SaveOnClickListener(initialContext, initialItemNameEditText,
                initialDescriptionEditText, imageView);
    }


}
