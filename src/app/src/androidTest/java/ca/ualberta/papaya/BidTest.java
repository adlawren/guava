package ca.ualberta.papaya;

import android.test.ActivityInstrumentationTestCase2;

import ca.ualberta.papaya.exceptions.BidNegativeException;
import ca.ualberta.papaya.exceptions.ThingUnavailableException;
import ca.ualberta.papaya.interfaces.IObserver;
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

        Bid bid  = null;
        try {
            bid = new Bid(thing, borrower, 800);
        }catch(Exception e){
            fail();
        }
        assertEquals(800, bid.getAmount());
        assertEquals(Bid.Per.FLAT, bid.getPer());
        assertEquals("8.00", bid.valueOf());
        assertEquals("$8.00", bid.toString());

    }

    /*
    Tests making a new Bid with value 0
     */
    public void testZeroBid(){

        Bid bid = null;
        try {
            bid = new Bid(new Thing(new User()), new User(), 0);
        } catch (Exception e){
            fail();
        }
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
        } catch (Exception e){
            //ok
        }

    }

    /*
     */
    public void testBidPer(){
        Bid bid = null;
        try {
            bid = new Bid(new Thing(new User()), new User(), 100);
        } catch(Exception e){
            fail();
        }
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

        Bid bid = null;
        try {
            bid = new Bid(thing1, borrower, 800);
        }catch(Exception e){
            fail();
        }
        try {
            thing1.placeBid(bid);
        } catch (ThingUnavailableException e) {
            fail();
        }

        //assertTrue(owner.notifiedOfBid(bid));

        assertTrue(false);
    }

    /*
    Use Case: 05.08.01 - BidAcceptNotification
     */
    public void testBidAcceptionNotification() {
        User owner = new User();
        Thing thing1 = new Thing(owner);

        User borrower = new User();

        Bid bid = null;
        try {
            bid = new Bid(thing1, borrower, 800);
        }catch(Exception e){
            fail();
        }
        try {
            thing1.placeBid(bid);
        } catch (ThingUnavailableException e) {
            fail();
        }

        //owner.acceptBid(thing1, bid);
        //assertTrue(borrower.notifiedOfAcceptedBid(bid));

        assertTrue(false);
    }

    /*
    Use Case: 05.09.01 - BidDeclineNotification
     */
    public void testBidDeclineNotification(){
        User owner = new User();
        Thing thing1 = new Thing(owner);

        User borrower = new User();

        Bid bid = null;
        try {
            bid = new Bid(thing1, borrower, 800);
        }catch(Exception e){
            fail();
        }
        try {
            thing1.placeBid(bid);
        } catch (ThingUnavailableException e) {
            fail();
        }

        // owner.declineBid(thing1, bid);
        //assertTrue(borrower.notifiedOfDeclinedBid(bid));

        assertTrue(false);
    }

}
