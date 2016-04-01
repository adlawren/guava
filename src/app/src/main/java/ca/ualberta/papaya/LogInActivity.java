package ca.ualberta.papaya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import ca.ualberta.papaya.controllers.LogInController;
import ca.ualberta.papaya.util.Ctx;
import ca.ualberta.papaya.util.LocalUser;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize shared context
        Ctx.set(getApplicationContext());

        String id =  LocalUser.getId();
        if (id != null) {
            Intent intent = new Intent(this, ThingListActivity.class);
            startActivity(intent);
        }

        EditText userNameEditText = (EditText)  findViewById(R.id.userName),
                passwordEditText =  (EditText) findViewById(R.id.password);

        Button submitButton = (Button) findViewById(R.id.login);
        submitButton.setOnClickListener(LogInController.getInstance().getSubmitOnClickListener(this,
                userNameEditText, passwordEditText));
    }
}
