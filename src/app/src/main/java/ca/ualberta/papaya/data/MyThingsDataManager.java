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
import java.lang.reflect.Type;
import java.util.ArrayList;

import ca.ualberta.papaya.controllers.ThrowawayElasticSearchController;
import ca.ualberta.papaya.interfaces.IObserver;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.util.Ctx;
import ca.ualberta.papaya.util.LocalUser;
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

    private static final String SAVED_FILENAME = "my_things.sav";
    private static final String DELETED_FILENAME = "my_things.del";

    private static ArrayList<Thing> myThings = new ArrayList<>(), zombieThings = new ArrayList<>();

    // TODO: Remove; used for debugging
    private void printThing(Thing thing) {
        System.out.println("Next thing: id: " + thing.getId() + ", title: " +
                thing.getTitle() + ", description: " + thing.getDescription());
    }

    // TODO: Remove; used for debugging
    private void printThingList(ArrayList<Thing> list) {
        for (Thing thing : list) {
            printThing(thing);
        }
    }

    public void getData(final Observable<ArrayList<Thing>> observable) {
        if (observable == null) {
            System.err.println("[GetThingsObserver.update] ERROR: Observable not initialized");
            return;
        }

        // Get local data
        loadFromFile();

        // Get online data
        Observable<ArrayList<Thing>> thingListObservable = new Observable<>();
        thingListObservable.addObserver(new IObserver<ArrayList<Thing>>() {
            @Override
            public void update(ArrayList<Thing> data) {
                resolve(data);
                saveToFile();

                observable.setData(myThings);
            }
        });

        ThrowawayElasticSearchController.SearchThingTask thingSearchTask =
                 new ThrowawayElasticSearchController.SearchThingTask(thingListObservable);
        thingSearchTask.execute("{ \"size\" : \"500\", \"query\" : { \"match\" : { \"ownerId\" : " +
                "\"" + LocalUser.getId() + "\" } } }");
    }

    public void update(final Observable<Thing> observable) {
        boolean found = false;
        for (Thing thing : myThings) {
            if (thing.getUuid().equals(observable.getData().getUuid())) {
                thing.setTitle(observable.getData().getTitle());
                thing.setDescription(observable.getData().getDescription());

                found = true;
                break;
            }
        }

        if (!found) {
            myThings.add(observable.getData());
        }

        saveToFile();

        observable.setData(observable.getData());
    }

    public void delete(final Observable<Thing> observable) {
        for (int i = 0; i < myThings.size(); ++i) {
            if (myThings.get(i).getUuid().equals(observable.getData().getUuid())) {
                zombieThings.add(myThings.get(i));
                myThings.remove(i);
                break;
            }
        }

        saveToFile();

        observable.setData(observable.getData());
    }

    // Used to resolve differences between the local contents and the remote contents
    private void resolve(ArrayList<Thing> remoteThings) {
        ArrayList<Thing> uniqueLocalThings = new ArrayList<>(),
                commonThings = new ArrayList<>();

        uniqueLocalThings.addAll(myThings);

        for (Thing remoteThing : remoteThings) {
            Integer index = null;
            for (int i = 0; i < uniqueLocalThings.size(); ++i) {
                if (uniqueLocalThings.get(i).getId() == null) continue;

                if (uniqueLocalThings.get(i).getId().equals(remoteThing.getId())) {
                    index = i;
                    break;
                }
            }

            if (index != null) {
                commonThings.add(uniqueLocalThings.get(index));
                uniqueLocalThings.remove(index);
            } else {
                int j;
                for (j = 0; j < zombieThings.size(); ++j) {
                    if (zombieThings.get(j).getUuid().equals(remoteThing.getUuid())) {

                        // Deleted thing
                        break;
                    }
                }

                // Unique remote thing
                if (j == zombieThings.size()) myThings.add(remoteThing);
            }
        }

        // Add newly created local things
        Observable<ArrayList<Thing>> addedThingsObservable = new Observable<>();
        addedThingsObservable.addObserver(new IObserver<ArrayList<Thing>>() {
            @Override
            public void update(ArrayList<Thing> data) {
                if (data.size() > 0) {
                    saveToFile();
                }
            }
        });

        ThrowawayElasticSearchController.AddThingTask addThingTask =
                new ThrowawayElasticSearchController.AddThingTask(addedThingsObservable);

        Thing[] uniqueLocalThingsArray = new Thing[ uniqueLocalThings.size() ];
        uniqueLocalThingsArray = uniqueLocalThings.toArray(uniqueLocalThingsArray);

        addThingTask.execute(uniqueLocalThingsArray);

        // Update common things
        Observable<ArrayList<Thing>> updatedThingsObservable = new Observable<>();
        updatedThingsObservable.addObserver(new IObserver<ArrayList<Thing>>() {
            @Override
            public void update(ArrayList<Thing> data) {
            }
        });

        ThrowawayElasticSearchController.UpdateThingTask updateThingTask =
                new ThrowawayElasticSearchController.UpdateThingTask(updatedThingsObservable);

        Thing[] commonThingsArray = new Thing[ commonThings.size() ];
        commonThingsArray = commonThings.toArray(commonThingsArray);

        updateThingTask.execute(commonThingsArray);

        // Delete removed things
        Observable<ArrayList<Thing>> deletedThingsObservable = new Observable<>();
        deletedThingsObservable.addObserver(new IObserver<ArrayList<Thing>>() {
            @Override
            public void update(ArrayList<Thing> data) {
            }
        });

        ThrowawayElasticSearchController.DeleteThingTask deleteThingTask =
                new ThrowawayElasticSearchController.DeleteThingTask(deletedThingsObservable);

        Thing[] zombieThingsArray = new Thing[ zombieThings.size() ];
        zombieThingsArray = zombieThings.toArray(zombieThingsArray);

        deleteThingTask.execute(zombieThingsArray);

        saveToFile();
    }

    private void loadFromFile() {
        ArrayList<Thing> savedThings = loadArrayListFromFile(SAVED_FILENAME);
        if (savedThings != null) {
            myThings.clear();
            myThings.addAll(savedThings);
        }

//        ArrayList<Thing> deletedThings = loadArrayListFromFile(DELETED_FILENAME);
//        if (deletedThings != null) {
//            zombieThings.clear();
//            zombieThings.addAll(deletedThings);
//        }
    }

    private void saveToFile() {
        saveArrayListToFile(myThings, SAVED_FILENAME);
        // saveArrayListToFile(zombieThings, DELETED_FILENAME);
    }

    // TODO: Use
    private ArrayList<Thing> loadArrayListFromFile(String filename) {
        ArrayList<Thing> things = null;

        try {

            // Initialize BufferedReader using stream from the given file
            FileInputStream stream = Ctx.get().openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            Gson gson = new Gson();

            // Taken from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html
            // Parse the Json stored in the file
            Type listType = new TypeToken<ArrayList<Thing>>() {}.getType();

            things = gson.fromJson(reader, listType);

            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return things;
    }

    private void saveArrayListToFile(ArrayList<Thing> things, String filename) {

        // TODO: Determine if the copies are needed
        ArrayList<Thing> tempThings = new ArrayList<>();
        tempThings.addAll(things);

        try {

            // Initialize BufferedWriter using stream from the given file
            FileOutputStream stream = Ctx.get().openFileOutput(filename, Context.MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));

            // Write the Json to the given file
            Gson gson = new Gson();

            gson.toJson(tempThings, writer);
            writer.flush();

            stream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
