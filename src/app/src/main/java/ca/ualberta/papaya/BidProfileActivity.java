package ca.ualberta.papaya;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import ca.ualberta.papaya.controllers.BidProfileController;
import ca.ualberta.papaya.models.User;

/**
 * Activity for displaying a User's profile when viewing a searched Thing.
 *
 */

public class BidProfileActivity extends AbstractPapayaActivity {

    public static final String USER_EXTRA = "com.papaya.user.profile.user.extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_user_info);

        User user = (User) getIntent().getSerializableExtra(USER_EXTRA);

        TextView userNameTextView = (TextView) findViewById(R.id.user);
        userNameTextView.setText(user.getName());

        TextView userEmailTextView = (TextView) findViewById(R.id.contactInfo);
        userEmailTextView.setText(user.getEmail());

        TextView userBidView = (TextView) findViewById(R.id.bid);
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
