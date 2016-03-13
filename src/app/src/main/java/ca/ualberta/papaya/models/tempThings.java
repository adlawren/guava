package ca.ualberta.papaya.models;

import java.util.ArrayList;

/**
 * Created by VK on 12/03/2016.
 */
public class tempThings {
    ArrayList<Thing> things= new ArrayList<>();
    private static tempThings ourInstance = new tempThings();

    public static tempThings getInstance() {
        return ourInstance;
    }

    private tempThings() {

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
}
