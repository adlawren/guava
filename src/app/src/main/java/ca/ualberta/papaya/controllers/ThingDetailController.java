package ca.ualberta.papaya.controllers;

import android.content.Context;
import android.view.View;

/**
 * Created by adlawren on 13/03/16.
 */
public class ThingDetailController {
    private static ThingDetailController ourInstance = new ThingDetailController();

    public static ThingDetailController getInstance() {
        return ourInstance;
    }

    private ThingDetailController() {
    }

    private class EditItemOnClickListener implements View.OnClickListener {

        private Context context;

        public EditItemOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        public void onClick(View view) {

            // TODO: Implement
            System.err.println("TODO: Implement EditItemOnClickListener");
        }
    }

    public EditItemOnClickListener getEditItemOnClickListener(Context initialContext) {
        return new EditItemOnClickListener(initialContext);
    }

    private class DeleteItemOnClickListener implements View.OnClickListener {

        private Context context;

        public DeleteItemOnClickListener(Context initialContext) {
            context = initialContext;
        }

        @Override
        public void onClick(View view) {

        // Copied from ThingDetailActivity
//                ThrowawayDataManager.getInstance().deleteThing(index);
//                Context context = v.getContext();
//                Intent intent = new Intent(context, ThingListActivity.class);
//                startActivity(intent);

            // TODO: Implement
            System.err.println("TODO: Implement DeleteItemOnClickListener");
        }
    }

    public DeleteItemOnClickListener getDeleteItemOnClickListener(Context initialContext) {
        return new DeleteItemOnClickListener(initialContext);
    }
}
