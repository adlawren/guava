package ca.ualberta.papaya;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

        FloatingActionButton saveFloatingActionButton = (FloatingActionButton) findViewById(R.id.editItem);
        saveFloatingActionButton.setOnClickListener(AddThingController.getInstance().getSaveOnClickListener(this,
                itemNameEditText,
                descriptionEditText));
    }
}
