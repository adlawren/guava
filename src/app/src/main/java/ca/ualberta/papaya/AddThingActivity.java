package ca.ualberta.papaya;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddThingActivity extends AbstractPapayaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thing);
    }
}
