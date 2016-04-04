package ca.ualberta.papaya;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

import ca.ualberta.papaya.controllers.EditUserProfileController;
import ca.ualberta.papaya.models.User;
import ca.ualberta.papaya.util.LocalUser;
import ca.ualberta.papaya.util.Observer;

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


        LocalUser.getUser(new Observer<User>() {
            @Override
            public void update(final User user) {
                if(user == null){
                    System.err.println("[[[USER NOT FOUND]]] " + LocalUser.getId());
                } else {

                    final EditText userFirstNameEditText = (EditText) findViewById(R.id.firstName);
                    userFirstNameEditText.post(new Runnable() {
                        @Override
                        public void run() {
                            userFirstNameEditText.setText(user.getFirstName());
                        }
                    });

                    final EditText userLastNameEditText = (EditText) findViewById(R.id.lastName);
                    userLastNameEditText.post(new Runnable() {
                        @Override
                        public void run() {
                            userLastNameEditText.setText(user.getLastName());
                        }
                    });

                    final EditText userEmailEditText = (EditText) findViewById(R.id.email);
                    userEmailEditText.post(new Runnable() {
                        @Override
                        public void run() {
                            userEmailEditText.setText(user.getEmail());
                        }
                    });

                    final EditText userPhoneEditText = (EditText) findViewById(R.id.phone);
                    userPhoneEditText.post(new Runnable() {
                        @Override
                        public void run() {
                            userPhoneEditText.setText(user.getPhone());
                        }
                    });

                    final EditText userAddress1EditText = (EditText) findViewById(R.id.address1);
                    userAddress1EditText.post(new Runnable() {
                        @Override
                        public void run() {
                            userAddress1EditText.setText(user.getAddress1());
                        }
                    });

                    final EditText userAddress2EditText = (EditText) findViewById(R.id.address2);
                    userAddress2EditText.post(new Runnable() {
                        @Override
                        public void run() {
                            userAddress2EditText.setText(user.getAddress2());
                        }
                    });

                    final EditText userCityEditText = (EditText) findViewById(R.id.city);
                    userCityEditText.post(new Runnable() {
                        @Override
                        public void run() {
                            userCityEditText.setText(user.getCity());
                        }
                    });

                    final EditText userProvinceEditText = (EditText) findViewById(R.id.province);
                    userProvinceEditText.post(new Runnable() {
                        @Override
                        public void run() {
                            userProvinceEditText.setText(user.getProvince());
                        }
                    });

                    final EditText userCountryEditText = (EditText) findViewById(R.id.country);
                    userProvinceEditText.post(new Runnable() {
                        @Override
                        public void run() {
                            userCountryEditText.setText(user.getCountry());
                        }
                    });

                    final EditText userPostalEditText = (EditText) findViewById(R.id.postal);
                    userPostalEditText.post(new Runnable() {
                        @Override
                        public void run() {
                            userPostalEditText.setText(user.getPostal());
                        }
                    });

                    final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    toolbar.post(new Runnable() {
                        @Override
                        public void run() {
                            setSupportActionBar(toolbar);
                        }
                    });

                    final ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null) actionBar.getCustomView().post(new Runnable() {
                        @Override
                        public void run() {
                            actionBar.setDisplayHomeAsUpEnabled(true);
                            actionBar.setDisplayShowTitleEnabled(false);
                        }
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toolbar.setNavigationIcon(R.drawable.ic_action_home);
                        }
                    });
                }
            }
        });

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

        HashMap profile = new HashMap<String, EditText>();
        profile.put("firstName", findViewById(R.id.firstName));
        profile.put("lastName", findViewById(R.id.lastName));
        profile.put("email", findViewById(R.id.email));
        profile.put("phone", findViewById(R.id.phone));
        profile.put("address1", findViewById(R.id.address1));
        profile.put("address2", findViewById(R.id.address2));
        profile.put("city", findViewById(R.id.city));
        profile.put("province", findViewById(R.id.province));
        profile.put("country", findViewById(R.id.country));
        profile.put("postal", findViewById(R.id.postal));


        menu.findItem(R.id.editProfile).setOnMenuItemClickListener(EditUserProfileController.getInstance().getSaveOnClickListener(this, profile));

        return true;
    }
}
