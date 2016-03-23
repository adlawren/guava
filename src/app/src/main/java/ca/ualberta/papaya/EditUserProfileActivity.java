package ca.ualberta.papaya;

import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import ca.ualberta.papaya.controllers.EditUserProfileController;
import ca.ualberta.papaya.models.User;

/**
 * Activity for editing user profiles.
 *
 * Calls EditUserProfile Controller for all of the button implementations.
 * @see EditUserProfileActivity
 */
public class EditUserProfileActivity extends AbstractPapayaActivity {

    public static final String USER_EXTRA = "ca.papaya.ualberta.user.edit.profile.user.extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        User user = (User) getIntent().getSerializableExtra(USER_EXTRA);

        EditText userEmailEditText = (EditText) findViewById(R.id.contactInfo);
        userEmailEditText.setText(user.getEmail());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.saveProfile);
//        saveButton.setOnClickListener(EditUserProfileController.getInstance().getSaveOnClickListener(this, userEmailEditText));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        EditText contactInfo = (EditText) findViewById(R.id.contactInfo);

        menu.findItem(R.id.editProfile).setOnMenuItemClickListener(EditUserProfileController.getInstance().getSaveOnClickListener(this, contactInfo));

        return true;
    }
}
