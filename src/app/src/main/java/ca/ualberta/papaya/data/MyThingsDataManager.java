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
import ca.ualberta.papaya.util.Observer;

/**
 * Created by adlawren on 28/03/16.
 */

abstract class MyThingsObserver implements IObserver<ArrayList<Thing>> {
    protected ArrayList<Thing> myThings = null;

    public void setThings(ArrayList<Thing> newThings) {
        myThings = newThings;
    }

    protected Observable<ArrayList<Thing>> observable = null;

    public void setObservable(Observable<ArrayList<Thing>> newObservable) {
        observable = newObservable;
    }

    public abstract void update();
}

class GetThingsObserver implements IObserver<ArrayList<Thing>> {
    private ArrayList<Thing> myThings = null;

//    public void GetThingsObserver(ArrayList<Thing> initialMyThings) {
//        myThings = initialMyThings;
//    }
    public void setThings(ArrayList<Thing> newThings) {
        myThings = newThings;
    }

    private Observable<ArrayList<Thing>> observable = null;

    public void setObservable(Observable<ArrayList<Thing>> newObservable) {
        observable = newObservable;
    }

    @Override
    public void update(ArrayList<Thing> data) {
        if (observable == null) {
            System.err.println("[GetThingsObserver.update] ERROR: Observable not initialized");
            return;
        }

        if (data.size() > 0) {
            if (myThings.size() > 0) {
                MyThingsDataManager.getInstance().resolve(data);
            } else {
                myThings.addAll(data);
            }

            observable.setData(myThings);
        }
    }
}

class UpdateThingsObserver implements IObserver<ArrayList<Thing>> {
    private ArrayList<Thing> myThings = null;

    public void setThings(ArrayList<Thing> newThings) {
        myThings = newThings;
    }

    private Observable<ArrayList<Thing>> observable = null;

    public void setObservable(Observable<ArrayList<Thing>> newObservable) {
        observable = newObservable;
    }

    @Override
    public void update(ArrayList<Thing> data) {
        if (observable == null) {
            System.err.println("[UpdateThingsObserver.update] ERROR: Observable not initialized");
            return;
        }

        System.out.println("[MyThingsDataManager.update.update] Thing list contents: ");
        for (Thing localThing : myThings) {
            System.out.println("Thing id: " + localThing.getId() +
                    ", Thing title: " + localThing.getTitle() +
                    ", Thing description: " + localThing.getDescription());
        }

        MyThingsDataManager.getInstance().resolve(data);
    }
}

public class MyThingsDataManager {
    private static MyThingsDataManager ourInstance = new MyThingsDataManager();

    public static MyThingsDataManager getInstance() {
        return ourInstance;
    }

    private MyThingsDataManager() {
        myThingsObservable = new Observable<>();
        myThingsObservable.setData(new ArrayList<Thing>());
        if (myThingsObservable.getData() == null) {
            System.err.println("wtf");
        } else {
            System.out.println("...k");
        }
    }

    private static final String FILENAME = "my_things.sav";

    // private static ArrayList<Thing> myThings = new ArrayList<>();
    private static Observable<ArrayList<Thing>> myThingsObservable = new Observable<>(); // null;

    public void getData(final Observable<ArrayList<Thing>> observable) {

        // Get local data
        loadFromFile();

        // Get online data
        Observable<ArrayList<Thing>> thingListObservable = new Observable<>();
//        thingListObservable.addObserver(new IObserver<ArrayList<Thing>>() {
//            @Override
//            public void update(ArrayList<Thing> data) {
//                if (observable == null) {
//                    System.err.println("[GetThingsObserver.update] ERROR: Observable not initialized");
//                    return;
//                }
//
//                if (data.size() > 0) {
//                    if (myThings.size() > 0) {
//                        MyThingsDataManager.getInstance().resolve(data);
//                    } else {
//                        myThings.addAll(data);
//                    }
//
//                    observable.setData(myThings);
//                }
//            }
//        });
        if (myThingsObservable.getData() == null) {
            System.err.println("Can confirm");
        }

        GetThingsObserver getThingsObserver = new GetThingsObserver();
        // getThingsObserver.setThings(myThings);
        getThingsObserver.setThings(myThingsObservable.getData());
        getThingsObserver.setObservable(observable);

        thingListObservable.addObserver(getThingsObserver);

        ThrowawayElasticSearchController.SearchThingTask thingSearchTask =
                new ThrowawayElasticSearchController.SearchThingTask(thingListObservable);

        thingSearchTask.execute("{}");

        // observable.setData(myThings);
        observable.setData(myThingsObservable.getData());
    }

