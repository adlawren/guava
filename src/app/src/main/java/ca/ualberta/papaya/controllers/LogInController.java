package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.papaya.EditUserProfileActivity;
import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.interfaces.IObserver;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.Ctx;
import ca.ualberta.papaya.util.LocalUser;
import ca.ualberta.papaya.util.Observable;
import ca.ualberta.papaya.util.Observer;

/**
 * Created by adlawren on 31/03/16.
 *
 * Main controller for logging into a user. It is a singleton that contains the instance and
 * methods for the LogInActivity
 *
 * @see ca.ualberta.papaya.LogInActivity
 *
 */
public class LogInController {
    private static LogInController ourInstance = new LogInController();

    public static LogInController getInstance() {
        return ourInstance;
    }

    private LogInController() {
    }

    private void transitionToActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // Save Button
    private class SubmitOnClickListener implements View.OnClickListener {

        private Context context;
        private EditText userNameEditText, passwordEditText;

        public SubmitOnClickListener(Context initialContext, EditText initialUserNameEditText,
                                     EditText initialPasswordEditText) {
            context = initialContext;
            userNameEditText = initialUserNameEditText;
            passwordEditText = initialPasswordEditText;
        }

        private void transitionToActivity(Context context, Class activityClass) {
            Intent intent = new Intent(context, activityClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        @Override
        public void onClick(View view) {
            final String userName = userNameEditText.getText().toString();
            final String password = passwordEditText.getText().toString();

            try {

                JSONObject jsonUsername = new JSONObject();
                jsonUsername.put("username", userName);
                JSONObject jsonMatch = new JSONObject();
                jsonMatch.put("match", jsonUsername);
                JSONObject query = new JSONObject();
                query.put("query", jsonMatch);

                User.search(new Observer<List<User>>() {
                    @Override
                    public void update(List<User> users) {

                        User currentUser = null;
                        boolean badPassword = false;

                        for (User user : users) {
                            if (user.getUsername().equals(userName) ) {
                                if(user.checkPassword(password)) {
                                    currentUser = user;
                                    LocalUser.setId(user.getId());
                                    break;
                                } else {
                                    badPassword = true;
                                }
                            }
                        }

                        if (currentUser == null && !badPassword) {

                            currentUser = new User();

                            currentUser.setUsername(userName);
                            currentUser.setPassword(password);

                            currentUser.publish(new Observer<User>() {
                                @Override
                                public void update(User addedUser) {
                                    LocalUser.setId(addedUser.getId());
                                    System.err.println("BEFORE USER PROFILE EDIT TRANSITION");
                                    System.err.println(addedUser.getId() + " " + LocalUser.getId());
                                    transitionToActivity(Ctx.get(), EditUserProfileActivity.class);
                                }
                            });

                        } else if(currentUser == null && badPassword ) {
                            // show we do something about this?
                        } else {
                            transitionToActivity(context, ThingListActivity.class);
                        }

                    }
                }, User.class, query.toString());


            } catch (JSONException e){
                e.printStackTrace();
            }

        }
    }

    // return the onClickListener
    public SubmitOnClickListener getSubmitOnClickListener(Context initialContext,
                                                      EditText initialUserNameEditText,
                                                      EditText initialPasswordEditText) {
        return new SubmitOnClickListener(initialContext, initialUserNameEditText,
                initialPasswordEditText);
    }
}
