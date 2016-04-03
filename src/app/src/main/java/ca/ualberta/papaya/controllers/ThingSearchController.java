package ca.ualberta.papaya.controllers;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.papaya.R;
import ca.ualberta.papaya.ThingSearchActivity;
import ca.ualberta.papaya.data.ThrowawayDataManager;
import ca.ualberta.papaya.interfaces.IObserver;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.util.LocalUser;
import ca.ualberta.papaya.util.Observer;

/**
 * Created by adlawren on 10/03/16.
 *
 * Main controller for Searching Thing objects. It is a singleton that contains the instance and
 * methods for the ThingSearchActivity
 *
 * @see ca.ualberta.papaya.ThingSearchActivity
 */

public class ThingSearchController {
    private static ThingSearchController ourInstance = new ThingSearchController();

    public static ThingSearchController getInstance() {
        return ourInstance;
    }

    private ThingSearchController() {
    }

    // button for searching inputted keywords
    // TODO: Fix
    private class SearchOnClickListener implements MenuItem.OnMenuItemClickListener {

        private Context context;

        private EditText keywordsEditText;
        private RecyclerView recyclerView;

        private ArrayList<Thing> thingList;

        private Observer<ArrayList<Thing>> thingListObserver = null;

        public SearchOnClickListener(Context initialContext, EditText initialKeywordsEditText,
                                     RecyclerView initialRecyclerView,
                                     ArrayList<Thing> initialThingList,
                                     Observer<ArrayList<Thing>> initialThingListObserver) {
            context = initialContext;

            keywordsEditText = initialKeywordsEditText;
            recyclerView = initialRecyclerView;

            thingList = initialThingList;

            thingListObserver = initialThingListObserver;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            // TODO: Fix? Not necessary but it might be a better solution; at present the control logic is within the activity
//            Thing.search(new Observer<List<Thing>>() {
//                @Override
//                public void update(final List<Thing> data) {
//                    ((Activity) context).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            // EditText keywordsEditText = (EditText) findViewById(R.id.keywords);
//
//                            thingList.clear();
//                            for (Thing thing : data) {
//                                if (thing.getDescription().contains(keywordsEditText.getText())) {
//                                    thingList.add(thing);
//                                }
//                            }
//
//                            // RecyclerView recyclerView = (RecyclerView) findViewById(R.id.thing_list);
//                            recyclerView.getAdapter().notifyDataSetChanged();
//                        }
//                    });
//                    thingList.clear();
//                    for (Thing thing : data) {
//                        if (thing.getDescription().contains(keywordsEditText.getText())) {
//                            thingList.add(thing);
//                        }
//                    }
//
//                    recyclerView.getAdapter().notifyDataSetChanged();
//                }
//            }, Thing.class, "{ \"size\" : \"100\", \"query\" : { \"bool\" : { \"must_not\" : " +
//                    "[ { \"match\" : { \"ownerId\" : \"" + LocalUser.getId() + "\" } } ], \"must\" : " +
//                    "[ { \"match\" : { \"status\" : \"AVAILABLE\" } } ] } } }");

            // Alternatively:
            Thing.search(thingListObserver, Thing.class, "{ \"size\" : \"500\", \"query\" : " +
                    "{ \"bool\" : { \"must_not\" : [ { \"match\" : { \"ownerId\" : \"" +
                    LocalUser.getId() + "\" } } ], \"must\" : [ { \"match\" : " +
                    "{ \"status\" : \"AVAILABLE\" } } ] } } }");

            return true;
        }
    }

    // return the onClickListener for search
    public SearchOnClickListener getSearchOnClickListener(Context initialContext,
                                                          EditText initialKeywordsEditText,
                                                          RecyclerView initialRecyclerView,
                                                          ArrayList<Thing> initialThingList,
                                                          Observer<ArrayList<Thing>> initialThingListObserver) {
        return new SearchOnClickListener(initialContext, initialKeywordsEditText,
                initialRecyclerView, initialThingList, initialThingListObserver);
    }
}
