package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import ca.ualberta.papaya.ThingListActivity;

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

            // TODO: Implement
            System.err.println("TODO: IMPLEMENT");

            transitionToActivity(context, ThingListActivity.class);
        }
    }

    public SaveOnClickListener getSaveOnClickListener(Context initialContext, EditText initialItemNameEditText, EditText initialDescriptionEditText) {
        return new SaveOnClickListener(initialContext, initialItemNameEditText, initialDescriptionEditText);
    }
}
