package ca.ualberta.papaya;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.papaya.controllers.ThingSearchController;
import ca.ualberta.papaya.data.ThrowawayDataManager;
import ca.ualberta.papaya.models.Thing;

/**
 *  Activity for searching Thing objects. Calls
 *
 *  @see ThingSearchController
 */
public class ThingSearchActivity extends AbstractPapayaActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private static ArrayList<Thing> thingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_search);

        thingList = ThrowawayDataManager.getInstance().getNonCurrentUserThings();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.thing_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.thing_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        EditText keywordsEditText = (EditText) findViewById(R.id.keywords);

        FloatingActionButton searchFloatingActionButton = (FloatingActionButton) findViewById(R.id.search);
        searchFloatingActionButton.setOnClickListener(ThingSearchController.getInstance().getSearchOnClickListener(this,
                keywordsEditText, recyclerView, thingList));

        // TODO: Remove; old
//        searchFloatingActionButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                EditText keywordsEditText = (EditText) findViewById(R.id.keywords);
//                String keywords = keywordsEditText.getText().toString();
//
//                thingList.clear();
//                for (Thing thing : ThrowawayDataManager.getInstance().getNonCurrentUserThings()) {
//                    if (thing.getDescription().contains(keywords)) {
//                        thingList.add(thing);
//                    }
//                }
//
//                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.thing_list);
//                recyclerView.getAdapter().notifyDataSetChanged();
//            }
//        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(thingList));
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
