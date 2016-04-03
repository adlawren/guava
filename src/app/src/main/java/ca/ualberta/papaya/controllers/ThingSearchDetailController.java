package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.math.BigDecimal;


import ca.ualberta.papaya.ThingSearchActivity;

import ca.ualberta.papaya.ViewPictureActivity;

import ca.ualberta.papaya.exceptions.ThingUnavailableException;
import ca.ualberta.papaya.models.Bid;

import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.Ctx;
import ca.ualberta.papaya.util.LocalUser;
import ca.ualberta.papaya.util.Observer;

/**
 * Created by adlawren on 14/03/16.
 *
 * Main controller for displaying Thing objects that are searched. It is a singleton that contains the instance and
 * methods for the ThingSearchDetailActivity
 *
 * @see ca.ualberta.papaya.ThingSearchDetailActivity
 *
 */
public class ThingSearchDetailController {
    private static ThingSearchDetailController ourInstance = new ThingSearchDetailController();

    public static ThingSearchDetailController getInstance() {
        return ourInstance;
    }

    private void transitionToActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    private ThingSearchDetailController() {
    }

    // The onClickMenuItem for placing a bid
    private class UserBidOnClickListener implements MenuItem.OnMenuItemClickListener {


        private Thing thing;
        private EditText bidAmount;
        private Context context;

        public UserBidOnClickListener(Thing theThing, EditText bidAmountText, Context theContext) {
            thing = theThing;
            bidAmount = bidAmountText;
            context = theContext;
        }

        private BigDecimal parseCurrency(String value){
            String numeric = value.replaceAll("[^\\d.]+", "");
            BigDecimal money = BigDecimal.valueOf(Double.parseDouble(numeric))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            return money;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            final BigDecimal money = parseCurrency(bidAmount.getText().toString());
            LocalUser.getUser(new Observer<User>() {
                @Override
                public void update(User bidder) {
                    try {
                        BigDecimal cents = BigDecimal.valueOf(100);
                        Bid bid = new Bid(thing, bidder, money.multiply(cents).intValue());
                        System.err.println("Bid Placed");
                        bid.publish(new Observer<Bid>() {
                            @Override
                            public void update(Bid bidSent) {
                                // todo: update bid list instead.
                                System.err.println("Bid Published");
                                transitionToActivity(context, ThingSearchActivity.class);
                            }
                        });

                    } catch (ThingUnavailableException e){
                        // todo: toaster
                        e.printStackTrace();
                    }
                }
            });

            return true;
        }

    }

    // return the onMenuItemListener for user bids
    public UserBidOnClickListener getUserBidOnClickListener(Thing theThing, EditText bidAmount, Context theContext) {
        return new UserBidOnClickListener(theThing, bidAmount, theContext);
    }


    // The onClick listener for user info
    private class UserInfoOnClickListener implements View.OnClickListener {

        private User user;

        public UserInfoOnClickListener(User theUser) {
            user = theUser;
        }

        @Override
        public void onClick(View view) {

        }

    }

    // return the onClickListener for user bids
    public UserInfoOnClickListener getUserInfoOnClickListener(User theUser) {
        return new UserInfoOnClickListener(theUser);
    }

    // The onClickMenuItem for placing a bid
    private class ImageOnClickListener implements MenuItem.OnMenuItemClickListener {

        private Context context;

        private Thing thing;

        public ImageOnClickListener(Context thisContext, Thing theThing) {
            thing = theThing;
            context = thisContext;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            Intent intent = new Intent(context, ViewPictureActivity.class);
            intent.putExtra(ViewPictureActivity.PICTURE_EXTRA, thing.getPhoto().getImage());

            context.startActivity(intent);
            return true;
        }


    }

    // return the onMenuItemListener for user bids
    public ImageOnClickListener getImageOnClickListener(Context thisContext, Thing theThing) {
        return new ImageOnClickListener(thisContext, theThing);
    }


}
