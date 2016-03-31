package ca.ualberta.papaya;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;

import android.view.ImageView;

/**
 * Created by bgodley on 3/11/16.
 */
public class Photographtest extends ActivityInstrumentationTestCase2 {

    public Photographtest() {
        super(ca.ualberta.papaya.ThingListActivity.class);
    }

    public void testAddPicture() {
        Thing thing = new Thing(new User());
        Bitmap testpic = BitmapFactory.decodeResource(getResources(),
                R.drawable.blue_pushpin);
        thing.addPicture(pic);
        assertEquals(thing.getPicture(), pic);
    }

    public void testDeletePic() {
        Thing thing = new Thing(new User());
        ImageView pic = (ImageView) findViewById(R.id.image1);
        thing.addPicture(pic);
        assertEquals(thing.getPicture(), pic);
        thing.deletePicture();
        assertEquals(thing.getPicture(), null);

    }

    public void testViewPic() {
        Thing thing = new Thing(new User());
        ImageView pic = (ImageView) findViewById(R.id.image1);
        thing.addPicture(pic);

        showPicture();
        ImageView screen = (ImageView) findViewById(R.id.showImage);
        assertEquals(pic, screen);

    }

    public void testPicSize() {
        // check to see if picture that is too big gets declined
        Thing thing = new Thing(new User());
        ImageView pic = (ImageView) findViewById(R.id.image2big);
        try {
            thing.addPicture(pic);
            assertTrue(false);
        } catch (exeption e) {
            assertTrue(true);
        }

    }
}
