package ca.ualberta.papaya.interfaces;

import java.util.ArrayList;

import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.Observable;

/**
 * Created by adlawren on 13/03/16.
 */
public interface IDataManager {

//    ArrayList<Thing> getLoadedThings();
//    User getCurrentUser();

    Observable<User> getCurrentUserObservable();
    Observable<ArrayList<Thing>> getCurrentUserThingsObservable();
}
