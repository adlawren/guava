package ca.ualberta.papaya;

import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.widget.EditText;

import ca.ualberta.papaya.controllers.UserEditProfileController;
import ca.ualberta.papaya.interfaces.IObserver;
import ca.ualberta.papaya.models.User;

public class UserEditProfileActivity extends AbstractPapayaActivity implements IObserver {

    public static final String USER_EXTRA = "ca.papaya.ualberta.user.edit.profile.user.extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        User user = (User) getIntent().getSerializableExtra(USER_EXTRA);

        EditText userEmailEditText = (EditText) findViewById(R.id.contactInfo);
        userEmailEditText.setText(user.getEmail());

        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.saveProfile);
        saveButton.setOnClickListener(UserEditProfileController.getInstance().getSaveOnClickListener(this, userEmailEditText));
    }

    @Override
    public void update() {

    }
}
