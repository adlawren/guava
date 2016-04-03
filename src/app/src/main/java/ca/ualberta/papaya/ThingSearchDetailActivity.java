package ca.ualberta.papaya;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import ca.ualberta.papaya.controllers.ThingSearchDetailController;
import ca.ualberta.papaya.exceptions.ThingUnavailableException;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.Ctx;
import ca.ualberta.papaya.util.Observer;

/**
 * Activity for displaying Thing objects that are returned by the search activity.
 * <p/>
 * Calls ThingSearchDetailController for all of the button implementations.
 *
 * @see ThingSearchDetailController
 */

public class ThingSearchDetailActivity extends AbstractPapayaActivity {

    public static final String THING_EXTRA = "ca.papaya.ualberta.thing.search.detail.thing.extra";

    private Thing thing;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
        thing = (Thing) intent.getSerializableExtra(THING_EXTRA);

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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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


        if (thing != null) {
            EditText bidAmount = (EditText) findViewById(R.id.bidAmount);
            MenuItem bidButton = (MenuItem) findViewById(R.id.bid);
            if(bidButton != null) {
                // WHY IS THIS NULL?
                bidButton.setOnMenuItemClickListener(
                        ThingSearchDetailController.getInstance()
                                .getUserBidOnClickListener(thing, bidAmount));
            }
        } else {
            System.err.print("No thing specified!!? (ThingSearchDetailActivity)");
        }

        menu.findItem(R.id.searchPictureView).setOnMenuItemClickListener(ThingSearchDetailController.getInstance()
                .getImageOnClickListener(this, thing));

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ThingSearchDetail Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://ca.ualberta.papaya/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ThingSearchDetail Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://ca.ualberta.papaya/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
