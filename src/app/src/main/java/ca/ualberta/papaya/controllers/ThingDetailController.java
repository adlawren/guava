package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.util.ArrayList;

import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.data.ThrowawayDataManager;
import ca.ualberta.papaya.models.Thing;

/**
 * Created by adlawren on 13/03/16.
 */
public class ThingDetailController {
    private static ThingDetailController ourInstance = new ThingDetailController();

    public static ThingDetailController getInstance() {
        return ourInstance;
    }

    private ThingDetailController() {
    }

    private void transitionToActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    private class EditItemOnClickListener implements View.OnClickListener {

        private Context context;

        public EditItemOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        public void onClick(View view) {

            // TODO: Implement
            System.err.println("TODO: Implement EditItemOnClickListener");
        }
    }

    public EditItemOnClickListener getEditItemOnClickListener(Context initialContext) {
        return new EditItemOnClickListener(initialContext);
    }

    private class DeleteItemOnClickListener implements View.OnClickListener {

        private Context context;
        private Thing thing;

        public DeleteItemOnClickListener(Context initialContext, Thing initialThing) {
            context = initialContext;
            thing = initialThing;
        }

        @Override
        public void onClick(View view) {
            ArrayList<Thing> things = ThrowawayDataManager.getInstance()
                    .getCurrentUserThingsObservable().getData();

            Thing match = null;
            for (Thing nextThing : things) {
                if (nextThing.getId().equals(thing.getId())) {
                    match = nextThing;
                }
            }

            if (match != null) {
                things.remove(things.indexOf(match));
            } else {
                System.err.println("[ThingDetailController.DeleteItemOnClickListener] ERROR: Thing not found.");
            }

            transitionToActivity(context, ThingListActivity.class);
        }
    }

    public DeleteItemOnClickListener getDeleteItemOnClickListener(Context initialContext,
                                                                  Thing initialThing) {
        return new DeleteItemOnClickListener(initialContext, initialThing);
    }
}
