package ca.ualberta.papaya;

import android.test.ActivityInstrumentationTestCase2;

import ca.ualberta.papaya.models.Bid;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;

/**
 * created by Vishruth on 02/21/16
 */
public class ReturnTest extends ActivityInstrumentationTestCase2{

    public ReturnTest(){
        super(ca.ualberta.papaya.ThingDetailActivity.class);
    }

    /*
    Use Case: US 07.01.01 - SetAvailable
     */
    public void testSetAvailable() {
        Thing thing = getCurrentThing();
        assertEquals(thing.getStatus(), Thing.Status.RETURNED);

        thing.setAvailable();
        status = thing.getStatus();
        assertEquals(thing.getStatus() , Thing.Status.AVAILABLE);
    }
}