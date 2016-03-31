package ca.ualberta.papaya;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ca.ualberta.papaya.util.Ctx;

/**
 * Created by mghumphr on 3/10/16.
 *
 * Super class for the activity classes.
 */
public abstract class AbstractPapayaActivity  extends AppCompatActivity {

    // get context information from Ctx
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Ctx.set(getApplicationContext());
    }

    private String loadUserId(){
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }
}
