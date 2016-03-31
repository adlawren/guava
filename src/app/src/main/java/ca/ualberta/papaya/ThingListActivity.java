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
import android.widget.TextView;


import ca.ualberta.papaya.controllers.ThingListController;
import ca.ualberta.papaya.controllers.ThrowawayElasticSearchController;
import ca.ualberta.papaya.data.MyThingsDataManager;
import ca.ualberta.papaya.interfaces.IObserver;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.Observable;
import ca.ualberta.papaya.util.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Things. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ThingDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 *
 * Calls ThingListController for all of the button implementations
 * @see ThingListController
 *
 * Calls ThingDetailFragment when a specific thing in clicked
 * @see ThingDetailFragment
 */
public class ThingListActivity extends AbstractPapayaActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.thing_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        if (findViewById(R.id.thing_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        // TODO: Remove; test
//        System.out.println("I'm resuming!");
//
//        View recyclerView = findViewById(R.id.thing_list);
//        assert recyclerView != null;
//        setupRecyclerView((RecyclerView) recyclerView);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_thing_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.otherItems).setOnMenuItemClickListener(ThingListController.getInstance()
                .getOtherItemsOnClickListener(this));
        menu.findItem(R.id.addItem).setOnMenuItemClickListener(ThingListController.getInstance()
                .getAddItemOnClickListener(this));
        menu.findItem(R.id.profile).setOnMenuItemClickListener(ThingListController.getInstance()
                .getProfileOnClickListener(this));
        menu.findItem(R.id.search).setOnMenuItemClickListener(ThingListController.getInstance()
                .getSearchOnClickListener(this));

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
        }, Thing.class, "{ \"size\" : \"50\" }"); // todo: add proper search query
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
                    .inflate(R.layout.thing_list_content, parent, false);
            return new ViewHolder(view);
        }

        // method that is called when a Thing in the list is selected.
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).getTitle()); // .getId()
            holder.mContentView.setText(mValues.get(position).getDescription()); // .getTitle()

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
                        Intent intent = new Intent(context, ThingDetailActivity.class);
                        intent.putExtra(ThingDetailActivity.THING_EXTRA, holder.mItem);
                        intent.putExtra(ThingDetailActivity.ID_EXTRA, holder.mItem.getId());
                        intent.putExtra(ThingDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        //intent.putExtra("position", position);

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
            public Thing mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
