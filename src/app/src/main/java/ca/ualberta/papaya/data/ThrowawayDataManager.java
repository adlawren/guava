package ca.ualberta.papaya.data;

import java.util.ArrayList;

import ca.ualberta.papaya.interfaces.IDataManager;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;

/**
 * Created by VK on 12/03/2016.
 *
 * Edited by adlawren on 13/03/2016
 *
 * Note: Originally named tempThings.java
 *
 */
public class ThrowawayDataManager implements IDataManager {

    private ArrayList<Thing> things = new ArrayList<Thing>();

    private void initThrowawayData() {

        User owner = new User(), borrower = new User();
        owner.setFirstName("Emily");
        owner.setLastName("Jones");
        owner.setEmail("ejones@ualberta.ca");

        for (int i = 0; i < 15; ++i) {
            Thing thing = new Thing(getCurrentUser());
            thing.setDescription("Thing " + i);
            thing.setOwner(owner);

            things.add(thing);
        }
    }

    private static ThrowawayDataManager ourInstance = new ThrowawayDataManager();

    public static ThrowawayDataManager getInstance() {
        return ourInstance;
    }

    private ThrowawayDataManager() {
        initThrowawayData();
    }

    public ArrayList<Thing> getThings(){
        return things;
    }

    public void addThings(Thing thing){
        things.add(thing);
    }

    public void deleteThing(int index){

        System.err.println(index);
        System.err.println(things.size());
        things.remove(index);
    }

    public Thing getThingAt(int index){

        return things.get(index);
    }

    // TODO: Implement
    @Override
    public ArrayList<Thing> getLoadedThings() {
        System.err.println("TODO: Implement getLoadedThings");

        return new ArrayList<Thing>();
    }

    // TODO: Implement
    @Override
    public User getCurrentUser() {
        System.err.println("TODO: Implement getCurrentUser");

        User user = new User();
        user.setFirstName("Emily");
        user.setLastName("Jones");
        user.setEmail("ejones@ualberta.ca");

        return user;
    }
}
