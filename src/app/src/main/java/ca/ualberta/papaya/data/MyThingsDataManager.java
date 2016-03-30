package ca.ualberta.papaya.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.controllers.ThrowawayElasticSearchController;
import ca.ualberta.papaya.interfaces.IObserver;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.util.Ctx;
import ca.ualberta.papaya.util.Observable;

/**
 * Created by adlawren on 28/03/16.
 */
public class MyThingsDataManager {
    private static MyThingsDataManager ourInstance = new MyThingsDataManager();

    public static MyThingsDataManager getInstance() {
        return ourInstance;
    }

    private MyThingsDataManager() {
    }

    private static final String FILENAME = "my_things.sav";

    private static ArrayList<Thing> myThings = new ArrayList<>();

    public void getData(final Observable<ArrayList<Thing>> observable) {

        // Get local data
        // loadFromFile();

        // Get online data
        Observable<ArrayList<Thing>> thingListObservable = new Observable<>();
        thingListObservable.addObserver(new IObserver<ArrayList<Thing>>() {
            @Override
            public void update(ArrayList<Thing> data) {
                if (data.size() > 0) {
                    if (myThings != null && myThings.size() > 0) {
                        resolve(data);
                        observable.setData(myThings);
                    } else {
                        myThings.addAll(data);
                        observable.setData(data);
                    }
                }
            }
        });

        ThrowawayElasticSearchController.SearchThingTask thingSearchTask =
                new ThrowawayElasticSearchController.SearchThingTask(thingListObservable);

        thingSearchTask.execute("{}");

        observable.setData(myThings);
    }

    public void update(Thing thing) {
        if (myThings.contains(thing)) {

            // TODO: See if myThings.get(...) works instead
            for (Thing nextThing : myThings) {
                if (nextThing.getId().equals(thing.getId())) {
                    nextThing.setTitle(thing.getTitle());
                    nextThing.setDescription(thing.getDescription());
                    break;
                }
            }
        } else {
            myThings.add(thing);
        }

        System.out.println("[MyThingsDataManager.update] Thing list contents:");
        for (Thing nextThing : myThings) {
            System.out.println("Thing id: " + nextThing.getId() +
                    ", Thing title: " + nextThing.getTitle() +
                    ", Thing description: " + nextThing.getDescription());
        }

        // Get online data
        Observable<ArrayList<Thing>> thingListObservable = new Observable<>();
        thingListObservable.addObserver(new IObserver<ArrayList<Thing>>() {
            @Override
            public void update(ArrayList<Thing> data) {
                System.out.println("[MyThingsDataManager.update.update] Thing list contents: ");
                for (Thing localThing : myThings) {
                    System.out.println("Thing id: " + localThing.getId() +
                            ", Thing title: " + localThing.getTitle() +
                            ", Thing description: " + localThing.getDescription());
                }

                resolve(data);
            }
        });

        ThrowawayElasticSearchController.SearchThingTask thingSearchTask =
                new ThrowawayElasticSearchController.SearchThingTask(thingListObservable);

        thingSearchTask.execute("{}");
    }

    // Used to resolve differences between the local contents and the remote contents
    private void resolve(ArrayList<Thing> remoteThings) {
        ArrayList<Thing> things = new ArrayList<>();
        things.addAll(myThings);

        if (remoteThings.size() > 0) {
            for (Thing nextRemoteThing : remoteThings) {
                int i;
                for (i = 0; i < things.size(); ++i) {
                    if (things.get(i).getId().equals(nextRemoteThing.getId())) {
                        break;
                    }
                }

                if (i == things.size()) {
                    things.add(nextRemoteThing);
                }
            }
        }

        myThings.clear();
        myThings.addAll(things);

        System.out.println("[MyThingsDataManager.resolve] Thing list contents: ");
        for (Thing thing : myThings) {
            System.out.println("Thing id: " + thing.getId() +
                    ", Thing title: " + thing.getTitle() +
                    ", Thing description: " + thing.getDescription());
        }

        // saveToFile();
    }

    private void loadFromFile() {
        try {

            // Initialize BufferedReader using stream from the given file
            FileInputStream stream = Ctx.get().openFileInput(FILENAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            Gson gson = new Gson();

            // Taken from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html
            // Parse the Json stored in the file
            Type listType = new TypeToken<ArrayList<Thing>>() {}.getType();

            ArrayList<Thing> tempThings = gson.fromJson(reader, listType);

            myThings.clear();
            myThings.addAll(tempThings);

            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try {

            // Initialize BufferedWriter using stream from the given file
            FileOutputStream stream = Ctx.get().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));

            // Write the Json to the given file
            Gson gson = new Gson();

            ArrayList<Thing> things = new ArrayList<>();
            things.addAll(myThings);

            gson.toJson(things, writer);
            writer.flush();

            stream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
