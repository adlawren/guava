package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

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

    private class SearchOnClickListener implements View.OnClickListener {

        private Context context;
        private EditText keywordsEditText;

        public SearchOnClickListener(Context initialContext, EditText initialKeywordsEditText) {
            context = initialContext;
            keywordsEditText = initialKeywordsEditText;
        }

        @Override
        public void onClick(View view) {
            String keywords = keywordsEditText.getText().toString();

            // TODO: Implement
            System.err.println("TODO: IMPLEMENT; Keywords: " + keywords);
        }
    }

    public SearchOnClickListener getSearchOnClickListener(Context initialContext, EditText initialKeywordsEditText) {
        return new SearchOnClickListener(initialContext, initialKeywordsEditText);
    }
}
