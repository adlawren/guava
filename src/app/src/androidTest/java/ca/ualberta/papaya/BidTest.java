package ca.ualberta.papaya;

import android.test.ActivityInstrumentationTestCase2;

import ca.ualberta.papaya.exceptions.ThingUnavailableException;
import ca.ualberta.papaya.models.Bid;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;

/**
 * Created by martin on 10/02/16.
 */
public class BidTest extends ActivityInstrumentationTestCase2 {

    public BidTest(){
        super(ThingListActivity.class);
    }

    @Override
    public void setUp(){

    }

    @Override
    public void tearDown(){
        // clean up if needed once on live DB.
    }

    public void testNewBid(){

        Thing thing = new Thing(new User());
        User borrower = new User();

        Bid bid = new Bid(thing, borrower, 800);

        assertEquals(bid.getAmount(), 800);
        assertEquals(bid.getPer(), Bid.Per.FLAT);
        assertEquals(bid.valueOf(), "8.00");
        assertEquals(bid.toString(), "$8.00");

    }


    public void testBidThing(){

        Thing thing = new Thing(new User());

        thing.setTitle("Fine Art");

        Bid bid = new Bid(thing, new User(), 200);

        assertEquals(
                bid.getThing().getTitle(),
                "Fine Art");
    }

    public void testBidOwner(){
        User owner = new User();
        owner.setFirstName("Bonnie").setLastName("Jones");

        Thing thing = new Thing(owner);

        Bid bid = new Bid(thing, new User(), 900);

        assertEquals(
                bid.getThing().getOwner().getFullName(),
                "Bonnie Jones");

    }



}
