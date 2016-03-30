package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ca.ualberta.papaya.AddThingActivity;
import ca.ualberta.papaya.R;
import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.ThingSearchActivity;
import ca.ualberta.papaya.EditUserProfileActivity;
import ca.ualberta.papaya.data.ThrowawayDataManager;

/**
 * Created by adlawren on 10/03/16.
 *
 * Main activity to control Things that are shown. Displays Thing objects and allows
 * the user to perform certain actions. The actions that can be performed are:
 *      edit and view a users own profile.
 *      add a thing to user
 *      switch to the search activity
 *      switch to other items activity
 *
 * @see ThingSearchActivity
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

    // switch to other items activity
    private class OtherItemsOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;

        public OtherItemsOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            System.err.println("TODO: IMPLEMENT");

            // TODO: Implement
            //Intent intent = new Intent(view.getContext(), ...);
            //view.getContext().startActivity(intent);

            return true;
        }
    }

    // returnt the onClickListener for OtherItems
    public OtherItemsOnClickListener getOtherItemsOnClickListener(Context initialContext) {
        return new OtherItemsOnClickListener(initialContext);
    }

    // add thing button
    private class AddItemOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;

        public AddItemOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            transitionToActivity(context, AddThingActivity.class);

            return true;
        }
    }

    // return onClickListener for AddItem
    public AddItemOnClickListener getAddItemOnClickListener(Context initialContext) {
        return new AddItemOnClickListener(initialContext);
    }

    // button to view and edit a users own contact information
    private class ProfileOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;

        public ProfileOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            // Intent intent = new Intent(view.getContext(), EditUserProfileActivity.class);
            Intent intent = new Intent(context, EditUserProfileActivity.class);
            intent.putExtra(EditUserProfileActivity.USER_EXTRA, ThrowawayDataManager.getInstance()
                    .getCurrentUser(context));

            // view.getContext().startActivity(intent);
            context.startActivity(intent);

            return true;
        }
    }
    // return onClickListener for Profile
    public ProfileOnClickListener getProfileOnClickListener(Context initialContext) {
        return new ProfileOnClickListener(initialContext);
    }

    // button to switch to the search activity
    private class SearchOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;

        public SearchOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            transitionToActivity(context, ThingSearchActivity.class);

            return true;
        }
    }

    // return onClickListener for search
    public SearchOnClickListener getSearchOnClickListener(Context initialContext) {
        return new SearchOnClickListener(initialContext);
    }
/*
    //ToDo: change comments
    // button to switch to the search activity
    private class AllOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;

        public AllOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            //transitionToActivity(context, ThingSearchActivity.class);
            setFilterAll();

            return true;
        }
    }

    // return onClickListener for search
    public AllOnClickListener getAllFilterOnClickListener(Context initialContext) {
        return new AllOnClickListener(initialContext);
    }

    private class BorrowedOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;

        public BorrowedOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            //transitionToActivity(context, ThingSearchActivity.class);
            context.

            return true;
        }
    }

    // return onClickListener for search
    public BorrowedOnClickListener getBorrowedFilterOnClickListener(Context initialContext) {
        return new BorrowedOnClickListener(initialContext);
    }

    private class BiddedOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;

        public BiddedOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            //transitionToActivity(context, ThingSearchActivity.class);
            setFilterBidded();

            return true;
        }
    }

    // return onClickListener for search
    public BiddedOnClickListener getBiddedFilterOnClickListener(Context initialContext) {
        return new BiddedOnClickListener(initialContext);
    }
*/
}
