package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import ca.ualberta.papaya.ThingBorrowingActivity;

/**
 * Created by hsbarker on 3/29/16.
 */
public class ThingBidsController {
    private static ThingBidsController ourInstance = new ThingBidsController();

    public static ThingBidsController getInstance() {
        return ourInstance;
    }

    private ThingBidsController() {
    }

    private void transitionToActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    // switch to other items activity
    private class BorrowingItemsOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;

        public BorrowingItemsOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            transitionToActivity(context, ThingBorrowingActivity.class);

            return true;
        }
    }

    // returnt the onClickListener for OtherItems
    public BorrowingItemsOnClickListener getBorrowingItemsOnClickListener(Context initialContext) {
        return new BorrowingItemsOnClickListener(initialContext);
    }
}
