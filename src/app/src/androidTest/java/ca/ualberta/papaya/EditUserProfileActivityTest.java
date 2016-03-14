package ca.ualberta.papaya;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import ca.ualberta.papaya.models.User;

/**
 * Created by adlawren on 09/03/16.
 */
public class EditUserProfileActivityTest extends ActivityInstrumentationTestCase2 {

    public EditUserProfileActivityTest() {
        super(EditUserProfileActivity.class);
    }

    /**
     * Use Case: US 03.02.01 - Profile Edition
     */
    public void testEditProfile() {
        Intent intent = new Intent();

        User user = new User();
        user.setEmail("ejones@ualberta.ca");

        intent.putExtra(EditUserProfileActivity.USER_EXTRA, user);
        setActivityIntent(intent);

        EditUserProfileActivity userProfileActivity = (EditUserProfileActivity) getActivity();

        TextView userEmailTextView = (TextView) userProfileActivity.findViewById(R.id.contactInfo);

        assertEquals("ERROR: User email is incorrect.",
                user.getEmail(),
                userEmailTextView.getText().toString());
    }
}
