package ca.ualberta.papaya;

import android.os.Bundle;
import android.view.Menu;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_user_info);

        Thing thing = (Thing) getIntent().getSerializableExtra(THING_EXTRA);
        Bid bid = (Bid) getIntent().getSerializableExtra(BID_EXTRA);

        TextView userNameTextView = (TextView) findViewById(R.id.user);
        userNameTextView.setText(bid.getBidderName());

        TextView bidAmountTextView = (TextView) findViewById(R.id.bid);
        bidAmountTextView.setText(bid.toString());

        /* on reserialization we lose the transient .kind property...
         so .getBidder breaks.


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
         */

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

//        menu.findItem(R.id.otherItems).setOnMenuItemClickListener(BidProfileController.getInstance()
//                .getAcceptOnClickListener(this));
//        menu.findItem(R.id.addItem).setOnMenuItemClickListener(BidProfileController.getInstance()
//                .getDeclineOnClickListener(this));


        return true;
    }
}
