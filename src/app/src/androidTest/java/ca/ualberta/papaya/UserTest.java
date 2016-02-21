package ca.ualberta.papaya;

import android.test.ActivityInstrumentationTestCase2;

import java.util.List;

import ca.ualberta.papaya.exceptions.ThingUnavailableException;
import ca.ualberta.papaya.exceptions.UserInvalidPostalException;
import ca.ualberta.papaya.fixtures.Country;
import ca.ualberta.papaya.fixtures.Province;
import ca.ualberta.papaya.models.Bid;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;

/**
 * Created by martin on 10/02/16.
 */
public class UserTest extends ActivityInstrumentationTestCase2 {

    public UserTest() {
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
    Use Case: US 03.01.01 - Profile
     */
    public void testNewUser(){
        User user = new User();

        user.setFirstName("Daddy").setLastName("Cool");
        assertEquals("Daddy Cool", user.getFullName());

        user.setEmail("abc@example.com");
        assertEquals("abc@example.com", user.getEmail());

        user.setAddress1("123 Fake Street");
        assertEquals("123 Fake Street", user.getAddress1());

        user.setAddress2("Penthouse Suite");
        assertEquals("Penthouse Suite", user.getAddress2());

        user.setCountry(Country.CANADA);
        assertEquals(Country.CANADA, user.getCountry());

        user.setProvince(Province.ALBERTA);
        assertEquals(Province.ALBERTA, user.getProvince());

        try {
            user.setPostal("T8A 5H8");
            assertEquals("T8A 5H8", user.getPostal());
        } catch (UserInvalidPostalException e){
            fail();
        }

        user.setCountry(Country.CANADA);
        assertEquals(Country.CANADA, user.getCountry());

        user.setProvince(Province.ONTARIO);
        assertEquals(Province.ONTARIO, user.getProvince());
    }

    /*
    Use Case: US 03.02.01 - EditContactInfo
     */
    public void testEditUser(){
        User user = new User();

        user.setFirstName("Daddy").setLastName("Cool");
        assertEquals("Daddy Cool", user.getFullName());

        user.setEmail("abc@example.com");
        assertEquals("abc@example.com", user.getEmail());

        user.setAddress1("123 Fake Street");
        assertEquals("123 Fake Street", user.getAddress1());

        user.setAddress2("Penthouse Suite");
        assertEquals("Penthouse Suite", user.getAddress2());

        user.setCountry(Country.CANADA);
        assertEquals(Country.CANADA, user.getCountry());

        user.setProvince(Province.ALBERTA);
        assertEquals(Province.ALBERTA, user.getProvince());

        try {
            user.setPostal("T8A 5H8");
            assertEquals("T8A 5H8", user.getPostal());
        } catch (UserInvalidPostalException e){
            fail();
        }

        user.setEmail("def@example.com");
        assertEquals("def@example.com", user.getEmail());

        user.setAddress1("1234 Fake Street");
        assertEquals("1234 Fake Street", user.getAddress1());

        user.setAddress2("Penthouse Suite 2");
        assertEquals("Penthouse Suite 2", user.getAddress2());

        try {
            user.setPostal("T8A 5H9");
            assertEquals("T8A 5H9", user.getPostal());
        } catch (UserInvalidPostalException e){
            fail();
        }
    }

    /*
    Tests for bad entry to Postal code
     */
    public void testBadPostal(){
        User user = new User();
        try {
            user.setPostal("BAD POSTAL CODE");
            fail();
        } catch (UserInvalidPostalException e) {
            // ok!
        }
    }

    /*
    Use Case: US 01.02.01 - ViewingMyItems
     */
    public void testUserThings(){

        User owner = new User();
        Thing thing1 = new Thing(owner);
        Thing thing2 = new Thing(owner);

        List<Thing> things = owner.getThings();

        assertEquals(2, owner.getThings().size());

    }

    /*
    Use Case: US 05.02.01 - ViewBids
     */
    public void testUserBids(){

        User owner = new User();
        Thing thing = new Thing(owner);

        User borrower = new User();

        assertEquals(0, borrower.getBids().size());

        Bid bid = new Bid(thing, borrower, 800);

        try {
            thing.placeBid(bid);
        } catch (ThingUnavailableException e) {
            fail();
        }

        assertEquals(1, borrower.getBids().size());
        assertEquals(thing, bid.getThing());
        assertEquals(owner, bid.getOwner());
        assertEquals(800, bid.getAmount());

    }

    /*
    Use Case: US 05.04.01 - ViewBiddedItems
     */
    public void testGetBiddedItems(){
        User owner = new User();
        Thing thing1 = new Thing(owner);
        Thing thing2 = new Thing(owner);

        List<Thing> things = owner.getThings();

        assertEquals(2, owner.getThings().size());

        User borrower = new User();
        Bid bid = new Bid(thing1, borrower, 800);

        try {
            thing.placeBid(bid);
        } catch (ThingUnavailableException e) {
            fail();
        }

        assertEquals(1, owner.getBiddedItems().size());
        assertEquals(2, owner.getThings().size());
    }

    /*
    Use Case: US 05.05.01 - ListBids
     */
    public void testGetBids(){
        User owner = new User();
        Thing thing1 = new Thing(owner);

        User borrower = new User();
        Bid bid = new Bid(thing1, borrower, 800);

        try {
            thing.placeBid(bid);
        } catch (ThingUnavailableException e) {
            fail();
        }

        List<Bid> bids = owner.getBids(thing1);

        assertEquals(1, bids.size());
        assertEquals(bids[0].getThing(), thing1);
        assertEquals(bids[0].getAmount(), 800);

    }
}
