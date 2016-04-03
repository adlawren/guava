package ca.ualberta.papaya.controllers;

import android.os.AsyncTask;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.ualberta.papaya.models.Bid;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.Observable;
import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by adlawren on 23/03/16.
 */
public class ThrowawayElasticSearchController {
    public static final String INDEX = "papaya";
    private static JestDroidClient client = null;

    private static void verifyConfiguration() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder(
                    "http://cmput301.softwareprocess.es:8080/");
                    //"http://adlawren-papayatest.rhcloud.com/");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

    public static class AddThingTask extends AsyncTask<Thing,Void,ArrayList<Thing>> {

        private Observable<ArrayList<Thing>> observable;

        public AddThingTask(Observable<ArrayList<Thing>> initialObservable) {
            observable = initialObservable;
        }

        @Override
        protected ArrayList<Thing> doInBackground(Thing... things) {
            verifyConfiguration();

            ArrayList<Thing> added = new ArrayList<>();
            for (Thing toAdd : things) {
                Index index = new Index.Builder(toAdd).index(INDEX).type("thing").build();
                try {
                    DocumentResult execute = client.execute(index);
                    if (execute.isSucceeded()) {
                        toAdd.setId(execute.getId());
                        added.add(toAdd);
                    } else {
                        System.err.println("[ThrowawayElasticSearchController.AddThingTask] " +
                                "Client execution failed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return added;
        }

        @Override
        protected void onPostExecute(ArrayList<Thing> addedThings) {
            super.onPostExecute(addedThings);
            observable.setData(addedThings);
        }
    }

    public static class GetThingTask extends AsyncTask<String,Void,ArrayList<Thing>> {

        private Observable<ArrayList<Thing>> observable;

        public GetThingTask(Observable<ArrayList<Thing>> initialObservable) {
            observable = initialObservable;
        }

        @Override
        protected ArrayList<Thing> doInBackground(String... ids) {
            verifyConfiguration();

            ArrayList<Thing> things = new ArrayList<>();
            for (String nextId : ids) {
                Get get = new Get.Builder(INDEX, nextId).type("thing").build();
                try {
                    JestResult getResult = client.execute(get);
                    if (getResult.isSucceeded()) {
                        things.add(getResult.getSourceAsObject(Thing.class));
                    } else {
                        System.err.println("[ThrowawayElasticSearchController.GetThingTask] " +
                                "Client execution failed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return things;
        }

        @Override
        public void onPostExecute(ArrayList<Thing> things) {
            super.onPostExecute(things);
            observable.setData(things);
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

            Search search = new Search.Builder(params[0]).addIndex(INDEX).addType("thing").build();
            try {
                SearchResult seachResult = client.execute(search);
                if (seachResult.isSucceeded()) {
                    List<Thing> list = seachResult.getSourceAsObjectList(Thing.class);
                    things.addAll(list);
                } else {
                    System.err.println("[ThrowawayElasticSearchController.SearchUserTask] " +
                            "Client execution failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return things;
        }

        @Override
        public void onPostExecute(ArrayList<Thing> things) {
            super.onPostExecute(things);
            observable.setData(things);
        }
    }

    public static class UpdateThingTask extends AsyncTask<Thing,Void,ArrayList<Thing>> {

        private Observable<ArrayList<Thing>> observable;

        public UpdateThingTask(Observable<ArrayList<Thing>> initialObservable) {
            observable = initialObservable;
        }

        @Override
        protected ArrayList<Thing> doInBackground(Thing... things) {
            verifyConfiguration();

            ArrayList<Thing> updated = new ArrayList<>();
            for (Thing toUpdate : things) {
                Index index = new Index.Builder(toUpdate).index(INDEX).id(toUpdate.getId()).type("thing").build();
                try {
                    DocumentResult execute = client.execute(index);
                    if (execute.isSucceeded()) {
                        updated.add(toUpdate);
                    } else {
                        System.err.println("[ThrowawayElasticSearchController.UpdateThingTask] " +
                                "Client execution failed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return updated;
        }

        @Override
        protected void onPostExecute(ArrayList<Thing> updatedThings) {
            super.onPostExecute(updatedThings);
            observable.setData(updatedThings);
        }
    }

    public static class DeleteThingTask extends AsyncTask<Thing,Void,ArrayList<Thing>> {

        private Observable<ArrayList<Thing>> observable;

        public DeleteThingTask(Observable<ArrayList<Thing>> initialObservable) {
            observable = initialObservable;
        }

        @Override
        protected ArrayList<Thing> doInBackground(Thing... things) {
            verifyConfiguration();

            ArrayList<Thing> deleted = new ArrayList<>();
            for (Thing toDelete : things) {
                try {
                    Delete delete = new Delete.Builder(toDelete.getId()).index(INDEX)
                            .type("thing").build();
                    JestResult result = client.execute(delete);
                    if (!result.isSucceeded()) {
                        System.err.println("[ThrowawayElasticSearchController.DeleteThingTask] " +
                                "Client execution failed");
                    } else {
                        deleted.add(toDelete);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            return deleted;
        }

        @Override
        public void onPostExecute(ArrayList<Thing> deleted) {
            super.onPostExecute(deleted);
            observable.setData(deleted);
        }
    }

    // TODO: Test
    public static class GetBidTask extends AsyncTask<String,Void,ArrayList<Bid>> {

        private Observable<ArrayList<Bid>> observable;

        public GetBidTask(Observable<ArrayList<Bid>> initialObservable) {
            observable = initialObservable;
        }

        @Override
        protected ArrayList<Bid> doInBackground(String... ids) {
            verifyConfiguration();

            ArrayList<Bid> bids = new ArrayList<>();
            for (String nextId : ids) {
                Get get = new Get.Builder(INDEX, nextId).type("thing").build();
                try {
                    JestResult getResult = client.execute(get);
                    if (getResult.isSucceeded()) {
                        bids.add(getResult.getSourceAsObject(Bid.class));
                    } else {
                        System.err.println("[ThrowawayElasticSearchController.SearchUserTask] " +
                                "Client execution failed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return bids;
        }

        @Override
        public void onPostExecute(ArrayList<Bid> bids) {
            super.onPostExecute(bids);
            observable.setData(bids);
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

            Search search = new Search.Builder(params[0]).addIndex(INDEX).addType("user").build();
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

            Index index = new Index.Builder(users[0]).index(INDEX).type("user").build();
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