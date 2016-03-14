package ca.ualberta.papaya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import ca.ualberta.papaya.controllers.ThingSearchDetailController;
import ca.ualberta.papaya.models.Thing;

public class ThingSearchDetailActivity extends Activity {

    public static final String THING_EXTRA = "ca.papaya.ualberta.thing.search.detail.thing.extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_search_detail);

        Intent intent = getIntent();
        Thing thing = (Thing) intent.getSerializableExtra(THING_EXTRA);

        // TODO: Remove; test
//        if (thing == null) {
//            System.out.println("It's the thing");
//        } else {
//            System.out.println("It's NOT the thing: " + thing.getOwner().getName());
//        }

        TextView userInformationTextView = (TextView) findViewById(R.id.userInfo);
        userInformationTextView.setText(thing.getOwnerName());
        userInformationTextView.setOnClickListener(ThingSearchDetailController.getInstance()
                .getUserOnClickListener(this, thing.getOwner()));

        TextView itemInformationTextView = (TextView) findViewById(R.id.thingDetail);
        itemInformationTextView.setText(thing.getDescription());
    }
}
