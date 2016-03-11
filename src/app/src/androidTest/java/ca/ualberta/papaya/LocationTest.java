package ca.ualberta.papaya;

import android.test.ActivityInstrumentationTestCase2;

import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;


/**
 * Created by bgodley on 3/11/16.
 */


public class LocationTest extends ActivityInstrumentationTestCase2{

    public void testSetLocation(){
        Thing thing = new Thing(new User());
        Point p = new Point( -113.43473, 53.45358);
        thing.setLocation(p);

        assertEquals(thing.getLocation(), p);

    }


    public void testGetLocation(){
        Thing thing = new Thing(new User());
        Point p = new Point( -113.43473, 53.45358);
        thing.setLocation(p);

        assertEquals( thing.getLocation(), p);

    }



}