package ca.ualberta.papaya;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import ca.ualberta.papaya.controllers.AddThingController;
import ca.ualberta.papaya.controllers.ThingListController;

/**
 * Activity for adding Thing objects.
 *
 * Calls AddThingController for all of the button implementations.
 * @see AddThingController
 *
 */
public class AddThingActivity extends AbstractPapayaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
//
//        EditText itemNameEditText = (EditText) findViewById(R.id.itemName),
//                descriptionEditText = (EditText) findViewById(R.id.description);

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
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        EditText itemNameEditText = (EditText) findViewById(R.id.itemName),
                descriptionEditText = (EditText) findViewById(R.id.description);

        menu.findItem(R.id.addItem).setOnMenuItemClickListener(
                AddThingController.getInstance()
                        .getSaveOnClickListener(this, itemNameEditText, descriptionEditText));

        return true;
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.addItem:
                View addItemView = findViewById(R.id.addItem);
                EditText itemNameEditText = (EditText) findViewById(R.id.itemName),
                        descriptionEditText = (EditText) findViewById(R.id.description);
                addItemView.setOnClickListener(AddThingController.getInstance().getSaveOnClickListener(this,itemNameEditText,descriptionEditText));
                addItemView.performClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */
}
