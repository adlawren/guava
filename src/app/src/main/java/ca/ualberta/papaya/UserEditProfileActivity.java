package ca.ualberta.papaya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import ca.ualberta.papaya.controllers.UserEditProfileController;
import ca.ualberta.papaya.interfaces.Observer;
import ca.ualberta.papaya.models.User;

public class UserEditProfileActivity extends AppCompatActivity implements Observer {

    public static final String USER_EXTRA = "com.papaya.user.edit.profile.user.extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_profile);

        User user = (User) getIntent().getSerializableExtra(USER_EXTRA);

        // TODO: Modify this; this screen requires a more complex UI
        EditText userInfoEditText = (EditText) findViewById(R.id.edit_user_profile_edit_contact_information);
        userInfoEditText.setText(user.getEmail());

        Button saveButton = (Button) findViewById(R.id.edit_user_profile_save);
        saveButton.setOnClickListener(UserEditProfileController.getInstance().getSaveOnClickListener(userInfoEditText));

        Button cancelButton = (Button) findViewById(R.id.edit_user_profile_cancel);
        cancelButton.setOnClickListener(UserEditProfileController.getInstance().getCancelOnClickListener());
    }

    @Override
    public void update() {

    }
}
