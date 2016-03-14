package ca.ualberta.papaya;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

import ca.ualberta.papaya.models.Bid;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;

/**
 * created by Vishruth on 02/21/16
 */
public class SearchTest extends ActivityInstrumentationTestCase2 {

    public SearchTest() {
        super(ThingSearchActivity.class);
    }

    /*
    Use Case: US 04.01.01 - KeywordsSearch
     */
    public void testKeywordSearch(){
        User user = new User();
        Thing thing = new Thing(user);
        thing.setTitle("Oven");

        ArrayList<Thing> searchResults = getSearchResults("oven");

        assertEquals(thing.getTitle(), searchResults[0].getTitle());
    }
}
