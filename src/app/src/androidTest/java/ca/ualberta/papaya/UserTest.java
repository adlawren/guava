package ca.ualberta.papaya;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.papaya.exceptions.ThingUnavailableException;
import ca.ualberta.papaya.exceptions.UserInvalidPostalException;
import ca.ualberta.papaya.fixtures.Country;
import ca.ualberta.papaya.fixtures.Province;
import ca.ualberta.papaya.models.Bid;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.Observer;

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

        user.setCountry(Country.CANADA.toString());
        assertEquals(Country.CANADA, user.getCountry());

        user.setProvince(Province.ALBERTA.toString());
        assertEquals(Province.ALBERTA, user.getProvince());

        try {
            user.setPostal("T8A 5H8");
            assertEquals("T8A 5H8", user.getPostal());
        } catch (Exception e){
            fail();
        }

        user.setCountry(Country.CANADA.toString());
        assertEquals(Country.CANADA, user.getCountry());

        user.setProvince(Province.ONTARIO.toString());
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

        user.setCountry(Country.CANADA.toString());
        assertEquals(Country.CANADA, user.getCountry());

        user.setProvince(Province.ALBERTA.toString());
        assertEquals(Province.ALBERTA, user.getProvince());

        try {
            user.setPostal("T8A 5H8");
            assertEquals("T8A 5H8", user.getPostal());
        } catch (Exception e){
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
        } catch (Exception e){
            fail();
        }
    }

    /*
    Use Case: US 03.03.01 - ViewUserContactInfo
     */
    public void testViewUserInfo(){
        User user = new User();
        Thing thing = new Thing(user);

        User owner = new User();

        thing.getOwner(new Observer<User>() {
            @Override
            public void update(User owner) {

            }
        });

        user.setFirstName("Daddy").setLastName("Cool");
        assertEquals(retrivedUser.getFullName(), user.getFullName());

        user.setEmail("abc@example.com");
        assertEquals(retrivedUser.getEmail(), user.getEmail());

        user.setAddress1("123 Fake Street");
        assertEquals(retrivedUser.getAddress1(), user.getAddress1());

        user.setAddress2("Penthouse Suite");
        assertEquals(retrivedUser.getAddress2(), user.getAddress2());

        user.setCountry(Country.CANADA.toString());
        assertEquals(retrivedUser.getCountry(), user.getCountry());

        user.setProvince(Province.ALBERTA.toString());
        assertEquals(retrivedUser.getProvince(), user.getProvince());
    }

    /*
    Tests for bad entry to Postal code
     */
    public void testBadPostal(){
        User user = new User();
        try {
            user.setPostal("BAD POSTAL CODE");
            fail();
        } catch (Exception e) {
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
    Use Case: US 01.03.01 - ViewItem
     */
    public void testUserIndexThing(){
        User owner = new User();
        Thing thing = new Thing(owner);
        String title = "Coffee Maker";
        String description = "Makes great coffee.\nOnce served the queen of England.";
        thing.setTitle(title).setDescription(description);

        List<Thing> things = owner.getThings();

        assertEquals(Thing.Status.AVAILABLE, things.get(0).getStatus());
        assertEquals(title, things.get(0).getTitle());
        assertEquals(description, things.get(0).getDescription());
        assertEquals(owner.getName(), things.get(0).getOwner().getUserName());
    }

    /*
    Use Case: US 01.04.01 - EditItem
     */
    public void testUserEditItem(){
        User owner = new User();
        Thing thing = new Thing(owner);
        String title = "Coffee Maker";
        String description = "Makes great coffee.\nOnce served the queen of England.";
        thing.setTitle(title).setDescription(description);

        assertEquals(title, thing.getTitle());
        assertEquals(description, thing.getDescription());

        thing.setTitle("New Coffee Maker");
        thing.setDescription("New");

        assertEquals("New Coffee Maker", thing.getTitle());
        assertEquals("New", thing.getDescription());
    }

    /*
    Use Case: US 01.05.01 - DeleteItem
     */
    public void testUserDeleteItem(){
        User owner = new User();
        assertEquals(0, owner.getThings().size());
        Thing thing = new Thing(owner);
        assertEquals(1, owner.getThings().size());
        Thing thing2 = new Thing(owner);
        assertEquals(2, owner.getThings());

        owner.deleteItemAtIndex[1];

        assertEquals(1, owner.getThings().size());

        owner.deleteItemAtIndex[0]; // See above
        assertEquals(0, owner.getThings().size());
    }

    /*
    Use Case: US 05.02.01 - ViewBids
     */
    public void testUserBids(){

        User owner = new User();
        Thing thing = new Thing(owner);

        User borrower = new User();

        assertEquals(0, borrower.getBids().size());

        Bid bid = null;
        try {
            bid = new Bid(thing, borrower, 800);
        }catch(Exception e){
            fail();
        }
        try {
            thing.placeBid(bid);
        } catch (ThingUnavailableException e) {
            fail();
        }

        assertEquals(1, borrower.getBids().size());
        assertEquals(thing, bid.getThing());
        assertEquals(owner, bid.getThing().getOwner());
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

        Bid bid = null;
        User borrower = new User();
        try {
            bid = new Bid(thing1, borrower, 800);
        } catch( Exception e){
            fail();
        }
        try {
            thing1.placeBid(bid);
        } catch (ThingUnavailableException e) {
            fail();
        }

        assertEquals(1, borrower.getBids().size());
        assertEquals(2, owner.getThings().size());
    }

    /*
    Use Case: US 05.05.01 - ListBids
     */
    public void testGetBids(){
        User owner = new User();
        Thing thing1 = new Thing(owner);

        User borrower = new User();
        Bid bid = null;
        try {
            bid = new Bid(thing1, borrower, 800);
        } catch(Exception e){
            fail();
        }
        try {
            thing1.placeBid(bid);
        } catch (ThingUnavailableException e) {
            fail();
        }

        List<Bid> bids = borrower.getBids();

        assertEquals(1, bids.size());
        assertEquals(bids.get(0).getThing(), thing1);
        assertEquals(bids.get(0).getAmount(), 800);
    }

    /*
    Use Case: US 05.06.01 - AcceptBid
    */
    public void testAcceptBid(){
        User owner = new User();
        Thing thing1 = new Thing(owner);

        Bid bid1 = null;
        Bid bid2 = null;

        try {
            User borrower = new User();
            bid1 = new Bid(thing1, borrower, 800);
            User borrower2 = new User();
            bid2 = new Bid(thing1, borrower2, 700);
        } catch(ThingUnavailableException e){
            assertTrue(true);
        }

        try {
            thing1.placeBid(bid1);
        } catch (ThingUnavailableException e) {
            fail();
        }
        try {
            thing1.placeBid(bid2);
        } catch (ThingUnavailableException e) {
            fail();
        }

        owner.acceptBid(thing1, bid1);

        assertEquals(Thing.Status.BORROWED, bid1.getThing().getStatus());
        assertEquals(Thing.Status.AVAILABLE, bid2.getThing().getStatus());
    }

    /*
    Use Case: US 05.07.01 - DeclineBid
    */
    public void testDeclineBid()
    {
        User owner = new User();
        Thing thing1 = new Thing(owner);
        Bid bid = null;
        Bid bid2 = null;
        try{
            User borrower = new User();
            bid = new Bid(thing1, borrower, 800);
            User borrower2 = new User();
            bid2 = new Bid(thing1, borrower2, 700);
        } catch(ThingUnavailableException e){
            assertTrue(true);
        }

        try {
            thing1.placeBid(bid);
        } catch (ThingUnavailableException e) {
            fail();
        }
        try {
            thing1.placeBid(bid2);
        } catch (ThingUnavailableException e) {
            fail();
        }

        assertEquals(2, thing1.getBids().size());

        owner.declineBid(thing1, bid);

        assertEquals(1, thing1.getBids().size());

    }
}
