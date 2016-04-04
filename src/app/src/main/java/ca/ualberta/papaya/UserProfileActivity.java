package ca.ualberta.papaya;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ca.ualberta.papaya.controllers.ThingSearchDetailController;
import ca.ualberta.papaya.models.User;

/**
 * Activity for displaying a User's profile when viewing a searched Thing.
 *
 */

public class UserProfileActivity extends AbstractPapayaActivity {

    public static final String USER_EXTRA = "com.papaya.user.profile.user.extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        User user = (User) getIntent().getSerializableExtra(USER_EXTRA);

        TextView userNameTextView = (TextView) findViewById(R.id.user);
        userNameTextView.setText(user.getName());

        TextView userInfoTextView = (TextView) findViewById(R.id.contactInfo);
        userInfoTextView.setText("Email: " + user.getEmail() + "\n" + "Phone: "
                + user.getPhone() + "\n" + "Address: " + user.getAddress1()
                + "\n" + "City: " + user.getCity() + "\n" + "Province/State: " + user.getProvince()
                + "\n" + "Country: " + user.getCountry() + "\n" + "Postal Code: "
                + user.getPostal());
    }
}
