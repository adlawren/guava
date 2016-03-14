package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.data.ThrowawayDataManager;
import ca.ualberta.papaya.models.Thing;

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
    private class EditItemOnClickListener implements View.OnClickListener {

        private Context context;

        private Thing thing;

        private EditText itemNameEditText, descriptionEditText;

        public EditItemOnClickListener(Context initialContext, Thing initialThing,
                                       EditText initialItemNameEditText,
                                       EditText initialDescriptionEditText) {
            context = initialContext;

            thing = initialThing;

            itemNameEditText = initialItemNameEditText;
            descriptionEditText = initialDescriptionEditText;
        }

        @Override
        public void onClick(View view) {
            thing.setTitle(itemNameEditText.getText().toString());
            thing.setDescription(descriptionEditText.getText().toString());

            ArrayList<Thing> things = ThrowawayDataManager.getInstance()
                    .getCurrentUserThings(context);

            Thing match = null;
            for (Thing nextThing : things) {
                if (nextThing.getId().equals(thing.getId())) {
                    match = nextThing;
                }
            }

            if (match != null) {
                things.set(things.indexOf(match), thing);
            } else {
                System.err.println("[ThingDetailController.DeleteItemOnClickListener] ERROR: Thing not found.");
            }

            ThrowawayDataManager.getInstance().setCurrentUserThings(context, things);

            transitionToActivity(context, ThingListActivity.class);
        }
    }

    // return the onClickListener for edit save
    public EditItemOnClickListener getEditItemOnClickListener(Context initialContext,
                                                              Thing initialThing,
                                                              EditText initialItemNameEditText,
                                                              EditText initialDescriptionEditText) {
        return new EditItemOnClickListener(initialContext, initialThing, initialItemNameEditText,
                initialDescriptionEditText);
    }

    // Button to change a Thing back to available once it is no being borrowed anymore
    private class AvailableOnClickListener implements View.OnClickListener {

        private Context context;

        private Thing thing;

        public AvailableOnClickListener(Context initialContext, Thing initialThing) {
            context = initialContext;
            thing = initialThing;
        }

        @Override
        public void onClick(View view) {

            // TODO: Implement
            System.err.println("TODO: Implement AvailableOnClickListener");

            // ...
        }
    }

    // return the onClickListener for available
    public AvailableOnClickListener getAvailableOnClickListener(Context initialContext, Thing initialThing) {
        return new AvailableOnClickListener(initialContext, initialThing);
    }
}