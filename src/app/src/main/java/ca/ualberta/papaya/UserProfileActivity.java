package ca.ualberta.papaya;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ca.ualberta.papaya.models.User;

public class UserProfileActivity extends PapayaActivity {

    public static final String USER_EXTRA = "com.papaya.user.profile.user.extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_info);

        User user = (User) getIntent().getSerializableExtra(USER_EXTRA);

        TextView userNameTextView = (TextView) findViewById(R.id.userName);
        userNameTextView.setText(user.getName());

        TextView userEmailTextView = (TextView) findViewById(R.id.userInfo);
        userEmailTextView.setText(user.getEmail());
    }
}
