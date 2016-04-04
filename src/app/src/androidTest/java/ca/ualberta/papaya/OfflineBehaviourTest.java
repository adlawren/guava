package ca.ualberta.papaya;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.test.ActivityInstrumentationTestCase2;

import java.security.acl.Owner;

import ca.ualberta.papaya.models.Bid;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;

/**
 * created by Vishruth on 02/21/16
 */
public class OfflineBehaviourTest extends ActivityInstrumentationTestCase2 {

    public OfflineBehaviourTest() {
        super(ca.ualberta.papaya.ThingListActivity.class);
    }

    /*
    Use Case: US 08.01.01 - AddOffline
     */
    public void testAddOffline() {
        ConnectivityManager cm =
                (ConnectivityManager) getInstrumentation().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        assertEquals(isConnected, false);

        String title = "title";
        String description = "description";

        int count = getItemCount();

        User owner = new User();
        Thing thing = new Thing(owner);
        thing.setTitle(title).setDescription(description);

        assertEquals(Thing.Status.AVAILABLE, thing.getStatus());
        assertEquals(title, thing.getTitle());
        assertEquals(description, thing.getDescription());
        assertEquals(owner.getName(), thing.getOwner());

        setConnectionOnline();
        isConnected = activeNetwork!= null &&
                activeNetwork.isConnectedOrConnecting();
        assertEquals(isConnected, true);
        assertEquals(getItemCount(), count + 1);
    }
}