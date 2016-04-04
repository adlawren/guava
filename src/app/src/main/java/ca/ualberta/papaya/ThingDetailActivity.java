package ca.ualberta.papaya;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.papaya.controllers.ThingDetailController;
import ca.ualberta.papaya.controllers.ThrowawayElasticSearchController;
import ca.ualberta.papaya.data.ThrowawayDataManager;
import ca.ualberta.papaya.interfaces.IObserver;
import ca.ualberta.papaya.models.Bid;
import ca.ualberta.papaya.models.ElasticModel;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.util.Ctx;
import ca.ualberta.papaya.util.Observable;
import ca.ualberta.papaya.util.Observer;

/**
 * An activity representing a single Thing detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ThingListActivity}.
 */
public class ThingDetailActivity extends AbstractPapayaActivity {

    public static final String THING_EXTRA = "ca.papaya.ualberta.thing.detail.thing.extra";

    // Test
    public static final String ID_EXTRA = "ca.papaya.ualberta.thing.detail.id.extra";

    Intent intent = null;
    //public static final int PHOTO_RESULT = 10;
    Bitmap picture = null;
    private Thing thing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = getIntent();
        thing = (Thing) intent.getSerializableExtra(THING_EXTRA);

        // Alternatively
        String id = intent.getStringExtra(ID_EXTRA);
        System.out.println("Intent passed id: " + id);

        ElasticModel.getById(new IObserver<Thing>() {
            @Override
            public void update(Thing data) {
                System.out.println("In observer: Thing id: " + data.getId());
            }
        }, Thing.class, id);


        TextView thingDetailTextView = (TextView) findViewById(R.id.thing_detail);
        thingDetailTextView.setText("Title: " + thing.getTitle() +"\n" + "Details: "
                + thing.getDescription() + "\n" + "Status: " + thing.getStatus().toString());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationIcon(R.drawable.ic_action_home);

        View recyclerView = findViewById(R.id.bid_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);



//        FloatingActionButton editItemButton = (FloatingActionButton) findViewById(R.id.editItem);
//        editItemButton.setOnClickListener(ThingDetailController.getInstance()
//                .getEditItemOnClickListener(this, thing));
//
//        FloatingActionButton deleteItemButton = (FloatingActionButton) findViewById(R.id.deleteItem);
//        deleteItemButton.setOnClickListener(ThingDetailController.getInstance()
//                .getDeleteItemOnClickListener(this, thing));

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
//        if (savedInstanceState == null) {
//            // Create the detail fragment and add it to the activity
//            // using a fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putString(ThingDetailFragment.ARG_ITEM_ID,
//                    getIntent().getStringExtra(ThingDetailFragment.ARG_ITEM_ID));
//            ThingDetailFragment fragment = new ThingDetailFragment();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.thing_detail_container, fragment)
//                    .commit();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_thing_detail, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.goToEdit).setOnMenuItemClickListener(ThingDetailController.getInstance()
                .getEditItemOnClickListener(this, thing));
        menu.findItem(R.id.delete).setOnMenuItemClickListener(ThingDetailController.getInstance()
                .getDeleteItemOnClickListener(this, thing));
        menu.findItem(R.id.display_location).setOnMenuItemClickListener(ThingDetailController.getInstance()
                .getDisplayLocationOnClickListener(this, thing));
        menu.findItem(R.id.image).setOnMenuItemClickListener(ThingDetailController.getInstance()
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
            holder.mView.setOnClickListener(
                    ThingDetailController.getInstance()
                        .getBidOnClickListener(thing, holder.mItem)
            );
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






    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.goToEdit:
                View goToEditView = findViewById(R.id.addItem);
                goToEditView.setOnClickListener(ThingDetailController.getInstance().getEditItemOnClickListener(this, thing));
                goToEditView.performClick();
                return true;
            case R.id.delete:
                View deleteView = findViewById(R.id.delete);
                deleteView.setOnClickListener(ThingDetailController.getInstance().getDeleteItemOnClickListener(this, thing));
                deleteView.performClick();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            // This ID represents the Home or Up button. In the case of this
//            // activity, the Up button is shown. For
//            // more details, see the Navigation pattern on Android Design:
//            //
//            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
//            //
//            navigateUpTo(new Intent(this, ThingListActivity.class));
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}

