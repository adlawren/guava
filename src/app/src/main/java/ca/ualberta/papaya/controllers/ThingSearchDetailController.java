package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import ca.ualberta.papaya.UserProfileActivity;
import ca.ualberta.papaya.models.User;

/**
 * Created by adlawren on 14/03/16.
 */
public class ThingSearchDetailController {
    private static ThingSearchDetailController ourInstance = new ThingSearchDetailController();

    public static ThingSearchDetailController getInstance() {
        return ourInstance;
    }

    private ThingSearchDetailController() {
    }

    private class UserOnClickListener implements View.OnClickListener {

        private Context context;

        private User user;

        public UserOnClickListener(Context initialContext, User initialUser) {
            context = initialContext;
            user = initialUser;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, UserProfileActivity.class);
            intent.putExtra(UserProfileActivity.USER_EXTRA, user);

            context.startActivity(intent);
        }
    }

    public UserOnClickListener getUserOnClickListener(Context initialContext,
                                                            User initialUser) {
        return new UserOnClickListener(initialContext, initialUser);
    }
}
