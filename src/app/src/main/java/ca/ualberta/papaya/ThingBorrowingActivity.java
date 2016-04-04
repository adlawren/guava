package ca.ualberta.papaya;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.papaya.controllers.ThingBorrowingController;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.util.LocalUser;
import ca.ualberta.papaya.util.Observer;

/**
 * Created by hsbarker on 3/29/16.
 *
 * Main activity for displaying objects that a user is currently borrowing
 *
 * Calls ThingBorrowingController for the method calls
 * @see ThingBorrowingController
 *
 *
 */
public class ThingBorrowingActivity extends AbstractPapayaActivity{
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_items_borrowing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitle(getTitle());


        View recyclerView = findViewById(R.id.borrowing_thing_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationIcon(R.drawable.ic_action_home);

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
        getMenuInflater().inflate(R.menu.menu_other_items_borrowing, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.goToBiddings).setOnMenuItemClickListener(ThingBorrowingController.getInstance()
                .getBiddingItemsOnClickListener(this));

        return true;
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        SimpleItemRecyclerViewAdapter va = new SimpleItemRecyclerViewAdapter(new ArrayList<Thing>());
        recyclerView.setAdapter(va);
        Thing.search(new Observer<List<Thing>>() {
            @Override
            public void update(List<Thing> things) {
                final SimpleItemRecyclerViewAdapter va = new SimpleItemRecyclerViewAdapter(things);
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(va);
                    }
                });

            }
        }, Thing.class, "{ \"size\" : \"500\", \"query\" : { \"match\" : { \"borrowerId\" : \"" +
                LocalUser.getId() + "\" } } }"); // TODO: Test query
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
                    .inflate(R.layout.borrowing_thing_list_content, parent, false);
            return new ViewHolder(view);
        }

        // method that is called when a Thing in the list is selected.
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).getTitle()); // .getId()
            holder.mContentView.setText(mValues.get(position).getDescription()); // .getTitle()
            holder.mPictureView.setImageBitmap(mValues.get(position).getPhoto().getImage());
            holder.mBorrowerBidView.setText("Paying: ");

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
                        Intent intent = new Intent(context, AllInfoActivity.class);
                        intent.putExtra(AllInfoActivity.THING_EXTRA, holder.mItem);
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
            public final TextView mBorrowerBidView;
            public Thing mItem;


            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
                mPictureView = (ImageView) view.findViewById(R.id.picture);
                mBorrowerBidView = (TextView) view.findViewById(R.id.borrowerBid);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

}
