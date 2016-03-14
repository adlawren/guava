package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import ca.ualberta.papaya.ThingSearchActivity;
import ca.ualberta.papaya.data.ThrowawayDataManager;
import ca.ualberta.papaya.models.Thing;

/**
 * Created by adlawren on 10/03/16.
 */
public class ThingSearchController {
    private static ThingSearchController ourInstance = new ThingSearchController();

    public static ThingSearchController getInstance() {
        return ourInstance;
    }

    private ThingSearchController() {
    }

    // TODO: Fix
    private class SearchOnClickListener implements View.OnClickListener {

        private Context context;

        private EditText keywordsEditText;
        private RecyclerView recyclerView;

        private ArrayList<Thing> thingList;

        public SearchOnClickListener(Context initialContext, EditText initialKeywordsEditText,
                                     RecyclerView initialRecyclerView,
                                     ArrayList<Thing> initialThingList) {
            context = initialContext;

            keywordsEditText = initialKeywordsEditText;
            recyclerView = initialRecyclerView;

            thingList = initialThingList;
        }

        @Override
        public void onClick(View view) {
            String keywords = keywordsEditText.getText().toString();

            thingList.clear();
            for (Thing thing : ThrowawayDataManager.getInstance().getNonCurrentUserThings()) {
                if (thing.getDescription().contains(keywords)) {
                    thingList.add(thing);
                }
            }

            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    public SearchOnClickListener getSearchOnClickListener(Context initialContext,
                                                          EditText initialKeywordsEditText,
                                                          RecyclerView initialRecyclerView,
                                                          ArrayList<Thing> initialThingList) {
        return new SearchOnClickListener(initialContext, initialKeywordsEditText,
                initialRecyclerView, initialThingList);
    }
}
