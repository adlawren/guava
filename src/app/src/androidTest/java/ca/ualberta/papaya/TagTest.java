package ca.ualberta.papaya;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.papaya.models.Tag;


/**
 * Created by martin on 10/02/16.
 */
public class TagTest extends ActivityInstrumentationTestCase2 {

    public TagTest() {
        super(ThingListActivity.class);
    }

    @Override
    public void setUp(){

    }

    @Override
    public void tearDown(){
        // clean up if needed once on live DB.
    }


    public void testNewTag() {

        String label = "coffee";

        Tag tag = new Tag();
        tag.setLabel(label);

        assertEquals(label, tag.getLabel());

    }

    public void testNewTagUppercase() {

        String label = "Coffee";
        String expected = "coffee";

        Tag tag = new Tag();
        tag.setLabel(label);

        assertEquals(expected, tag.getLabel());

    }

    public void testTagParser() {
        String tagsString = "popcorn machine maker";

        List<Tag> tags = Tag.parseString(tagsString);

        assertEquals(3, tags.size());

        for(Tag tag : tags){
            switch(tag.getLabel()){
                case "popcorn":
                case "machine":
                case "maker":
                    break;
                default:
                    fail();
            }
        }
    }

    public void testTagParserComplex() {
        String tagsString = "popcorn, machine / maker";

        List<Tag> tags = Tag.parseString(tagsString);

        assertEquals(3, tags.size());

        for(Tag tag : tags){
            switch(tag.getLabel()){
                case "popcorn":
                case "machine":
                case "maker":
                    break;
                default:
                    fail();
            }
        }
    }

    public void testTagParserEmpty() {
        String tagsString = "";

        List<Tag> tags = Tag.parseString(tagsString);

        assertEquals(0, tags.size());
    }

    public void testTagStringify(){
        Tag tag1 = new Tag(); tag1.setLabel("cappuccino");
        Tag tag2 = new Tag(); tag2.setLabel("steamer");
        Tag tag3 = new Tag(); tag3.setLabel("frother");

        List<Tag> tags = new ArrayList<Tag>();
        tags.add(tag1);
        tags.add(tag2);
        tags.add(tag3);

        assertEquals("cappuccino, steamer, frother", Tag.stringify(tags));
    }



}
