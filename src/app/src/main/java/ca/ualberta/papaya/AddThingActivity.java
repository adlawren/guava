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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import ca.ualberta.papaya.controllers.AddThingController;
import ca.ualberta.papaya.controllers.EditThingController;
import ca.ualberta.papaya.controllers.ThingListController;
import ca.ualberta.papaya.models.Photo;

/**
 * Activity for adding Thing objects.
 *
 * Calls AddThingController for all of the button implementations.
 * @see AddThingController
 *
 */
public class AddThingActivity extends AbstractPapayaActivity {

    private Bitmap picture = null;
    public static final int PHOTO_RESULT = 10;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);

        }

        toolbar.setNavigationIcon(R.drawable.ic_action_home);

        imageView = (ImageButton) findViewById(R.id.viewPicture);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddThingActivity.this, AddPictureActivity.class);
                intent.putExtra(AddPictureActivity.PICTURE_EXTRA, picture);

                startActivityForResult(intent, PHOTO_RESULT);

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        EditText itemNameEditText = (EditText) findViewById(R.id.itemName),
                descriptionEditText = (EditText) findViewById(R.id.description);

        menu.findItem(R.id.addItem).setOnMenuItemClickListener(AddThingController.getInstance()
                .getSaveOnClickListener(this, itemNameEditText, descriptionEditText, imageView));


        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == EditThingController.PHOTO_RESULT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                picture = data.getParcelableExtra(AddPictureActivity.PICTURE_EXTRA);
                ImageButton imageButton =(ImageButton) findViewById(R.id.viewPicture);
                imageButton.setImageBitmap(picture);
            }
        }
    }
}
