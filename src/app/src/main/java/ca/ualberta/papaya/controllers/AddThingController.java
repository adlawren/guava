package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.data.ThrowawayDataManager;
import ca.ualberta.papaya.models.User;

/**
 * Created by adlawren on 10/03/16.
 */
public class AddThingController {
    private static AddThingController ourInstance = new AddThingController();

    public static AddThingController getInstance() {
        return ourInstance;
    }

    private AddThingController() {
    }

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

            //System.err.println("TODO: IMPLEMENT");

            //TODO: Set up with ElasticSearch
            User user = new User();
            user.setFirstName("Emily");
            user.setLastName("Jones");
            user.setEmail("ejones@ualberta.ca");
            Thing newThing = new Thing(user);
            newThing.setTitle(itemName);
            newThing.setDescription(description);
            ThrowawayDataManager.getInstance().addThings(newThing);
            System.err.println("added 1");
            System.err.println(ThrowawayDataManager.getInstance().getThings().size());

            transitionToActivity(context, ThingListActivity.class);
        }
    }

    public SaveOnClickListener getSaveOnClickListener(Context initialContext, EditText initialItemNameEditText, EditText initialDescriptionEditText) {
        return new SaveOnClickListener(initialContext, initialItemNameEditText, initialDescriptionEditText);
    }
}
