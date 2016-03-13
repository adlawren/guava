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
 * Note: Originally named ThrowawayDataManager.java
 *
 */
public class ThrowawayDataManager implements IDataManager {

    private ArrayList<Thing> things = new ArrayList<Thing>();

    private static ThrowawayDataManager ourInstance = new ThrowawayDataManager();

    public static ThrowawayDataManager getInstance() {
        return ourInstance;
    }

    private ThrowawayDataManager() {

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

        return new User();
    }
}
