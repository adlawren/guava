package ca.ualberta.papaya;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


/**
 * Created by bgodley on 27/03/16.
 *
 * Activity for a user to view and edit a picture for their Thing
 */
public class AddPictureActivity extends AbstractPapayaActivity {

    private Intent intent;
    //private Thing thing;
    //private Photo photo;
    private Bitmap picture;
    private Bitmap undoPicture;

    private Button takePicture;
    private Button saveButton;
    private Button deletePicture;
    private ImageView imageView;
    private int UNDO = 0;



    public static final String PICTURE_EXTRA = "ca.papaya.ualberta.edit.Picture";
    private static final int REQUEST_CAPTURING_IMAGE = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.); //Todo change xml file


       //imageView = (ImageView) findViewById(R.id.imageView);//Todo fill in id

        // Button for saving the current picture to the selected thing
        //saveButton = (Button) findViewById(R.id.saveButton);//Todo fill in id
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(PICTURE_EXTRA, picture);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        });

        // Button for taking a new picture
        //takePicture = (Button) findViewById(R.id.takePicturue);//Todo fill in id
        takePicture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_CAPTURING_IMAGE);
                }
            }
        });

        // button for deleting the picture
        // may put in a undo feature
        //deletePicture = (Button) findViewById(R.id.deletePicture);//Todo fill in id
        deletePicture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(UNDO == 0){
                    //delete
                    undoPicture = picture;
                    picture = null;
                    updateView();
                    UNDO = 1;
                    deletePicture.setText("Redo");
                } else {
                    //redo
                    picture = undoPicture;
                    undoPicture = null;
                    updateView();
                    UNDO = 0;
                    deletePicture.setText("Delete");
                }
                picture = null;
                updateView();
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


    //save the newly taken picture and update the view
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent intent){
        if(requestCode == REQUEST_CAPTURING_IMAGE && resultCode == RESULT_OK){
            Bundle extras  = intent.getExtras();
            picture = (Bitmap) extras.get("data");
            updateView();

        }
    }







}
