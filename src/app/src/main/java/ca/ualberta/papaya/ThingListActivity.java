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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import ca.ualberta.papaya.controllers.ThingListController;
import ca.ualberta.papaya.controllers.ThrowawayElasticSearchController;
import ca.ualberta.papaya.data.MyThingsDataManager;
import ca.ualberta.papaya.interfaces.IObserver;
import ca.ualberta.papaya.models.Bid;
import ca.ualberta.papaya.models.ElasticModel;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.LocalUser;
import ca.ualberta.papaya.util.Observable;
import ca.ualberta.papaya.util.Observer;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
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

    // TODO: Determine if this is needed
    private static ArrayList<Thing> thingList = new ArrayList<>();

    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitle(getTitle());

        recyclerView = (RecyclerView) findViewById(R.id.thing_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayShowTitleEnabled(false);
//        }

        if (findViewById(R.id.thing_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        //updateView();

        Observable<ArrayList<Thing>> observable = new Observable<>();
        observable.addObserver(new IObserver<ArrayList<Thing>>() {
            @Override
            public void update(ArrayList<Thing> data) {
                thingList.clear();
                thingList.addAll(data);

                // TODO: Add borrowed things - DON'T actually neeed this
//                Observable<ArrayList<Thing>> allThingsObservable = new Observable<>();
//                allThingsObservable.addObserver(new IObserver<ArrayList<Thing>>() {
//                    @Override
//                    public void update(ArrayList<Thing> data) {
//                        ArrayList<Thing> borrowedThings = new ArrayList<Thing>();
//
//                        for (Thing thing : data) {
//                            if (thing.getBorrowerId() == null) continue;
//
//                            if (thing.getBorrowerId().equals(LocalUser.getId())) {
//                                borrowedThings.add(thing);
//                            }
//                        }
//
//                        thingList.addAll(borrowedThings);
//                        recyclerView.getAdapter().notifyDataSetChanged();
//                    }
//                });
//
//                ThrowawayElasticSearchController.SearchThingTask searchThingTask =
//                        new ThrowawayElasticSearchController.SearchThingTask(allThingsObservable);
//                searchThingTask.execute("{ \"size\" : \"500\" }");

                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        MyThingsDataManager.getInstance().getData(observable);

        // TODO: Remove; test
        User user = new User();
        user.setId(LocalUser.getId());

        Thing thing = new Thing(user);
        thing.getLastModified();

        Date date = thing.getLastModified();

        System.out.println("Date: " + date);
        System.out.println("Time: " + thing.getLastModified().getTime());
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

        return true;
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        SimpleItemRecyclerViewAdapter va = new SimpleItemRecyclerViewAdapter(thingList);
        recyclerView.setAdapter(va);
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
            holder.mStatusView.setText("Status: " + mValues.get(position).getStatus().toString());

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
            public final ImageView mPictureView;
            public final TextView mStatusView;
            public Thing mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
                mPictureView = (ImageView) view.findViewById(R.id.picture);
                mStatusView = (TextView) view.findViewById(R.id.status);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all:
                // EITHER CALL THE METHOD HERE OR DO THE FUNCTION DIRECTLY
                setFilterAll();
                return true;
            case R.id.borrowed:
                setFilterBorrowed();
                return true;
            case R.id.bidded:
                setFilterBidded();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setFilterAll(){
        Thing.search(new Observer<List<Thing>>() {
            @Override
            public void update(List<Thing> data) {
                thingList.clear();
                thingList.addAll(data);

                final SimpleItemRecyclerViewAdapter va = new SimpleItemRecyclerViewAdapter(thingList);
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(va);
                    }
                });
            }
        }, Thing.class, "{ \"size\" : \"500\", \"query\" : { \"bool\" : { \"must\" : " +
                "[ { \"match\" : { \"ownerId\" : \"" + LocalUser.getId() + "\" } } ] } } }");
    }
    public void setFilterBorrowed(){
        Thing.search(new Observer<List<Thing>>() {
            @Override
            public void update(List<Thing> data) {
                thingList.clear();
                thingList.addAll(data);

                final SimpleItemRecyclerViewAdapter va = new SimpleItemRecyclerViewAdapter(thingList);
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(va);
                    }
                });
            }
        }, Thing.class, "{ \"size\" : \"500\", \"query\" : { \"bool\" : { \"must\" : " +
                "[ { \"match\" : { \"ownerId\" : \"" + LocalUser.getId() + "\" } } ], \"must\" : " +
                "[ { \"match\" : { \"status\" : \"BORROWED\" } } ] } } }");
    }
    public void setFilterBidded(){
//        Thing.search(new Observer<List<Thing>>() {
//            @Override
//            public void update(List<Thing> data) {
//                thingList.clear();
//                thingList.addAll(data);
//
//                final SimpleItemRecyclerViewAdapter va = new SimpleItemRecyclerViewAdapter(thingList);
//                recyclerView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        recyclerView.setAdapter(va);
//                    }
//                });
//            }
//        }, Thing.class, "{ \"size\" : \"500\", \"query\" : { \"bool\" : { \"must\" : " +
//                "[ { \"match\" : { \"ownerId\" : \"" + LocalUser.getId() + "\" } } ], \"must\" : " +
//                "[ { \"match\" : { \"status\" : \"BIDDED\" } } ] } } }");
        Thing.search(new Observer<List<Thing>>() {
            @Override
            public void update(List<Thing> data) {
                final ArrayList<Thing> tempThings = new ArrayList<>();

                thingList.clear();
                for (Thing thing : data) {
                        final Thing nextThing = thing;

                        System.out.println("[ThingListActivity] Next thing: id: " + nextThing.getId() +
                                ", title: " + nextThing.getTitle() + ", description: " + nextThing.getDescription());

                        nextThing.getBids(new Observer() {
                            @Override
                            public void update(Object data) {
                                ArrayList<Bid> bids = (ArrayList<Bid>) data;

                                System.out.println("[ThingListActivity] bid count: " + bids.size());

                                if (bids.size() > 0) {
//                                    tempThings.add(nextThing);
                                    thingList.add(nextThing);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            recyclerView.getAdapter().notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                        });
                }

                System.out.println("[ThingListActivity] Temp things: size: " + tempThings.size() + ", contents:");
                for (Thing thing : tempThings) {
                   System.out.println("Next thing: id: " + thing.getId() + ", title: " + thing.getTitle() + ", description: " + thing.getDescription());
                }

//                thingList.clear();
//                thingList.addAll(tempThings);

                final SimpleItemRecyclerViewAdapter va = new SimpleItemRecyclerViewAdapter(thingList);
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(va);
                    }
                });
            }
        }, Thing.class, "{ \"size\" : \"500\", \"query\" : { \"bool\" : { \"must\" : " +
                "[ { \"match\" : { \"ownerId\" : \"" + LocalUser.getId() + "\" } } ] } } }");
    }
}
