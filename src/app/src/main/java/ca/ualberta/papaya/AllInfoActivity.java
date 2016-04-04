package ca.ualberta.papaya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import java.util.List;

import ca.ualberta.papaya.controllers.AllInfoController;
import ca.ualberta.papaya.models.Bid;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.LocalUser;
import ca.ualberta.papaya.util.Observer;

/**
 * Created by Nfs91 on 2016-04-03.
 *
 *  Main activity for displaying the user info of a user.
 */
public class AllInfoActivity extends AbstractPapayaActivity {

    public static final String THING_EXTRA = "ca.papaya.ualberta.thing.search.detail.thing.extra";

    private Thing thing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_info);

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

            final TextView itemInformationTextView = (TextView) findViewById(R.id.thing_detail);
            itemInformationTextView.setText("Title: " + thing.getTitle() +"\n" + "Details: "
                    + thing.getDescription() + "\n" + "My Bid: ");

            thing.getOwner(new Observer<User>() {
                @Override
                public void update(final User owner) {
                    final TextView userNameTextView = (TextView) findViewById(R.id.userName);
                    final TextView userInformationTextView = (TextView) findViewById(R.id.userInfo);


                    Bid.search(new Observer<List<Bid>>() {
                        @Override
                        public void update(List<Bid> bids) {
                            if (bids.size() == 0) {
                                itemInformationTextView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        itemInformationTextView.setText("Title: " + thing.getTitle() +"\n" + "Details: "
                                                + thing.getDescription() + "\n" + "My Bid: $0.00");
                                    }
                                });
                            } else {
                                Bid maxBid = bids.get(0);
                                for (Bid bid : bids) {
                                    if (bid.getAmount() > maxBid.getAmount()) {
                                        maxBid = bid;
                                    }
                                }
                                final Bid theMaxBid = maxBid;
                                itemInformationTextView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        itemInformationTextView.setText("Title: " + thing.getTitle() +"\n" + "Details: "
                                                + thing.getDescription() + "\n" + "My Bid: " + theMaxBid.toString());
                                    }
                                });
                            }
                        }
                    }, Bid.class, "{\"query\": {\"bool\": {\"must\": [" +
                            "{\"match\": {\"bidderId\": \"" + LocalUser.getId() + "\"}}," +
                            "{\"match\": {\"thingId\": \"" + thing.getId() + "\"}}" +
                            "]}}}");



                    userInformationTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            userNameTextView.setText(owner.getFullName());
                            userInformationTextView.setText("Email: " + owner.getEmail() + "\n"
                                    + "Phone: " + owner.getPhone() + "\n" + "Address: "
                                    + owner.getAddress1() + "\n" + "City: " + owner.getCity()
                                    + "\n" + "Province/State: " + owner.getProvince() + "\n"
                                    + "Country: " + owner.getCountry() + "\n" + "Postal Code: "
                                    + owner.getPostal());
                        }
                    });
                }
            }); // todo: add proper search query

        } else {
            System.err.print("No thing specified!!? (AllInfoActivity)");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_info, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.image).setOnMenuItemClickListener(AllInfoController.getInstance()
                .getImageOnClickListener(this, thing));

        menu.findItem(R.id.display_location).setOnMenuItemClickListener(AllInfoController.getInstance()
                .getDisplayLocationOnClickListener(this, thing));

        return true;
    }

}
