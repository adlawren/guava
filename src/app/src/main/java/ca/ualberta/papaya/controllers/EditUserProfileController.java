package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

import ca.ualberta.papaya.AddPictureActivity;
import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.data.ThrowawayDataManager;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.LocalUser;
import ca.ualberta.papaya.util.Observer;

/**
 * Created by adlawren on 07/03/16.
 *
 * Main controller for Editing a user profile. It is a singleton that contains the instance and
 * methods for the EditUserProfileActivity
 *
 * @see ca.ualberta.papaya.EditUserProfileActivity
 */
public class EditUserProfileController {
    private static EditUserProfileController ourInstance = new EditUserProfileController();

    public static EditUserProfileController getInstance() {
        return ourInstance;
    }

    private EditUserProfileController() {}

    private void transitionToActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // button to save the changes to the profile
    private class SaveOnClickListener implements MenuItem.OnMenuItemClickListener {

        private Context context;
        private HashMap<String, EditText> editTexts;

        public SaveOnClickListener(Context initialContext, HashMap<String, EditText> editTexts) {
            context = initialContext;
            this.editTexts = editTexts;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item)  {

            LocalUser.getUser(new Observer<User>() {
                @Override
                public void update(User user) {
                    user.setFirstName(editTexts.get("firstName").getText().toString());
                    user.setLastName(editTexts.get("lastName").getText().toString());
                    user.setEmail(editTexts.get("email").getText().toString());
                    user.setPhone(editTexts.get("phone").getText().toString());
                    user.setAddress1(editTexts.get("address1").getText().toString());
                    user.setAddress2(editTexts.get("address2").getText().toString());
                    user.setProvince(editTexts.get("province").getText().toString());
                    user.setCountry(editTexts.get("country").getText().toString());
                    user.setPostal(editTexts.get("postal").getText().toString());

                    transitionToActivity(context, ThingListActivity.class);
                }
            });



            return true;
        }
    }

    //return the onClickListener for save button
    public SaveOnClickListener getSaveOnClickListener(Context initialContext,
                                                      HashMap<String, EditText> editTexts) {
        return new SaveOnClickListener(initialContext, editTexts);
    }

    // button to cancel the edit
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

    // return the onClickListener for cancel
    public CancelOnClickListener getCancelOnClickListener(Context initialContext) {
        return new CancelOnClickListener(initialContext);
    }


}
