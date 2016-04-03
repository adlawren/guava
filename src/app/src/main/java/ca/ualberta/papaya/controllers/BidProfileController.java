package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import ca.ualberta.papaya.DisplayLocationActivity;
import ca.ualberta.papaya.EditThingActivity;
import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.ViewPictureActivity;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.util.Observer;

/**
 * Created by adlawren on 13/03/16.
 *
 * Main controller for displaying a single item. It is a singleton that contains the instance and
 * methods for the ThingDetailActivity
 *
 * @see ca.ualberta.papaya.ThingDetailActivity
 */
public class BidProfileController {
    private static BidProfileController ourInstance = new BidProfileController();

    public static BidProfileController getInstance() {
        return ourInstance;
    }

    private BidProfileController() {
    }

    //Change to the activity
    private void transitionToActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    // button for editing the item
    private class AcceptOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;

        private Thing thing;

        public AcceptOnClickListener(Context initialContext, Thing initialThing) {
            context = initialContext;
            thing = initialThing;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            Intent intent = new Intent(context, EditThingActivity.class);
            intent.putExtra(EditThingActivity.THING_EXTRA, thing);

            context.startActivity(intent);

            return true;
        }
    }

    // return the onClickListener for accept
    public AcceptOnClickListener getAcceptOnClickListener(Context initialContext, Thing initialThing) {
        return new AcceptOnClickListener(initialContext, initialThing);
    }

    // Button for Declinig the bid
    private class DeclineOnClickListener implements MenuItem.OnMenuItemClickListener { // implements View.OnClickListener {

        private Context context;
        private Thing thing;

        public DeclineOnClickListener(Context initialContext, Thing initialThing) {
            context = initialContext;
            thing = initialThing;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            Thing.delete(new Observer<Thing>() {
                @Override
                public void update(Thing thing) {

                    // TODO: Remove; test
                    // System.out.println("[EditThingController.delete] In update.");
                }
            }, Thing.class, thing);

            transitionToActivity(context, ThingListActivity.class);

            return true;
        }
    }

    // return the onClickListener for decline
    public DeclineOnClickListener getDeclineOnClickListener(Context initialContext,
                                                                  Thing initialThing) {
        return new DeclineOnClickListener(initialContext, initialThing);
    }


}
