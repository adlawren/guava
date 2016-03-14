package ca.ualberta.papaya.interfaces;

import android.content.Context;

import java.util.ArrayList;

import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.Observable;

/**
 * Created by adlawren on 13/03/16.
 */
public interface IDataManager {
    User getCurrentUser(Context context);
    void setCurrentUser(Context context, User newUser);

    ArrayList<Thing> getCurrentUserThings(Context context);
    void setCurrentUserThings(Context context, ArrayList<Thing> newThings);

    ArrayList<Thing> getNonCurrentUserThings();
}