    public void update(Thing thing) {
        // if (myThings.contains(thing)) {
        if (myThingsObservable.getData().contains(thing)) {

            // TODO: See if myThings.get(...) works instead
            // for (Thing nextThing : myThings) {
            for (Thing nextThing : myThingsObservable.getData()) {
                if (nextThing.getId().equals(thing.getId())) {
                    nextThing.setTitle(thing.getTitle());
                    nextThing.setDescription(thing.getDescription());
                    break;
                }
            }
        } else {
            // myThings.add(thing);
            ArrayList<Thing> tempThings = myThingsObservable.getData();
            tempThings.add(thing);

            myThingsObservable.setData(tempThings);
        }

        System.out.println("[MyThingsDataManager.update] Thing list contents:");
        // for (Thing nextThing : myThings) {
        for (Thing nextThing : myThingsObservable.getData()) {
            System.out.println("Thing id: " + nextThing.getId() +
                    ", Thing title: " + nextThing.getTitle() +
                    ", Thing description: " + nextThing.getDescription());
        }

        // Get online data
        Observable<ArrayList<Thing>> thingListObservable = new Observable<>();
//        thingListObservable.addObserver(new IObserver<ArrayList<Thing>>() {
//            @Override
//            public void update(ArrayList<Thing> data) {
//                System.out.println("[MyThingsDataManager.update.update] Thing list contents: ");
//                for (Thing localThing : myThings) {
//                    System.out.println("Thing id: " + localThing.getId() +
//                            ", Thing title: " + localThing.getTitle() +
//                            ", Thing description: " + localThing.getDescription());
//                }
//
//                resolve(data);
//            }
//        });
        UpdateThingsObserver updateThingsObserver = new UpdateThingsObserver();

        // updateThingsObserver.setThings(myThings);
        updateThingsObserver.setThings(myThingsObservable.getData());

        thingListObservable.addObserver(updateThingsObserver);


        ThrowawayElasticSearchController.SearchThingTask thingSearchTask =
                new ThrowawayElasticSearchController.SearchThingTask(thingListObservable);

        thingSearchTask.execute("{}");
    }

    // Used to resolve differences between the local contents and the remote contents
    // private void resolve(ArrayList<Thing> remoteThings) {
    public void resolve(ArrayList<Thing> remoteThings) {
        ArrayList<Thing> things = new ArrayList<>();

        // things.addAll(myThings);
        things.addAll(myThingsObservable.getData());

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

//        myThings.clear();
//        myThings.addAll(things);
        myThingsObservable.setData(things);

        System.out.println("[MyThingsDataManager.resolve] Thing list contents: ");
        // for (Thing thing : myThings) {
        for (Thing thing : myThingsObservable.getData()) {
            System.out.println("Thing id: " + thing.getId() +
                    ", Thing title: " + thing.getTitle() +
                    ", Thing description: " + thing.getDescription());
        }

        saveToFile();
    }

    // private void loadThingsFromFile(Context context) {
    private void loadFromFile() {
        ArrayList<Thing> things;
        try {

            // Initialize BufferedReader using stream from the given file
            // FileInputStream stream = context.openFileInput(FILENAME);
            FileInputStream stream = Ctx.get().openFileInput(FILENAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            Gson gson = new Gson();

            // Taken from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html
            // Parse the Json stored in the file
            Type listType = new TypeToken<ArrayList<Thing>>() {}.getType();
            things = gson.fromJson(reader, listType);

            stream.close();

            // currentUserThingsObservable.setData(things);

//            myThings.clear();
//            myThings.addAll(things);
            myThingsObservable.setData(things);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // private void saveThingsToFile(Context context) {
    private void saveToFile() {
        try {

            // Initialize BufferedWriter using stream from the given file
            // FileOutputStream stream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            FileOutputStream stream = Ctx.get().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));

            // Write the Json to the given file
            Gson gson = new Gson();
            // gson.toJson(currentUserThingsObservable.getData(), writer);

            // gson.toJson(myThings, writer);

            gson.toJson(myThingsObservable.getData(), writer);

            writer.flush();

            stream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

//    private void loadFromFile() {
//        ArrayList<Thing> tempThings = null;
//
//        try {
//
//            // Initialize BufferedReader using stream from the given file
//            FileInputStream stream = Ctx.get().openFileInput(FILENAME);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//
//            Gson gson = new Gson();
//
//            // Taken from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html
//            // Parse the Json stored in the file
//            Type listType = new TypeToken<ArrayList<Thing>>() {}.getType();
//
//            tempThings = gson.fromJson(reader, listType);
//
//            stream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (tempThings != null) {
//            myThings.clear();
//            myThings.addAll(tempThings);
//        }
//    }
//
//    private void saveToFile() {
//        ArrayList<Thing> tempThings = new ArrayList<>();
//
//        try {
//
//            // Initialize BufferedWriter using stream from the given file
//            FileOutputStream stream = Ctx.get().openFileOutput(FILENAME, Context.MODE_PRIVATE);
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
//
//            // Write the Json to the given file
//            Gson gson = new Gson();
//
//            tempThings.addAll(myThings);
//
//            gson.toJson(tempThings, writer);
//            writer.flush();
//
//            stream.close();
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException();
//        } catch (IOException e) {
//            throw new RuntimeException();
//        }
//    }
}
