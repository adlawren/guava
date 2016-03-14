package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.models.tempThings;

/**
 * Created by adlawren on 10/03/16.
 *
 * Main controller for creating Thing objects. extract input data and creates a
 * thing object.
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
    private class SaveOnClickListener implements View.OnClickListener {

        private Context context;
        private EditText itemNameEditText, descriptionEditText;

        public SaveOnClickListener(Context initialContext, EditText initialItemNameEditText, EditText initialDescriptionEditText) {
            context = initialContext;
            itemNameEditText = initialItemNameEditText;
            descriptionEditText = initialDescriptionEditText;
        }

        private void transitionToActivity(Context context, Class activityClass) {
            Intent intent = new Intent(context, activityClass);
            context.startActivity(intent);
        }

        @Override
        public void onClick(View view) {
            String itemName = itemNameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            //TODO: Set up with ElasticSearch
            User user = new User();
            user.setFirstName("Emily");
            user.setLastName("Jones");
            user.setEmail("ejones@ualberta.ca");
            Thing newThing = new Thing(user);
            newThing.setTitle(itemName);
            newThing.setDescription(description);
            tempThings.getInstance().addThings(newThing);
            System.err.println("added 1");
            System.err.println(tempThings.getInstance().getThings().size());
            //System.err.println("TODO: IMPLEMENT");

            transitionToActivity(context, ThingListActivity.class);

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


        }
    }

    public SaveOnClickListener getSaveOnClickListener(Context initialContext, EditText initialItemNameEditText, EditText initialDescriptionEditText) {
        return new SaveOnClickListener(initialContext, initialItemNameEditText, initialDescriptionEditText);
    }
}
