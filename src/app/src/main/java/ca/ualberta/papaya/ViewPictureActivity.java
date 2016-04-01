package ca.ualberta.papaya;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


/**
 * Created by bgodley on 27/03/16.
 *
 * Activity for a user to view a picture for another User's Thing
 */
public class ViewPictureActivity extends AbstractPapayaActivity {

    private Intent intent;
    private Bitmap picture;



    private ImageView image;


    public static final String PICTURE_EXTRA = "ca.papaya.ualberta.edit.Picture";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_picture);

        image = (ImageView) findViewById(R.id.picture);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        intent = getIntent();
        picture = intent.getParcelableExtra(PICTURE_EXTRA);
        updateView();
    }

    private void updateView(){
        if(picture != null){
            image.setImageBitmap(picture);
        } else {
            image.setImageResource(android.R.color.transparent);
        }
    }


}
