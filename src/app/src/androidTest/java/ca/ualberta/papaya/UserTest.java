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

    public void testNewUser(){
        User user = new User();

        user.setFirstName("Daddy").setLastName("Cool");
        assertEquals("Daddy Cool", user.getFullName());

        user.setEmail("abc@example.com");
        assertEquals("abc@example.com", user.getEmail());

        user.setAddress1("123 Fake Street");
        assertEquals("123 Fake Street", user.getAddress1());

        user.setAddress2("Penthouse Suite");
        assertEquals("Penthouse Suite", user.getAddress2())

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
    }

    public void testBadPostal(){
        User user = new User();
        try {
            user.setPostal("BAD POSTAL CODE");
            fail();
        } catch (UserInvalidPostalException e) {
            // ok!
        }
    }

    public void testUserThings(){

        User owner = new User();
        Thing thing1 = new Thing(owner);
        Thing thing2 = new Thing(owner);

        List<Thing> things = owner.getThings();

        assertEquals(2, owner.getThings().size());

    }

    public void testUserBids(){

        Thing thing = new Thing(new User());

        User borrower = new User();

        assertEquals(0, borrower.getBids().size());

        Bid bid = new Bid(thing, borrower, 800);

        try {
            thing.placeBid(bid);
        } catch (ThingUnavailableException e) {
            fail();
        }

        assertEquals(1, borrower.getBids().size());

    }

}
