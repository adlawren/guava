package ca.ualberta.papaya.data;

import java.util.ArrayList;

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

    private static Observable<User> observedCurrentUser = null;

    @Override
    public Observable<User> getCurrentUserObservable() {
        return observedCurrentUser;
    }

    private static Observable<ArrayList<Thing>> observedCurrentUserThings = null;

    @Override
    public Observable<ArrayList<Thing>> getCurrentUserThingsObservable() {
        return observedCurrentUserThings;
    }

    private ArrayList<Thing> things = new ArrayList<Thing>();
//    public ArrayList<Thing> getThings(){
//        return things;
//    }

    private void initThrowawayData() {

        User currentUser = new User(), borrower = new User();
        currentUser.setFirstName("Emily");
        currentUser.setLastName("Jones");
        currentUser.setEmail("ejones@ualberta.ca");

        observedCurrentUser = new Observable<User>(currentUser);

        for (int i = 0; i < 15; ++i) {
            Thing thing = new Thing(getCurrentUserObservable().getData());
            thing.setDescription("Thing " + i);
            thing.setOwner(currentUser);

            things.add(thing);
        }

        observedCurrentUserThings = new Observable<ArrayList<Thing>>(things);
    }

    private static ThrowawayDataManager ourInstance = new ThrowawayDataManager();

    public static ThrowawayDataManager getInstance() {
        return ourInstance;
    }

    private ThrowawayDataManager() {
        initThrowawayData();
    }

//    public void addThings(Thing thing){
//        things.add(thing);
//    }
//
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
