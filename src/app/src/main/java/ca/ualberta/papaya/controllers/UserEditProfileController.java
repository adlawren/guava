package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.interfaces.Observer;

/**
 * Created by adlawren on 07/03/16.
 */
public class UserEditProfileController {
    private static UserEditProfileController ourInstance = new UserEditProfileController();

    public static UserEditProfileController getInstance() {
        return ourInstance;
    }

    private UserEditProfileController() {}

    private ArrayList<Observer> observers;

    private void updateObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    private void transitionToActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    private class SaveOnClickListener implements View.OnClickListener {

        private Context context;

        private EditText firstNameEditText;
        private EditText lastNameEditText;
        private EditText emailEditText;
        private EditText address1EditText;
        private EditText address2EditText;
        private EditText provinceEditText;
        private EditText countryEditText;
        private EditText postalEditText;

        public SaveOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        public void onClick(View view) {
            TextView textView = (TextView) view;

            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String address1 = address1EditText.getText().toString();
            String address2 = address2EditText.getText().toString();
            String province = provinceEditText.getText().toString();
            String country = countryEditText.getText().toString();
            String postal = postalEditText.getText().toString();

            // TODO: Save the profile updates
            // ...

            transitionToActivity(context, ThingListActivity.class);
        }
    }

    public SaveOnClickListener getSaveOnClickListener(Context initialContext) {
        return new SaveOnClickListener(initialContext);
    }

    private class CancelOnClickListener implements View.OnClickListener {

        private Context context;

        public CancelOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        public void onClick(View view) {
            transitionToActivity(context, ThingListActivity.class);
        }
    }

    public CancelOnClickListener getCancelOnClickListener(Context initialContext) {
        return new CancelOnClickListener(initialContext);
    }
}
