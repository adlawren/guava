package ca.ualberta.papaya.controllers;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

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

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    private class SaveOnClickListener implements View.OnClickListener {

        private TextView textView;

        public SaveOnClickListener(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void onClick(View view) {
            String text = textView.getText().toString();

            // ...

            
        }
    }

    public SaveOnClickListener getSaveOnClickListener(TextView textView) {
        return new SaveOnClickListener(textView);
    }

    private class CancelOnClickListener implements View.OnClickListener {

        public CancelOnClickListener() {}

        @Override
        public void onClick(View view) {
            // ...
        }
    }

    public CancelOnClickListener getCancelOnClickListener() {
        return new CancelOnClickListener();
    }
}
