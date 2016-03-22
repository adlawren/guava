package ca.ualberta.papaya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ca.ualberta.papaya.controllers.EditThingController;
import ca.ualberta.papaya.models.Thing;

/**
 * Activity for editing Thing objects.
 *
 * Calls EditThingController for all of the button implementations.
 * @see EditThingActivity
 */
public class EditThingActivity extends AbstractPapayaActivity {

    public static final String THING_EXTRA = "ca.papaya.ualberta.edit.thing.thing.extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Intent intent = getIntent();
        Thing thing = (Thing) intent.getSerializableExtra(THING_EXTRA);

        EditText itemNameEditText = (EditText) findViewById(R.id.itemName),
                descriptionEditText = (EditText) findViewById(R.id.description);

        itemNameEditText.setText(thing.getTitle());
        descriptionEditText.setText(thing.getDescription());

//        FloatingActionButton editItemFloatingActionButton = (FloatingActionButton)
//                findViewById(R.id.editItem);
//        editItemFloatingActionButton.setOnClickListener(EditThingController.getInstance()
//                .getEditItemOnClickListener(this, thing, itemNameEditText, descriptionEditText));
//
//        Button availableButton = (Button) findViewById(R.id.available);
//        availableButton.setOnClickListener(EditThingController.getInstance()
//                .getAvailableOnClickListener(this, thing));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
    }

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

}
