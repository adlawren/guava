package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.data.ThrowawayDataManager;
import ca.ualberta.papaya.models.User;

/**
 * Created by adlawren on 07/03/16.
 */
public class EditUserProfileController {
    private static EditUserProfileController ourInstance = new EditUserProfileController();

    public static EditUserProfileController getInstance() {
        return ourInstance;
    }

    private EditUserProfileController() {}

    private void transitionToActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    private class SaveOnClickListener implements View.OnClickListener {

        private Context context;
        private EditText emailEditText;

        public SaveOnClickListener(Context initialContext,
                                   EditText initialEmailEditText) {
            context = initialContext;
            emailEditText = initialEmailEditText;
        }

        @Override
        public void onClick(View view) {
            String email = emailEditText.getText().toString();

            User user = ThrowawayDataManager.getInstance().getCurrentUser(context);
            user.setEmail(email);

            ThrowawayDataManager.getInstance().setCurrentUser(context, user);

            transitionToActivity(context, ThingListActivity.class);
        }
    }

    public SaveOnClickListener getSaveOnClickListener(Context initialContext,
                                                      EditText initialEmailEditText) {
        return new SaveOnClickListener(initialContext,
                initialEmailEditText);
    }

    private class CancelOnClickListener implements View.OnClickListener {

        private Context context;

        public CancelOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        public void onClick(View view) {
            transitionToActivity(context, ThingListActivity.class);
        }
    }

    public CancelOnClickListener getCancelOnClickListener(Context initialContext) {
        return new CancelOnClickListener(initialContext);
    }
}
