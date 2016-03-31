package ca.ualberta.papaya;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        intent = getIntent();
        thing = (Thing) intent.getSerializableExtra(THING_EXTRA);

        EditText itemNameEditText = (EditText) findViewById(R.id.itemName),
                descriptionEditText = (EditText) findViewById(R.id.description);

        itemNameEditText.setText(thing.getTitle());
        descriptionEditText.setText(thing.getDescription());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
    }


    //
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        EditText itemNameEditText = (EditText) findViewById(R.id.itemName),
                descriptionEditText = (EditText) findViewById(R.id.description);

        menu.findItem(R.id.editItem).setOnMenuItemClickListener(EditThingController.getInstance()
                .getEditItemOnClickListener(this, thing, itemNameEditText, descriptionEditText));
        menu.findItem(R.id.available).setOnMenuItemClickListener(EditThingController.getInstance()
                .getEditItemOnClickListener(this, thing, itemNameEditText, descriptionEditText));
        menu.findItem(R.id.viewPicture).setOnMenuItemClickListener(EditThingController.getInstance()
                .getSetPictureOnClickListener(this, thing));

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == EditThingController.PHOTO_RESULT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bitmap temp = data.getParcelableExtra(AddPictureActivity.PICTURE_EXTRA);
                Photo photo = new Photo();
                photo.setImage(temp);
                thing.setPhoto(photo);

            }
        }
    }

}
