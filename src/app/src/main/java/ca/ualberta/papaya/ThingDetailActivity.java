package ca.ualberta.papaya;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import ca.ualberta.papaya.data.ThrowawayDataManager;

/**
 * An activity representing a single Thing detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ThingListActivity}.
 */
public class ThingDetailActivity extends AbstractPapayaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        //final int index = intent.getIntExtra("position", 0);

        //Thing tempThing = ThrowawayDataManager.getInstance().getThingAt(index);
        //ListView details = (ListView) findViewById(R.id.bids);
        //details = (EditText) findViewById(R.id.thing_detail_container);
        //details.setText(tempThing.getTitle());
        //NestedScrollView details = (NestedScrollView) findViewById(R.id.thing_detail_container);
        //TextView tv1 = new TextView(this);
        //tv1.setText(tempThing.getTitle());
        //details.addView(tv1);
        //ArrayList<String> items = new ArrayList<>();
        //items.add(tempThing.getTitle());
        //items.add(tempThing.getDescription());
        //ArrayAdapter<String> adapter;
        //adapter = new ArrayAdapter<>(this, R.layout.activity_thing_detail, items);
        //details.setAdapter(adapter);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton deleteButton = (FloatingActionButton) findViewById(R.id.deleteItem);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                // TODO: Implement
                System.err.println("TODO: Implement deleteButton OnClickListener");

//                ThrowawayDataManager.getInstance().deleteThing(index);
//                Context context = v.getContext();
//                Intent intent = new Intent(context, ThingListActivity.class);
//                startActivity(intent);
            }
        });


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
