package ca.ualberta.papaya;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ca.ualberta.papaya.controllers.ThingSearchDetailController;
import ca.ualberta.papaya.models.Thing;

/**
 * Activity for displaying Thing objects that are returned by the search activity.
 *
 * Calls ThingSearchDetailController for all of the button implementations.
 * @see ThingSearchDetailController
 */

public class ThingSearchDetailActivity extends AbstractPapayaActivity {

    public static final String THING_EXTRA = "ca.papaya.ualberta.thing.search.detail.thing.extra";

    Thing thing = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_search_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        Intent intent = getIntent();
        thing = (Thing) intent.getSerializableExtra(THING_EXTRA);

        // TODO: Remove; test
//        if (thing == null) {
//            System.out.println("It's the thing");
//        } else {
//            System.out.println("It's NOT the thing: " + thing.getOwner().getName());
//        }

        TextView userInformationTextView = (TextView) findViewById(R.id.userInfo);
        userInformationTextView.setText(thing.getOwnerName());
        //userInformationTextView.setOnClickListener(ThingSearchDetailController.getInstance()
                //.getUserOnClickListener(this, thing.getOwner()));

        TextView itemInformationTextView = (TextView) findViewById(R.id.thingDetail);
        itemInformationTextView.setText(thing.getDescription());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_thing_search_detail, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        //menu.findItem(R.id.bid).setOnMenuItemClickListener(ThingSearchDetailController.getInstance()
                //.getUserOnClickListener(this, thing.getOwner()));

        return true;
    }
}
