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
                    .getCurrentUserThingsObservable().getData();

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

            ThrowawayDataManager.getInstance().getCurrentUserThingsObservable().setData(things);

            transitionToActivity(context, ThingListActivity.class);
        }
    }

    public EditItemOnClickListener getEditItemOnClickListener(Context initialContext,
                                                              Thing initialThing,
                                                              EditText initialItemNameEditText,
                                                              EditText initialDescriptionEditText) {
        return new EditItemOnClickListener(initialContext, initialThing, initialItemNameEditText,
                initialDescriptionEditText);
    }

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

    public AvailableOnClickListener getAvailableOnClickListener(Context initialContext, Thing initialThing) {
        return new AvailableOnClickListener(initialContext, initialThing);
    }
}
