package ca.ualberta.papaya;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import ca.ualberta.papaya.controllers.EditThingController;
import ca.ualberta.papaya.models.Photo;
import ca.ualberta.papaya.models.Thing;

/**
 * Activity for editing Thing objects.
 *
 * Calls EditThingController for all of the button implementations.
 * @see EditThingActivity
 */

public class EditThingActivity extends AbstractPapayaActivity {

    public static final String THING_EXTRA = "ca.papaya.ualberta.edit.thing.thing.extra";

    Intent intent = null;
    Thing thing = null;
    public static final int PHOTO_RESULT = 10;
    Bitmap picture = null;

    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        intent = getIntent();
        thing = (Thing) intent.getSerializableExtra(THING_EXTRA);

        EditText itemNameEditText = (EditText) findViewById(R.id.itemName),
                descriptionEditText = (EditText) findViewById(R.id.description);
        imageButton = (ImageButton) findViewById(R.id.viewPicture);


        itemNameEditText.setText(thing.getTitle());
        descriptionEditText.setText(thing.getDescription());
        if( thing.getPhoto().getImage() != null){
            picture = thing.getPhoto().getImage();
            imageButton.setImageBitmap(picture);
        } else{
            imageButton.setImageBitmap(picture);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditThingActivity.this, AddPictureActivity.class);
                intent.putExtra(ViewPictureActivity.PICTURE_EXTRA, picture);

                startActivityForResult(intent, PHOTO_RESULT);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        EditText itemNameEditText = (EditText) findViewById(R.id.itemName),
                descriptionEditText = (EditText) findViewById(R.id.description);



        menu.findItem(R.id.editItem).setOnMenuItemClickListener(EditThingController.getInstance()
                .getEditItemOnClickListener(this, thing, itemNameEditText, descriptionEditText, imageButton));
        //menu.findItem(R.id.available).setOnMenuItemClickListener(EditThingController.getInstance()

                //bgodley: i still have to do fiddle with this
                //.getEditItemOnClickListener(this, thing, itemNameEditText, descriptionEditText, picture));
                //.getEditItemOnClickListener(this, thing, itemNameEditText, descriptionEditText));


        return true;
    }


    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.editItem:
                View editItemView = findViewById(R.id.addItem);
                Intent intent = getIntent();
                Thing thing = (Thing) intent.getSerializableExtra(THING_EXTRA);

                EditText itemNameEditText = (EditText) findViewById(R.id.itemName),
                        descriptionEditText = (EditText) findViewById(R.id.description);
                editItemView.setOnClickListener(EditThingController.getInstance().getEditItemOnClickListener(this, thing, itemNameEditText, descriptionEditText));
                editItemView.performClick();
                return true;
            case R.id.available:
                View availableView = findViewById(R.id.addItem);
                Intent intent2 = getIntent();
                Thing thingy = (Thing) intent2.getSerializableExtra(THING_EXTRA);
                availableView.setOnClickListener(EditThingController.getInstance().getAvailableOnClickListener(this, thingy));
                availableView.performClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == EditThingController.PHOTO_RESULT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                picture = data.getParcelableExtra(AddPictureActivity.PICTURE_EXTRA);
                imageButton.setImageBitmap(picture);
                Photo photo = new Photo();
                photo.setImage(picture);
                thing.setPhoto(photo);

            }
        }
    }
//Todo picture doesn't work
}
