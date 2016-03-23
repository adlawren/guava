package ca.ualberta.papaya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import ca.ualberta.papaya.controllers.ThingSearchDetailController;
import ca.ualberta.papaya.exceptions.ThingUnavailableException;
import ca.ualberta.papaya.models.Thing;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.Ctx;
import ca.ualberta.papaya.util.Observer;

/**
 * Activity for displaying Thing objects that are returned by the search activity.
 *
 * Calls ThingSearchDetailController for all of the button implementations.
 * @see ThingSearchDetailController
 */

public class ThingSearchDetailActivity extends Activity {

    public static final String THING_EXTRA = "ca.papaya.ualberta.thing.search.detail.thing.extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_search_detail);

        Intent intent = getIntent();
        final Thing thing = (Thing) intent.getSerializableExtra(THING_EXTRA);

        if(thing != null) {

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
                                    .getUserOnClickListener(Ctx.get(), owner));
                        }
                    });
                }
            }); // todo: add proper search query
        } else {
            System.err.print("No thing specified!!? (ThingSearchDetailActivity)");
        }

    }
}
