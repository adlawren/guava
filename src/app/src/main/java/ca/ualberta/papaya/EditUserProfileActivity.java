package ca.ualberta.papaya;

import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
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

        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.saveProfile);
        saveButton.setOnClickListener(EditUserProfileController.getInstance().getSaveOnClickListener(this, userEmailEditText));
    }
}
