package ca.ualberta.papaya;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

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


    private Button backButton;
    private ImageView imageView;


    public static final String PICTURE_EXTRA = "ca.papaya.ualberta.edit.Picture";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.); //Todo change xml file


        //imageView = (ImageView) findViewById(R.id.imageView);//Todo fill in id

        // Button for going back to the Thing
        //backButton = (Button) findViewById(R.id.backButton);//Todo fill in id
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

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
            imageView.setImageBitmap(picture);

        } else {
            imageView.setImageResource(android.R.color.transparent);
        }
    }










}
