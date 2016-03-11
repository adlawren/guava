package ca.ualberta.papaya;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import ca.ualberta.papaya.controllers.ThingSearchController;

public class ThingSearchActivity extends AbstractPapayaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_search);

        EditText keywordsEditText = (EditText) findViewById(R.id.keywords);
        FloatingActionButton searchFloatingActionButton = (FloatingActionButton) findViewById(R.id.search);
        searchFloatingActionButton.setOnClickListener(ThingSearchController.getInstance().getSearchOnClickListener(this,
                keywordsEditText));
    }
}
