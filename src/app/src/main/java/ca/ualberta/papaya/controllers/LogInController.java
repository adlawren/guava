package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.interfaces.IObserver;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.LocalUser;
import ca.ualberta.papaya.util.Observable;

/**
 * Created by adlawren on 31/03/16.
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
        context.startActivity(intent);
    }

    // Save Button
    private class SubmitOnClickListener implements View.OnClickListener {

        private Context context;
        private EditText userNameEditText, passwordEditText;
        private Bitmap image;

        public SubmitOnClickListener(Context initialContext, EditText initialUserNameEditText,
                                     EditText initialPasswordEditText) {
            context = initialContext;
            userNameEditText = initialUserNameEditText;
            passwordEditText = initialPasswordEditText;
        }

        private void transitionToActivity(Context context, Class activityClass) {
            Intent intent = new Intent(context, activityClass);
            context.startActivity(intent);
        }

        @Override
        public void onClick(View view) {
            final String userName = userNameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            Observable<ArrayList<User>> observable = new Observable<>();
            observable.addObserver(new IObserver<ArrayList<User>>() {
                @Override
                public void update(ArrayList<User> data) {
                    User currentUser = null;

                    System.out.println("User list:");
                    for (User user : data) {
                        System.out.println("Next user: id: " + user.getId());
                        if (user.getEmail().equals(userName)) {
                            currentUser = user;
                            LocalUser.setId(user.getId());
                            break;
                        }
                    }

                    if (currentUser == null) {
                        Observable<User> userObservable = new Observable<>();
                        userObservable.addObserver(new IObserver<User>() {
                            @Override
                            public void update(User data) {

                                // TODO: Remove; test
                                System.out.println("Added user: id: " + data.getId());

                                LocalUser.setId(data.getId());

                                transitionToActivity(context, ThingListActivity.class);
                            }
                        });

                        currentUser = new User();

                        // TODO: Remove; test
                        currentUser.setFirstName("Things");
                        currentUser.setLastName("McGee");

                        currentUser.setEmail(userName);

                        ThrowawayElasticSearchController.AddUserTask addUserTask =
                                new ThrowawayElasticSearchController.AddUserTask(userObservable);
                        addUserTask.execute(currentUser);
                    } else {
                        transitionToActivity(context, ThingListActivity.class);
                    }
                }
            });

            ThrowawayElasticSearchController.SearchUserTask searchUserTask =
                    new ThrowawayElasticSearchController.SearchUserTask(observable);
            searchUserTask.execute("{}");
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
