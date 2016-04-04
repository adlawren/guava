package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import ca.ualberta.papaya.DisplayLocationActivity;
import ca.ualberta.papaya.EditThingActivity;
import ca.ualberta.papaya.ThingDetailActivity;
import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.ViewPictureActivity;
import ca.ualberta.papaya.exceptions.ThingUnavailableException;
import ca.ualberta.papaya.models.Bid;
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
    private class AcceptOnClickListener implements MenuItem.OnMenuItemClickListener {

        private Context context;

        private Bid bid;

        public AcceptOnClickListener(Context initialContext, Bid theBid) {
            context = initialContext;
            bid = theBid;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            bid.getThing(new Observer<Thing>() {
                @Override
                public void update(Thing thing) {
                    try {
                        thing.acceptBid(bid);
                        transitionToActivity(context, ThingListActivity.class);


                    } catch (ThingUnavailableException e){
                        e.printStackTrace();
                    }
                }
            });

            return true;
        }
    }

    // return the onClickListener for accept
    public AcceptOnClickListener getAcceptOnClickListener(Context initialContext, Bid theBid) {
        return new AcceptOnClickListener(initialContext, theBid);
    }

    // Button for Declinig the bid
    private class DeclineOnClickListener implements MenuItem.OnMenuItemClickListener {

        private Context context;
        private Bid bid;

        public DeclineOnClickListener(Context initialContext, Bid theBid) {
            context = initialContext;
            bid = theBid;
        }

        @Override
        // public void onClick(View view) {
        public boolean onMenuItemClick(MenuItem item) {
            Bid.delete(new Observer<Bid>() {
                @Override
                public void update(Bid bid) {

                    bid.getThing(new Observer<Thing>() {
                        @Override
                        public void update(Thing thing) {
                            Intent intent = new Intent(context, ThingDetailActivity.class);
                            intent.putExtra(EditThingActivity.THING_EXTRA, thing);
                            context.startActivity(intent);
                        }
                    });

                }
            }, Bid.class, bid);



            return true;
        }
    }

    // return the onClickListener for decline
    public DeclineOnClickListener getDeclineOnClickListener(Context initialContext,
                                                                  Bid theBid) {
        return new DeclineOnClickListener(initialContext, theBid);
    }


}
