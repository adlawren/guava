package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import ca.ualberta.papaya.ThingBidsActivity;

/**
 * Created by hsbarker on 3/29/16.
 *
 * Main controller for displaying Thing objects that a user is currently borrowing. It is a singleton that contains the instance and
 * methods for the ThingBorrowingActivity
 *
 * @see ca.ualberta.papaya.ThingBorrowingActivity
 */
public class ThingBorrowingController {
    private static ThingBorrowingController ourInstance = new ThingBorrowingController();

    public static ThingBorrowingController getInstance() {
        return ourInstance;
    }

    private ThingBorrowingController() {
    }

    private void transitionToActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    // switch to other items activity
    private class BiddingItemsOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;

        public BiddingItemsOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            transitionToActivity(context, ThingBidsActivity.class);

            return true;
        }
    }

    // returnt the onClickListener for OtherItems
    public BiddingItemsOnClickListener getBiddingItemsOnClickListener(Context initialContext) {
        return new BiddingItemsOnClickListener(initialContext);
    }
}
