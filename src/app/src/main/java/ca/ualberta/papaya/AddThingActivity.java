package ca.ualberta.papaya;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;

import ca.ualberta.papaya.controllers.AddThingController;
import ca.ualberta.papaya.controllers.ThingListController;

public class AddThingActivity extends PapayaActivity {

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
