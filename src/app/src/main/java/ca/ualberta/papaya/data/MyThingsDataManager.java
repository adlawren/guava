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
import java.util.Vector;

import ca.ualberta.papaya.ThingListActivity;
import ca.ualberta.papaya.controllers.ThrowawayElasticSearchController;
import ca.ualberta.papaya.interfaces.IObserver;
import ca.ualberta.papaya.models.ElasticModel;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.util.Ctx;
import ca.ualberta.papaya.util.LocalUser;
import ca.ualberta.papaya.util.Observable;
import ca.ualberta.papaya.util.Observer;

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

    private static ArrayList<Thing> myThings = new ArrayList<>(), zombieThings = new ArrayList<>();

//    public void setData(ArrayList<Thing> newThings) {
//        myThings = newThings;
//    }

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
        // loadFromFile();

        // Get online data
        Observable<ArrayList<Thing>> thingListObservable = new Observable<>();
        thingListObservable.addObserver(new IObserver<ArrayList<Thing>>() {
            @Override
            public void update(ArrayList<Thing> data) {
                if (data.size() > 0) {

                    System.out.println("[MyThingsDataManager.getData] Things:");
                    printThingList(data);

                    // TODO: ************ Remove; test **************
                    myThings.clear();

                    if (false) { // (myThings.size() > 0) {
                        Observable<ArrayList<Thing>> thingsObservable = new Observable<>();
                        thingsObservable.addObserver(new IObserver<ArrayList<Thing>>() {
                            @Override
                            public void update(ArrayList<Thing> data) {

                            }
                        });

                        resolve(data, thingsObservable);
                    } else {
                        myThings.addAll(data);
                    }
                }

                observable.setData(myThings);
            }
        });

         ThrowawayElasticSearchController.SearchThingTask thingSearchTask =
                 new ThrowawayElasticSearchController.SearchThingTask(thingListObservable);
        thingSearchTask.execute("{ \"size\" : \"500\", \"query\" : { \"match\" : { \"ownerId\" : " +
                "\"" + LocalUser.getId() + "\" } } }");
    }

    public void update(final Observable<Thing> observable) {

        // TODO: See if myThings.contains(...) works, also see if myThings.get(...) works
        if (observable.getData().getId() == null) {
            myThings.add(observable.getData());

            Observable<ArrayList<Thing>> thingsObservable = new Observable<>();
            thingsObservable.addObserver(new IObserver<ArrayList<Thing>>() {
                @Override
                public void update(ArrayList<Thing> data) {
                    System.out.println("[MyThingsDataManager.update] Added things:");
                    printThingList(data);

                    observable.setData(observable.getData());
                }
            });

            ThrowawayElasticSearchController.AddThingTask addThingTask =
                    new ThrowawayElasticSearchController.AddThingTask(thingsObservable);
            addThingTask.execute(observable.getData());
        } else {
            for (Thing thing : myThings) {
                if (thing.getId().equals(observable.getData().getId())) {
                    thing.setTitle(observable.getData().getTitle());
                    thing.setDescription(observable.getData().getDescription());
                    break;
                }
            }

            Observable<ArrayList<Thing>> thingsObservable = new Observable<>();
            thingsObservable.addObserver(new IObserver<ArrayList<Thing>>() {
                @Override
                public void update(ArrayList<Thing> data) {
                    System.out.println("[MyThingsDataManager.update] Updated things:");
                    printThingList(data);

                    observable.setData(observable.getData());
                }
            });

            ThrowawayElasticSearchController.UpdateThingTask updateThingTask =
                    new ThrowawayElasticSearchController.UpdateThingTask(thingsObservable);
            updateThingTask.execute(observable.getData());
        }
    }

    public void delete(final Observable<Thing> observable) {

        // TODO: See if myThings.contains(...) works, also see if myThings.get(...) works
        for (int i = 0; i < myThings.size(); ++i) {
            if (myThings.get(i).getId().equals(observable.getData().getId())) {
                zombieThings.add(myThings.get(i));
                myThings.remove(i);
                break;
            }
        }

        Observable<Thing> thingObservable = new Observable<>();
        thingObservable.addObserver(new IObserver<Thing>() {
            @Override
            public void update(Thing data) {
                if (data != null) {

                    // TODO: Test; might need to use remove([index])
                    zombieThings.remove(data);
                }

                observable.setData(observable.getData());
            }
        });

        // Note assumption
        Thing toDelete = zombieThings.get(0);

        ThrowawayElasticSearchController.DeleteThingTask deleteThingTask =
                new ThrowawayElasticSearchController.DeleteThingTask(thingObservable);
        deleteThingTask.execute(toDelete);
    }

    // Used to resolve differences between the local contents and the remote contents
    private void resolve(ArrayList<Thing> remoteThings, final Observable<ArrayList<Thing>> thingsObservable) {
        final ArrayList<Thing> localThings = new ArrayList<>();
        final ArrayList<Thing> newThings = new ArrayList<>();

        localThings.addAll(myThings);
        myThings.clear();

        System.out.println("[MyThingsDataManager.resolve] Things:");
        printThingList(localThings);

        if (remoteThings.size() > 0) {
            for (Thing nextRemoteThing : remoteThings) {
                int i;
                for (i = 0; i < localThings.size(); ++i) {
                    if (localThings.get(i).getId().equals(nextRemoteThing.getId())) {

                        // TODO: Update the elastic search object
                        // ...

                        newThings.add(localThings.get(i));

                        localThings.remove(i);
                        break;
                    }
                }

                if (i == localThings.size()) {
                    System.out.println("[MyThingsDataManager.resolve] Did not find thing: id: " +
                            remoteThings.get(i).getId());
                    newThings.add(nextRemoteThing);
                }
            }
        }

        // Add remaining local contents to elastic search
        Observable<ArrayList<Thing>> observable = new Observable<>();
        observable.addObserver(new IObserver<ArrayList<Thing>>() {
            @Override
            public void update(ArrayList<Thing> data) {
                System.out.println("[MyThingsDataManager.resolve.update] Data:");
                printThingList(data);

                myThings.addAll(data);

                System.out.println("[MyThingsDataManager.resolve.update] Things:");
                printThingList(myThings);

                // saveToFile();

                thingsObservable.setData(myThings);
            }
        });

        Thing[] newThingsArray = new Thing[ newThings.size() ];
        newThingsArray = newThings.toArray(newThingsArray);

        ThrowawayElasticSearchController.AddThingTask addThingTask =
                new ThrowawayElasticSearchController.AddThingTask(observable);
        addThingTask.execute(newThingsArray);

        // TODO: Iterate through local things to add things which were not successfully added to elastic search
        // ...

        // Delete zombie contents
        for (Thing deletedThing : zombieThings) {

            // TODO: Remove deleted things from elastic search
            // ...

            // zombieThings.remove(deletedThing);
        }
    }

    private void loadFromFile() {

        // TODO: Remove; test
        System.out.println("[MyThingsDataManager.loadFromFile]");

        ArrayList<Thing> tempThings = null;

        try {

            // Initialize BufferedReader using stream from the given file
            FileInputStream stream = Ctx.get().openFileInput(FILENAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            Gson gson = new Gson();

            // Taken from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html
            // Parse the Json stored in the file
            Type listType = new TypeToken<ArrayList<Thing>>() {}.getType();

            tempThings = gson.fromJson(reader, listType);

            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (tempThings != null) {
            myThings.clear();
            myThings.addAll(tempThings);
        }
    }

    private void saveToFile() {

        // TODO: Remove; test
        System.out.println("[MyThingsDataManager.saveToFile]");

        ArrayList<Thing> tempThings = new ArrayList<>();
        for (Thing thing : myThings) {
            Thing newThing = new Thing(thing);
            tempThings.add(newThing);
        }

        try {

            // Initialize BufferedWriter using stream from the given file
            FileOutputStream stream = Ctx.get().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));

            // Write the Json to the given file
            Gson gson = new Gson();

            // tempThings.addAll(myThings);

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
