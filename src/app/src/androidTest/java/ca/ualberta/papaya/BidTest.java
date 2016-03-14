package ca.ualberta.papaya;

import android.test.ActivityInstrumentationTestCase2;

import ca.ualberta.papaya.exceptions.BidNegativeException;
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

    /*
    Use Case: US 05.01.01 - Bid
     */
    public void testNewBid(){

        Thing thing = new Thing(new User());
        User borrower = new User();

        Bid bid = new Bid(thing, borrower, 800);

        assertEquals(800, bid.getAmount());
        assertEquals(Bid.Per.FLAT, bid.getPer());
        assertEquals("8.00", bid.valueOf());
        assertEquals("$8.00", bid.toString());

    }

    /*
    Tests making a new Bid with value 0
     */
    public void testZeroBid(){
        Bid bid = new Bid(new Thing(new User()), new User(), 0);
        assertEquals(0, bid.getAmount());
        assertEquals("0.00", bid.valueOf());
        assertEquals("$0.00", bid.toString());
    }

    /*
    Tests placing a Bid with negative value
     */
    public void testNegativeBid(){
        try {
            Bid bid = new Bid(new Thing(new User()), new User(), -20);
            fail();
        } catch (BidNegativeException e){
            // ok!
        }

    }

    /*
    Tests title of Thing in a new Bid
     */
    public void testBidThing(){

        Thing thing = new Thing(new User());

        thing.setTitle("Fine Art");

        Bid bid = new Bid(thing, new User(), 200);

        assertNotNull(bid.getThing());
        assertEquals("Fine Art", bid.getThing().getTitle());
    }

    /*
    Tests name of (Owner of Thing) of Bid
     */
    public void testBidOwner(){
        User owner = new User();
        owner.setFirstName("Bonnie").setLastName("Jones");

        Thing thing = new Thing(owner);

        Bid bid = new Bid(thing, new User(), 900);

        assertNotNull(bid.getThing());
        assertNotNull(bid.getThing().getOwner());
        assertEquals("Bonnie Jones", bid.getThing().getOwner().getFullName());

    }

    /*
     */
    public void testBidPer(){
        Bid bid = new Bid(new Thing(new User()), new User(), 100);

        bid.setPer(Bid.Per.FLAT);
        assertEquals(Bid.Per.FLAT, bid.getPer());

        bid.setPer(Bid.Per.HOUR);
        assertEquals(Bid.Per.HOUR, bid.getPer());

        bid.setPer(Bid.Per.DAY);
        assertEquals(Bid.Per.DAY, bid.getPer());

        bid.setPer(Bid.Per.WEEK);
        assertEquals(Bid.Per.WEEK, bid.getPer());

        bid.setPer(Bid.Per.MONTH);
        assertEquals(Bid.Per.MONTH, bid.getPer());

        try {
            bid.setPer(null);
            fail();
        } catch (NullPointerException e){
            // ok!
        }

    }

    /*
    Use Case: 05.03.01 - BidNotification
     */
    public void testBidNotification() {
        User owner = new User();
        Thing thing1 = new Thing(owner);

        User borrower = new User();
        Bid bid = new Bid(thing1, borrower, 800);

        try {
            thing1.placeBid(bid);
        } catch (ThingUnavailableException e) {
            fail();
        }

        assertTrue(owner.notifiedOfBid(bid));

        assertTrue(false);
    }

    /*
    Use Case: 05.08.01 - BidAcceptNotification
     */
    public void testBidAcceptionNotification() {
        User owner = new User();
        Thing thing1 = new Thing(owner);

        User borrower = new User();
        Bid bid = new Bid(thing1, borrower, 800);

        try {
            thing1.placeBid(bid);
        } catch (ThingUnavailableException e) {
            fail();
        }

        owner.acceptBid(thing1, bid);
        assertTrue(borrower.notifiedOfAcceptedBid(bid));

        assertTrue(false);
    }

    /*
    Use Case: 05.09.01 - BidDeclineNotification
     */
    public void testBidDeclineNotification(){
        User owner = new User();
        Thing thing1 = new Thing(owner);

        User borrower = new User();
        Bid bid = new Bid(thing1, borrower, 800);

        try {
            thing1.placeBid(bid);
        } catch (ThingUnavailableException e) {
            fail();
        }

        owner.declineBid(thing, bid);
        assertTrue(borrower.notifiedOfDeclinedBid(bid));

        assertTrue(false);
    }

}
