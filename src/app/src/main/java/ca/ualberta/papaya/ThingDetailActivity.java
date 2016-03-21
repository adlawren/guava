package ca.ualberta.papaya;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;

import ca.ualberta.papaya.controllers.ThingDetailController;
import ca.ualberta.papaya.data.ThrowawayDataManager;
import ca.ualberta.papaya.models.Thing;

/**
 * An activity representing a single Thing detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ThingListActivity}.
 */
public class ThingDetailActivity extends AbstractPapayaActivity {

    public static final String THING_EXTRA = "ca.papaya.ualberta.thing.detail.thing.extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Thing thing = (Thing) intent.getSerializableExtra(THING_EXTRA);

        TextView thingDetailTextView = (TextView) findViewById(R.id.thing_detail);
        thingDetailTextView.setText(thing.getDescription());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        FloatingActionButton editItemButton = (FloatingActionButton) findViewById(R.id.editItem);
//        editItemButton.setOnClickListener(ThingDetailController.getInstance()
//                .getEditItemOnClickListener(this, thing));
//
//        FloatingActionButton deleteItemButton = (FloatingActionButton) findViewById(R.id.deleteItem);
//        deleteItemButton.setOnClickListener(ThingDetailController.getInstance()
//                .getDeleteItemOnClickListener(this, thing));

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ThingDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ThingDetailFragment.ARG_ITEM_ID));
            ThingDetailFragment fragment = new ThingDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.thing_detail_container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, ThingListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
