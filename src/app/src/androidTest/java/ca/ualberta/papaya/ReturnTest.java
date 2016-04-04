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
        User user = new User();
        Thing thing = new Thing(user);
        User borrower = new User();
        Bid bid = null;
        try {
            bid = new Bid(thing, borrower, 800);
        }catch(Exception e){
            fail();
        }

        try {
            thing.acceptBid(bid);
        }catch( Exception e){
            fail();
        }

        assertEquals(thing.getStatus(), Thing.Status.BORROWED);

        thing.setAvailable();
        assertEquals(thing.getStatus() , Thing.Status.AVAILABLE);
    }
}