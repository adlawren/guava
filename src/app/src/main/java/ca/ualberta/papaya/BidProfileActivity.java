package ca.ualberta.papaya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import ca.ualberta.papaya.controllers.BidProfileController;
import ca.ualberta.papaya.models.Bid;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.Observer;

/**
 * Activity for displaying a User's profile when viewing a searched Thing.
 *
 */

public class BidProfileActivity extends AbstractPapayaActivity {

    public static final String THING_EXTRA = "com.papaya.user.profile.thing.extra";
    public static final String BID_EXTRA = "com.papaya.user.profile.bid.extra";

    private Thing thing;
    private Bid bid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_user_info);

        thing = (Thing) getIntent().getSerializableExtra(THING_EXTRA);
        bid = (Bid) getIntent().getSerializableExtra(BID_EXTRA);

        TextView userNameTextView = (TextView) findViewById(R.id.user);
        userNameTextView.setText(bid.getBidderName());

        TextView bidAmountTextView = (TextView) findViewById(R.id.bid);
        bidAmountTextView.setText(bid.toString());

        bid.getBidder(new Observer<User>() {
            @Override
            public void update(final User bidder) {
                final TextView userEmailTextView = (TextView) findViewById(R.id.contactInfo);
                userEmailTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        userEmailTextView.setText(bidder.getEmail());
                    }
                });

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationIcon(R.drawable.ic_action_home);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bid_decision, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.accept).setOnMenuItemClickListener(
                BidProfileController.getInstance()
                        .getAcceptOnClickListener(this, bid));

        menu.findItem(R.id.decline).setOnMenuItemClickListener(
                BidProfileController.getInstance()
                        .getDeclineOnClickListener(this, bid));


        return true;
    }
}
