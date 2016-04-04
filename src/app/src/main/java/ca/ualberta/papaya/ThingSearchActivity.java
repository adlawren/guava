package ca.ualberta.papaya;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.papaya.controllers.ThingSearchController;
import ca.ualberta.papaya.data.ThrowawayDataManager;
import ca.ualberta.papaya.exceptions.ThingUnavailableException;
import ca.ualberta.papaya.interfaces.IObserver;
import ca.ualberta.papaya.models.Bid;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.LocalUser;
import ca.ualberta.papaya.util.Observer;

/**
 *  Activity for searching Thing objects. Calls ThingSearchController for all of the
 *  button implementations
 *
 *  @see ThingSearchController
 */
public class ThingSearchActivity extends AbstractPapayaActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private static ArrayList<Thing> thingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationIcon(R.drawable.ic_action_home);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.search_thing_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        if (findViewById(R.id.thing_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        EditText searchValue = (EditText) findViewById(R.id.keywords);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.search_thing_list);

        Observer<ArrayList<Thing>> thingListObserver = new Observer<ArrayList<Thing>>() {
            @Override
            public void update(final ArrayList<Thing> data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EditText keywordsEditText = (EditText) findViewById(R.id.keywords);

                        thingList.clear();
                        for (Thing thing : data) {
                            if (thing.getDescription().contains(keywordsEditText.getText())) {
                                thingList.add(thing);
                            }
                        }

                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.search_thing_list);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                });
            }
        };

        menu.findItem(R.id.search).setOnMenuItemClickListener(ThingSearchController.getInstance()
                .getSearchOnClickListener(this, searchValue, recyclerView, thingList, thingListObserver));

        return true;
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        SimpleItemRecyclerViewAdapter va = new SimpleItemRecyclerViewAdapter(new ArrayList<Thing>());
        recyclerView.setAdapter(va);

        // TODO: Determine if filtering is needed here; depends on if the Activity is loaded with previously used keywords
        final EditText keywordsEditText = (EditText) findViewById(R.id.keywords);

        Thing.search(new Observer<List<Thing>>() {
            @Override
            public void update(List<Thing> data) {

                // TODO: Find a more efficient way to filter by keywords
                thingList.clear();
                for (Thing thing : data) {
                    if (thing.getTitle().contains(keywordsEditText.getText())) {
                        thingList.add(thing);
                    }
                }

                final SimpleItemRecyclerViewAdapter va = new SimpleItemRecyclerViewAdapter(thingList);
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(va);
                    }
                });
            }
        }, Thing.class, "{ \"size\" : \"500\", \"sort\" : { \"subscription\" : {\"order\" : \"desc\"} }, " +
                "\"query\" : { \"bool\" : { \"must_not\" : " +
                "[ { \"match\" : { \"ownerId\" : \"" + LocalUser.getId() + "\" } } ], \"must\" : " +
                "[ { \"match\" : { \"status\" : \"AVAILABLE\" } } ] } } }");
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Thing> mValues;

        public SimpleItemRecyclerViewAdapter(List<Thing> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_thing_list_content, parent, false);
            return new ViewHolder(view);
        }

        // display the Thing that is clicked on in the list. start ThingSearchDetailActivity
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.mItem = mValues.get(position);
            holder.mIdView.setText(holder.mItem.getTitle()); // .getId()
            holder.mContentView.setText(holder.mItem.getDescription()); // .getTitle()
            holder.mPictureView.setImageBitmap(holder.mItem.getPhoto().getImage());

            holder.mBidView.setText("Highest Bid: ");
            holder.mItem.getBids(new Observer<List<Bid>>() {
                @Override
                public void update(List<Bid> bids) {
                    if(bids.size() == 0){
                        holder.mBidView.post(new Runnable() {
                            @Override
                            public void run() {
                                holder.mBidView.setText("Highest Bid: $0.00");
                            }
                        });
                    } else {
                        Bid maxBid = bids.get(0);
                        for(Bid bid : bids){
                            if(bid.getAmount() > maxBid.getAmount()){
                                maxBid = bid;
                            }
                        }
                        final Bid theMaxBid = maxBid;
                        holder.mBidView.post(new Runnable() {
                            @Override
                            public void run() {
                                holder.mBidView.setText("Highest Bid: " + theMaxBid.toString());
                            }
                        });
                    }
                }
            });

            holder.mOwnerView.setText("Owner: ");
            holder.mItem.getOwner(new Observer<User>() {
                @Override
                public void update(final User owner) {
                    holder.mOwnerView.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.mOwnerView.setText("Owner: " + owner.getFullName());
                        }
                    });
                }
            });


            if(holder.mItem.getSubscription() > 0){
                holder.mIdView.setTypeface(Typeface.DEFAULT_BOLD);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ThingDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        ThingDetailFragment fragment = new ThingDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.thing_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ThingSearchDetailActivity.class);
                        intent.putExtra(ThingSearchDetailActivity.THING_EXTRA, holder.mItem);
                        intent.putExtra(ThingDetailFragment.ARG_ITEM_ID, holder.mItem.getId());

                        context.startActivity(intent);
                    }
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
            public final ImageView mPictureView;
            public final TextView mBidView;
            public final TextView mOwnerView;
            public Thing mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
                mPictureView = (ImageView) view.findViewById(R.id.picture);
                mBidView = (TextView) view.findViewById(R.id.highestBid);
                mOwnerView = (TextView) view.findViewById(R.id.owner);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
