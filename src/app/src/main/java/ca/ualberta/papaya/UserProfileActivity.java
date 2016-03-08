package ca.ualberta.papaya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ca.ualberta.papaya.models.User;

public class UserProfileActivity extends AppCompatActivity {

    public static final String USER_EXTRA = "com.papaya.user.profile.user.extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra(USER_EXTRA); // TODO: establish public identifier

        TextView userNameTextView = (TextView) findViewById(R.id.user_profile_user_name);
        userNameTextView.setText(user.getName());

        TextView userEmailTextView = (TextView) findViewById(R.id.user_profile_user_email);
        userEmailTextView.setText(user.getEmail());

        TextView userAddress1TextView = (TextView) findViewById(R.id.user_profile_user_address1);
        userAddress1TextView.setText(user.getAddress1());

        TextView userAddress2TextView = (TextView) findViewById(R.id.user_profile_user_address2);
        userAddress2TextView.setText(user.getAddress2());

        TextView userProvinceTextView = (TextView) findViewById(R.id.user_profile_user_province);
        userProvinceTextView.setText(user.getProvince().toString());

        TextView userCountryTextView = (TextView) findViewById(R.id.user_profile_user_country);
        userCountryTextView.setText(user.getCountry().toString());

        TextView userPostalTextView = (TextView) findViewById(R.id.user_profile_user_postal);
        userPostalTextView.setText(user.getPostal());
    }
}
