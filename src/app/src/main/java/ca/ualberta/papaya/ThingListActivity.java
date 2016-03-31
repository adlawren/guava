package ca.ualberta.papaya;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.ActionBarActivity;



import ca.ualberta.papaya.controllers.ThingListController;
import ca.ualberta.papaya.dummy.DummyContent;
import ca.ualberta.papaya.fixtures.Country;
import ca.ualberta.papaya.fixtures.Province;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.data.ThrowawayDataManager;
import ca.ualberta.papaya.util.Ctx;
import ca.ualberta.papaya.util.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representin
 * g a list of Things. This activity
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

    private int FILTER; //0 all, 1 borrowed,2 bidded
    //TODO: Remove usage of tempThings
    //ArrayList<Thing> tempThings = new ArrayList<>();


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

        //updateView();

    }


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

        //menu.findItem(R.id.all).setOnMenuItemClickListener(ThingListController.getInstance()
              //  .getAllFilterOnClickListener(this));
        //menu.findItem(R.id.borrowed).setOnMenuItemClickListener(ThingListController.getInstance()
              // .getBorrewedFilterOnClickListener(this));
        //menu.findItem(R.id.bidded).setOnMenuItemClickListener(ThingListController.getInstance()
        //  .getBiddidFilterOnClickListener(this));


        return true;

    }


    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //http://developer.android.com/reference/android/view/View.html#performClick()

        switch (item.getItemId()) {
            case R.id.otherItems:
                View otherItemsView = findViewById(R.id.otherItems);
                otherItemsView.setOnClickListener(ThingListController.getInstance().getOtherItemsOnClickListener(this));
                otherItemsView.performClick();
                return true;
            case R.id.addItem:
                View addItemView = findViewById(R.id.addItem);
                addItemView.setOnClickListener(ThingListController.getInstance().getAddItemOnClickListener(this));
                addItemView.performClick();
                return true;
            case R.id.profile:
                View profileView = findViewById(R.id.profile);
                profileView.setOnClickListener(ThingListController.getInstance().getProfileOnClickListener(this));
                profileView.performClick();
                return true;
            case R.id.search:
                View searchView = findViewById(R.id.search);
                searchView.setOnClickListener(ThingListController.getInstance().getSearchOnClickListener(this));
                searchView.performClick();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */


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
        }, Thing.class, "{}"); // todo: add proper search query

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
            holder.mPictureView.setImageBitmap(mValues.get(position).getPhoto().getImage());

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
            public final ImageView mPictureView;
            public Thing mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
                mPictureView = (ImageView) view.findViewById(R.id.picture);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    public void setFilterAll(){
        FILTER = 0;
        updateView();
    }
    public void setFilterBorrowed(){
        FILTER = 1;
        updateView();
    }
    public void setFilterBidded(){
        FILTER = 2;
        updateView();
    }





    public void updateView(){
        MenuItem filterButton =(MenuItem) findViewById(R.id.filter);
        if( FILTER == 0){
            filterButton.setTitle("All Item");
        } else if(FILTER == 1){
            filterButton.setTitle("Borrowed");
        } else{
            filterButton.setTitle("Bidded");
        }

        //Todo put the code to change items per filter here

    }


}
