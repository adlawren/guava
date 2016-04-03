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

import java.util.List;

import ca.ualberta.papaya.controllers.ThingSearchDetailController;
import ca.ualberta.papaya.exceptions.ThingUnavailableException;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.Ctx;
import ca.ualberta.papaya.util.Observer;

/**
 * Activity for displaying Thing objects that are returned by the search activity.
 *
 * Calls ThingSearchDetailController for all of the button implementations.
 * @see ThingSearchDetailController
 */

public class ThingSearchDetailActivity extends AbstractPapayaActivity {

    public static final String THING_EXTRA = "ca.papaya.ualberta.thing.search.detail.thing.extra";

    private Thing thing;

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

        toolbar.setNavigationIcon(R.drawable.ic_action_home);

        Intent intent = getIntent();
        final Thing thing = (Thing) intent.getSerializableExtra(THING_EXTRA);

        if (thing != null) {

            TextView itemInformationTextView = (TextView) findViewById(R.id.thingDetail);
            itemInformationTextView.setText(thing.getDescription());

            thing.getOwner(new Observer<User>() {
                @Override
                public void update(final User owner) {
                    final TextView userInformationTextView = (TextView) findViewById(R.id.userInfo);
                    userInformationTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            userInformationTextView.setText(owner.getFullName());
                            userInformationTextView.setOnClickListener(ThingSearchDetailController.getInstance()
                                    .getUserInfoOnClickListener(owner));
                        }
                    });
                }
            }); // todo: add proper search query
        } else {
            System.err.print("No thing specified!!? (ThingSearchDetailActivity)");
        }
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

        menu.findItem(R.id.bid).setOnMenuItemClickListener(ThingSearchDetailController.getInstance()
                .getUserBidOnClickListener(thing));

        return true;
    }
}
