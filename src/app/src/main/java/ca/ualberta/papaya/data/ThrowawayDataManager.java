package ca.ualberta.papaya.data;

import java.util.ArrayList;
import java.util.UUID;

import ca.ualberta.papaya.interfaces.IDataManager;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.Observable;

/**
 * Created by VK on 12/03/2016.
 *
 * Edited by adlawren on 13/03/2016
 *
 * Note: Originally named tempThings.java
 *
 */
public class ThrowawayDataManager implements IDataManager {

    private static Observable<User> currentUserObservable = null;

    @Override
    public Observable<User> getCurrentUserObservable() {
        return currentUserObservable;
    }

    private static Observable<ArrayList<Thing>> currentUserThingsObservable = null;

    @Override
    public Observable<ArrayList<Thing>> getCurrentUserThingsObservable() {
        return currentUserThingsObservable;
    }

    private void initThrowawayData() {

        User currentUser = new User(), borrower = new User();
        currentUser.setFirstName("Emily");
        currentUser.setLastName("Jones");
        currentUser.setEmail("ejones@ualberta.ca");

        currentUserObservable.setData(currentUser);

        ArrayList<Thing> things = new ArrayList<>();
        for (int i = 0; i < 15; ++i) {
            Thing thing = new Thing(getCurrentUserObservable().getData());
            thing.setTitle("Thing " + (i + 1));
            thing.setDescription("Description " + (i + 1));
            thing.setOwner(currentUser);

            things.add(thing);
        }

        currentUserThingsObservable.setData(things);
    }

    private static ThrowawayDataManager ourInstance = new ThrowawayDataManager();

    public static ThrowawayDataManager getInstance() {
        return ourInstance;
    }

    private ThrowawayDataManager() {
        currentUserObservable = new Observable<>(null);
        currentUserThingsObservable = new Observable<>(null);

        initThrowawayData();
    }

//    public void deleteThing(int index){
//
//        System.err.println(index);
//        System.err.println(things.size());
//        things.remove(index);
//    }
//
//    public Thing getThingAt(int index){
//
//        return things.get(index);
//    }
}
