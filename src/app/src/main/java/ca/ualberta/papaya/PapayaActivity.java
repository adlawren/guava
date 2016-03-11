package ca.ualberta.papaya;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ca.ualberta.papaya.util.Ctx;

/**
 * Created by mghumphr on 3/10/16.
 */
public class PapayaActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Ctx.set(getApplicationContext());
    }


}
