package ca.ualberta.papaya.controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.Observable;
import io.searchbox.client.JestResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by adlawren on 23/03/16.
 */
public class ThrowawayElasticSearchController {
    private static JestDroidClient client = null;

    private static void verifyConfiguration() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder(
                    "http://cmput301.softwareprocess.es:8080/");
                    // "http://adlawren-papayatest.rhcloud.com/");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

    public static class GetThingTask extends AsyncTask<String,Void,Thing> {

        private Observable<Thing> observable;

        public GetThingTask(Observable<Thing> initialObservable) {
            observable = initialObservable;
        }

        @Override
        protected Thing doInBackground(String... params) {
            verifyConfiguration();

            ArrayList<User> users = new ArrayList<>();

            List<SearchResult.Hit<User, Void>> test = null;

            Get get = new Get.Builder("papaya", params[0]).type("thing").build();
            try {
                JestResult getResult = client.execute(get);
                if (getResult.isSucceeded()) {
                    return getResult.getSourceAsObject(Thing.class);
                } else {
                    System.err.println("[ThrowawayElasticSearchController.SearchUserTask] " +
                            "Client execution failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onPostExecute(Thing thing) {
            super.onPostExecute(thing);
            observable.setData(thing);
        }
    }

    public static class SearchThingTask extends AsyncTask<String,Void,ArrayList<Thing>> {

        private Observable<ArrayList<Thing>> observable;

        public SearchThingTask(Observable<ArrayList<Thing>> initialObservable) {
            observable = initialObservable;
        }

        @Override
        protected ArrayList<Thing> doInBackground(String... params) {
            verifyConfiguration();

            ArrayList<Thing> things = new ArrayList<>();

            List<SearchResult.Hit<User, Void>> test = null;

            Search search = new Search.Builder(params[0]).addIndex("papaya").addType("thing").build();
            try {
                SearchResult seachResult = client.execute(search);
                if (seachResult.isSucceeded()) {
                    List<Thing> list = seachResult.getSourceAsObjectList(Thing.class);

                    test =  seachResult.getHits(User.class);

                    things.addAll(list);
                } else {
                    System.err.println("[ThrowawayElasticSearchController.SearchUserTask] " +
                            "Client execution failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (test != null) {
                for (SearchResult.Hit<User, Void> hit : test) {
                    // System.out.println("Next hit thing id: " + hit.source.getId());
                }
            } else {
                System.err.println("Didn't work");
            }

            return things;
        }

        @Override
        public void onPostExecute(ArrayList<Thing> things) {
            super.onPostExecute(things);

//            // TODO: Update; test
//            System.out.println("Thing count: " + things.size());
//            for (Thing thing : things) {
//                System.out.println("Next thing id: " + thing.getId());
//                System.out.println("Next thing description: " + thing.getDescription());
//            }

            observable.setData(things);
        }
    }

    public static class SearchUserTask extends AsyncTask<String,Void,ArrayList<User>> {

        private Observable<ArrayList<User>> observable;

        public SearchUserTask(Observable<ArrayList<User>> initialObservable) {
            observable = initialObservable;
        }

        @Override
        protected ArrayList<User> doInBackground(String... params) {
            verifyConfiguration();

            ArrayList<User> users = new ArrayList<>();

            List<SearchResult.Hit<User, Void>> test = null;

            Search search = new Search.Builder(params[0]).addIndex("papaya").addType("user").build();
            try {
                SearchResult seachResult = client.execute(search);
                if (seachResult.isSucceeded()) {
                    List<User> list = seachResult.getSourceAsObjectList(User.class);

                    test =  seachResult.getHits(User.class);

                    users.addAll(list);
                } else {
                    System.err.println("[ThrowawayElasticSearchController.SearchUserTask] " +
                            "Client execution failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (test != null) {
                for (SearchResult.Hit<User, Void> hit : test) {
                    // System.out.println("Next hit user id: " + hit.source.getId());
                    // System.out.println("Next hit user id: " + hit.source.getTestId());
                }
            } else {
                System.err.println("Didn't work");
            }

            return users;
        }

        @Override
        public void onPostExecute(ArrayList<User> users) {
            super.onPostExecute(users);

            // TODO: Update; test
            System.out.println("User count: " + users.size());
            for (User user : users) {
                System.out.println("Next user id: " + user.getId());
                System.out.println("Next user name: " + user.getName());
            }

            observable.setData(users);
        }
    }

    public static class AddUserTask extends AsyncTask<User,Void,User> {

        private Observable<User> observable;

        public AddUserTask(Observable<User> initialObservable) {
            observable = initialObservable;
        }

        @Override
        protected User doInBackground(User... users) {
            verifyConfiguration();

            if (users.length != 1) {
                System.err.println("[ThrowawayElasticSearchController.AddUserTask] " +
                        "Invalid number of users given");
            }

            Index index = new Index.Builder(users[0]).index("papaya").type("user").build();
            try {
                DocumentResult execute = client.execute(index);
                if (execute.isSucceeded()) {
                    users[0].setId(execute.getId());
                } else {
                    System.err.println("[ThrowawayElasticSearchController.AddUserTask] " +
                            "Client execution failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return users[0];
        }

        // TODO: Use???
        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);

            // TODO: Remove; test
            System.out.println("In post execute, User id: " + user.getId());

            observable.setData(user);
        }
    }
}