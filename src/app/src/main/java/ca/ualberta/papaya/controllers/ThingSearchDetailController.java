package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import ca.ualberta.papaya.UserProfileActivity;
import ca.ualberta.papaya.ViewPictureActivity;
import ca.ualberta.papaya.data.ThrowawayDataManager;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;

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

    private ThingSearchDetailController() {
    }

    // The onClickMenuItem for placing a bid
    private class UserBidOnClickListener implements MenuItem.OnMenuItemClickListener {


        private Thing thing;

        public UserBidOnClickListener(Thing theThing) {
            thing = theThing;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return true;
        }

    }

    // return the onMenuItemListener for user bids
    public UserBidOnClickListener getUserBidOnClickListener(Thing theThing) {
        return new UserBidOnClickListener(theThing);
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
