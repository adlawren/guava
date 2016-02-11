package ca.ualberta.papaya;

import android.test.ActivityInstrumentationTestCase2;

import java.util.List;

import ca.ualberta.papaya.exceptions.ThingUnavailableException;
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
        assertEquals(user.getFullName(), "Daddy Cool");

        user.setAddress1("123 Fake Street");
        assertEquals(user.getAddress1(), "123 Fake Street");

        user.setAddress2("Penthouse Suite");
        assertEquals(user.getAddress2(), "Penthouse Suite");

        user.setCountry(Country.CANADA);
        assertEquals(user.getCountry(), Country.CANADA);

        user.setProvince(Province.ALBERTA);
        assertEquals(user.getProvince(), Province.ALBERTA);

        user.setPostal("T8A 5H8");
        assertEquals(user.getPostal(), "T8A 5H8");
    }

    public void testUserThings(){

        User owner = new User();
        Thing thing1 = new Thing(owner);
        Thing thing2 = new Thing(owner);

        List<Thing> things = owner.getThings();

        assertEquals(owner.getThings().size(), 2);

    }

    public void testUserBids(){

        Thing thing = new Thing(new User());

        User borrower = new User();

        assertEquals(borrower.getBids().size(), 0);

        Bid bid = new Bid(thing, borrower, 800);

        try {
            thing.placeBid(bid);
        } catch (ThingUnavailableException e) {
            fail();
        }

        assertEquals(borrower.getBids().size(), 1);

    }

}
