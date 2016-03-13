package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import ca.ualberta.papaya.AddThingActivity;
import ca.ualberta.papaya.ThingSearchActivity;
import ca.ualberta.papaya.UserEditProfileActivity;
import ca.ualberta.papaya.models.User;

/**
 * Created by adlawren on 10/03/16.
 */
public class ThingListController {
    private static ThingListController ourInstance = new ThingListController();

    public static ThingListController getInstance() {
        return ourInstance;
    }

    private ThingListController() {
    }

    private void transitionToActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    private class OtherItemsOnClickListener implements View.OnClickListener {

        private Context context;

        public OtherItemsOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        public void onClick(View view) {
            System.err.println("TODO: IMPLEMENT");

            // TODO: Implement
            //Intent intent = new Intent(view.getContext(), ...);
            //view.getContext().startActivity(intent);
        }
    }

    public OtherItemsOnClickListener getOtherItemsOnClickListener(Context initialContext) {
        return new OtherItemsOnClickListener(initialContext);
    }

    private class AddItemOnClickListener implements View.OnClickListener {

        private Context context;

        public AddItemOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        public void onClick(View view) {
            transitionToActivity(context, AddThingActivity.class);
        }
    }

    public AddItemOnClickListener getAddItemOnClickListener(Context initialContext) {
        return new AddItemOnClickListener(initialContext);
    }

    private class ProfileOnClickListener implements View.OnClickListener {

        private Context context;

        public ProfileOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), UserEditProfileActivity.class);

            // TODO: Implement using actual data
            User user = new User();
            user.setFirstName("Emily");
            user.setLastName("Jones");
            user.setEmail("ejones@ualberta.ca");

            intent.putExtra(UserEditProfileActivity.USER_EXTRA, user);
            view.getContext().startActivity(intent);
        }
    }

    public ProfileOnClickListener getProfileOnClickListener(Context initialContext) {
        return new ProfileOnClickListener(initialContext);
    }

    private class SearchOnClickListener implements View.OnClickListener {

        private Context context;

        public SearchOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        public void onClick(View view) {
            transitionToActivity(context, ThingSearchActivity.class);
        }
    }

    public SearchOnClickListener getSearchOnClickListener(Context initialContext) {
        return new SearchOnClickListener(initialContext);
    }
}
