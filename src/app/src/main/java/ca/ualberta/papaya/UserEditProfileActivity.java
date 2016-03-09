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

        EditText userFirstNameEditText = (EditText) findViewById(R.id.edit_user_profile_edit_first_name);
        userFirstNameEditText.setText(user.getFirstName());

        EditText userLastNameEditText = (EditText) findViewById(R.id.edit_user_profile_edit_last_name);
        userLastNameEditText.setText(user.getFirstName());

        EditText userEmailEditText = (EditText) findViewById(R.id.edit_user_profile_edit_email);
        userEmailEditText.setText(user.getFirstName());

        EditText userAddress1EditText = (EditText) findViewById(R.id.edit_user_profile_edit_address1);
        userEmailEditText.setText(user.getAddress1());

        EditText userAddress2EditText = (EditText) findViewById(R.id.edit_user_profile_edit_address2);
        userEmailEditText.setText(user.getAddress2());

        EditText userProvinceEditText = (EditText) findViewById(R.id.edit_user_profile_edit_province);
        userEmailEditText.setText(user.getProvince().toString());

        EditText userCountryEditText = (EditText) findViewById(R.id.edit_user_profile_edit_country);
        userEmailEditText.setText(user.getCountry().toString());

        EditText userPostalEditText = (EditText) findViewById(R.id.edit_user_profile_edit_postal);
        userEmailEditText.setText(user.getPostal());

        Button saveButton = (Button) findViewById(R.id.edit_user_profile_save);
        saveButton.setOnClickListener(UserEditProfileController.getInstance().getSaveOnClickListener(this,
            userFirstNameEditText,
            userLastNameEditText,
            userEmailEditText,
            userAddress1EditText,
            userAddress2EditText,
            userProvinceEditText,
            userCountryEditText,
            userPostalEditText));

        Button cancelButton = (Button) findViewById(R.id.edit_user_profile_cancel);
        cancelButton.setOnClickListener(UserEditProfileController.getInstance().getCancelOnClickListener(this));
    }

    @Override
    public void update() {

    }
}
