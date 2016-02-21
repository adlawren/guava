package ca.ualberta.papaya;

import android.test.ActivityInstrumentationTestCase2;

import ca.ualberta.papaya.exceptions.ThingUnavailableException;
import ca.ualberta.papaya.models.Bid;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;

/**
 * Created by martin on 10/02/16.
 */
public class ThingTest extends ActivityInstrumentationTestCase2 {

    private User owner;
    private User borrower;
    private User borrower2;

    private final String title = "Coffee Maker";
    private final String description = "Makes great coffee.\nOnce served the queen of England.";

    public ThingTest() {
        super(ThingListActivity.class);
    }

    @Override
    public void setUp(){
        owner = new User();
        owner.setFirstName("Sarah").setLastName("Conner");

        borrower = new User();
        borrower.setFirstName("Arnold").setLastName("Schwarzenegger");

        borrower2 = new User();
        borrower2.setFirstName("Alex").setLastName("Jones");

    }

    @Override
    public void tearDown(){
        // clean up if needed once on live DB.
    }

    /*
    Use Case: US 01.01.01 - AddItem
     */
    public void testNewThing() {

        Thing thing = new Thing(owner);
        thing.setTitle(title).setDescription(description);

        assertEquals(Thing.Status.AVAILABLE, thing.getStatus());
        assertEquals(title, thing.getTitle());
        assertEquals(description, thing.getDescription());
        assertEquals(owner.getName(), thing.getOwnerName());

    }

    public void testPlaceBids(){

        Thing thing = new Thing(owner);
        thing.setTitle(title).setDescription(description);

        Bid bid1 = new Bid(thing, borrower, 800);
        Bid bid2 = new Bid(thing, borrower2, 1000);

        try {
            thing.placeBid(bid1);
            thing.placeBid(bid2);
        } catch (ThingUnavailableException e){
            fail();
        }

        boolean found = false;
        for (Bid b : thing.getBids()) {
            if (b.getId() == bid1.getId()){
                if(found){
                    fail();
                }
                found = true;
            }
        }

        assertEquals(true, found);

        assertEquals(2, thing.getBids().size());

    }

    public void testPlaceBidUnavailable(){
        Thing thing = new Thing(new User());

        try {
            Bid bid = new Bid(thing, new User(), 10000);
            thing.placeBid(bid);
            thing.acceptBid(bid);
        } catch (ThingUnavailableException e) {
            fail();
        }

        try {
            Bid bid2 = new Bid(thing, new User(), 15000);
            thing.placeBid(bid2);
        } catch (ThingUnavailableException e) {
            // ok!
        }
    }

    public void testAcceptBidUnavailable(){
        Thing thing = new Thing(new User());

        try {
            Bid bid = new Bid(thing, new User(), 10000);
            thing.placeBid(bid);
            thing.acceptBid(bid);
        } catch (ThingUnavailableException e) {
            fail();
        }

        try {
            Bid bid2 = new Bid(thing, new User(), 15000);
            thing.placeBid(bid2);
            thing.acceptBid(bid2);
            fail();
        } catch (ThingUnavailableException e) {
            // ok!
        }
    }




}
