package ca.ualberta.papaya;

import android.graphics.Bitmap;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageView;

import ca.ualberta.papaya.models.Photo;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;



/**
 * Created by bgodley on 3/11/16.
 */
public class Photographtest extends ActivityInstrumentationTestCase2 {

    public Photographtest() {
        super(ca.ualberta.papaya.ThingListActivity.class);
    }

    public void testAddPicture() {
        Thing thing = new Thing(new User());
        Bitmap testpic = null;//Todo initialize bitmap

        Photo photo = new Photo();
        photo.setImage(testpic);
        thing.setPhoto(photo);

        assertEquals(thing.getPhoto().getImage(), testpic);
    }

    public void testDeletePic() {
        Thing thing = new Thing(new User());
        Bitmap testpic = null;//Todo initialize bitmap

        Photo photo = new Photo();
        photo.setImage(testpic);
        thing.setPhoto(photo);

        photo.setImage(null);
        thing.setPhoto(photo);
        assertEquals(thing.getPhoto().getImage(), null);

    }

    public void testViewPic() {
        Thing thing = new Thing(new User());
        Bitmap testpic = null;//Todo initialize bitmap


        ImageView screen = (ImageView) findViewById(R.id.imageView);
        screen.setImageBitmap(testpic);
        Bitmap onScreen = screen.getDrawingCache();
        assertEquals(testpic, onScreen);

    }

    public void testPicSize() {
        // check to see if picture that is too big gets declined
        Thing thing = new Thing(new User());
        Bitmap testpic = null;//Todo initialize bitmap

        Photo photo = new Photo();
        photo.setImage(testpic);


        try {
            thing.setPhoto(testpic);
            assertTrue(false);
        } catch (exeption e) {
            assertTrue(true);
        }

    }
}
