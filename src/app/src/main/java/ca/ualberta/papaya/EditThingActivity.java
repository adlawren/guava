package ca.ualberta.papaya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
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

        FloatingActionButton editItemFloatingActionButton = (FloatingActionButton)
                findViewById(R.id.editItem);
        editItemFloatingActionButton.setOnClickListener(EditThingController.getInstance()
                .getEditItemOnClickListener(this, thing, itemNameEditText, descriptionEditText));

        Button availableButton = (Button) findViewById(R.id.available);
        availableButton.setOnClickListener(EditThingController.getInstance()
                .getAvailableOnClickListener(this, thing));

    }
}
