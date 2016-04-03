package ca.ualberta.papaya;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.papaya.controllers.ThingSearchDetailController;

import ca.ualberta.papaya.models.Bid;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.LocalUser;
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

            View recyclerView = findViewById(R.id.bid_list);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView);

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



            EditText bidAmount = (EditText) findViewById(R.id.bidAmount);
            bidAmount.setText("0");

            if (bidAmount != null) {
                menu.findItem(R.id.bid).setOnMenuItemClickListener(
                        ThingSearchDetailController.getInstance()
                                .getUserBidOnClickListener(thing, bidAmount, this));
            }



        menu.findItem(R.id.searchPictureView).setOnMenuItemClickListener(ThingSearchDetailController.getInstance()
                .getImageOnClickListener(this, thing));

        return true;
    }



    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        SimpleItemRecyclerViewAdapter va = new SimpleItemRecyclerViewAdapter(new ArrayList<Bid>());
        recyclerView.setAdapter(va);
        thing.getBids(new Observer<List<Bid>>() {
            @Override
            public void update(List<Bid> bids) {
                System.err.println("GOT BIDS");
                System.err.println(bids.size());
                final SimpleItemRecyclerViewAdapter va = new SimpleItemRecyclerViewAdapter(bids);
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(va);
                    }
                });

            }
        });
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Bid> mValues;

        public SimpleItemRecyclerViewAdapter(List<Bid> bids) {
            mValues = bids;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bid_list_content, parent, false);
            return new ViewHolder(view);
        }

        // method that is called when a Thing in the list is selected.
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).toString());
            holder.mContentView.setText(mValues.get(position).getBidderName());


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // do nothing on click for now
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Bid mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.bid);
                mContentView = (TextView) view.findViewById(R.id.bidder);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }



}
