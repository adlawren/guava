package ca.ualberta.papaya;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import ca.ualberta.papaya.models.User;

/**
 * Created by adlawren on 09/03/16.
 */
public class UserProfileActivityTest extends ActivityInstrumentationTestCase2 {

    public UserProfileActivityTest() {
        super(UserProfileActivity.class);
    }

    /**
     * Use Case: US 03.01.01 & US 03.03.01 - Profile & Profile Selection
     */
    public void testOpenProfile() {
        Intent intent = new Intent();

        User user = new User();
        user.setFirstName("Emily");
        user.setLastName("Jones");
        user.setEmail("ejones@ualberta.ca");

        intent.putExtra(UserProfileActivity.USER_EXTRA, user);
        setActivityIntent(intent);

        UserProfileActivity userProfileActivity = (UserProfileActivity) getActivity();

        TextView userNameTextView = (TextView) userProfileActivity.findViewById(R.id.userName),
                userEmailTextView = (TextView) userProfileActivity.findViewById(R.id.userInfo);

        assertEquals("ERROR: User name is incorrect.",
                user.getName(),
                userNameTextView.getText().toString());
        assertEquals("ERROR: User email is incorrect.",
                user.getEmail(),
                userEmailTextView.getText().toString());
    }
}
