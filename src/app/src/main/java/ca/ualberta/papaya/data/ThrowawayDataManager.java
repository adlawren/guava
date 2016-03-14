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

import ca.ualberta.papaya.interfaces.IDataManager;
import ca.ualberta.papaya.interfaces.IObserver;
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
 * A singleton that contains all Users and Things data
 *
 */
public class ThrowawayDataManager implements IDataManager {

    private static final String USER_FILENAME = "papaya.user.sav";
    private static final String THINGS_FILENAME = "papaya.things.sav";

    private static Observable<User> currentUserObservable = null;

    @Override
    public User getCurrentUser(Context context) {
        if (currentUserObservable.getData() == null) {
            loadUserFromFile(context);
        }

        return currentUserObservable.getData() == null ? new User() :
                currentUserObservable.getData();
    }

    @Override
    public void setCurrentUser(Context context, User user) {
        currentUserObservable.setData(user);
        saveUserToFile(context);
    }

    private static Observable<ArrayList<Thing>> currentUserThingsObservable = null;

    @Override
    public ArrayList<Thing> getCurrentUserThings(Context context) {
        if (currentUserThingsObservable.getData() == null) {
            loadThingsFromFile(context);
        }

        return currentUserThingsObservable.getData() == null ? new ArrayList<Thing>() :
                currentUserThingsObservable.getData();
    }

    @Override
    public void setCurrentUserThings(Context context, ArrayList<Thing> newThings) {
        currentUserThingsObservable.setData(newThings);
        saveThingsToFile(context);
    }

    private void loadUserFromFile(Context context) {
        User user;
        try {

            // Initialize BufferedReader using stream from the given file
            FileInputStream stream = context.openFileInput(USER_FILENAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            Gson gson = new Gson();

            // Taken from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html
            // Parse the Json stored in the file
            user = gson.fromJson(reader, User.class);

            stream.close();

            currentUserObservable.setData(user);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUserToFile(Context context) {
        try {

            // Initialize BufferedWriter using stream from the given file
            FileOutputStream stream = context.openFileOutput(USER_FILENAME, Context.MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));

            // Write the Json to the given file
            Gson gson = new Gson();
            gson.toJson(currentUserObservable.getData(), writer);
            writer.flush();

            stream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void loadThingsFromFile(Context context) {
        ArrayList<Thing> things;
        try {

            // Initialize BufferedReader using stream from the given file
            FileInputStream stream = context.openFileInput(THINGS_FILENAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            Gson gson = new Gson();

            // Taken from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html
            // Parse the Json stored in the file
            Type listType = new TypeToken<ArrayList<Thing>>() {}.getType();
            things = gson.fromJson(reader, listType);

            stream.close();

            currentUserThingsObservable.setData(things);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveThingsToFile(Context context) {
        try {

            // Initialize BufferedWriter using stream from the given file
            FileOutputStream stream = context.openFileOutput(THINGS_FILENAME, Context.MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));

            // Write the Json to the given file
            Gson gson = new Gson();
            gson.toJson(currentUserThingsObservable.getData(), writer);
            writer.flush();

            stream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public ArrayList<Thing> getNonCurrentUserThings() {

        // TODO: Get actual data
        User owner = new User();
        owner.setFirstName("Bob");
        owner.setLastName("Jones");
        owner.setEmail("bjones@ualberta.ca");

        ArrayList<Thing> things = new ArrayList<>();
        for (int i = 0; i < 15; ++i) {
            Thing thing = new Thing(owner);
            thing.setOwner(owner); // ???
            thing.setTitle("Thing " + (i + 1));
            thing.setDescription("The description of Thing " + (i + 1));

            things.add(thing);
        }

        return things;
    }

    private static ThrowawayDataManager ourInstance = new ThrowawayDataManager();

    public static ThrowawayDataManager getInstance() {
        return ourInstance;
    }

    private ThrowawayDataManager() {
        currentUserObservable = new Observable<>(null);
        currentUserThingsObservable = new Observable<>(null);
    }
}
