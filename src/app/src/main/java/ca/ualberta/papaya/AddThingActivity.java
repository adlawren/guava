package ca.ualberta.papaya;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

        EditText itemNameEditText = (EditText) findViewById(R.id.itemName),
                descriptionEditText = (EditText) findViewById(R.id.description);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton saveFloatingActionButton = (FloatingActionButton) findViewById(R.id.editItem);
//        saveFloatingActionButton.setOnClickListener(AddThingController.getInstance().getSaveOnClickListener(this,
//                itemNameEditText,
//                descriptionEditText));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.otherItems:

                return true;
            case R.id.search:
                //showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
