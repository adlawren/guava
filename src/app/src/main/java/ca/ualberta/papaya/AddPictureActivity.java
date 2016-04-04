package ca.ualberta.papaya;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import ca.ualberta.papaya.controllers.AddThingController;

/**
 * Created by bgodley on 27/03/16.
 *
 * Activity for a user to view and edit a picture for their Thing
 */
public class AddPictureActivity extends AbstractPapayaActivity {

    private Intent intent;
    private Bitmap picture;
    private Bitmap undoPicture;

    private MenuItem takePicture;
    private MenuItem save;
    private MenuItem delete;

    private ImageView image;
    private int UNDO = 0;

    public static final String PICTURE_EXTRA = "ca.papaya.ualberta.edit.Picture";
    private static final int REQUEST_CAPTURING_IMAGE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture);

        image = (ImageView) findViewById(R.id.picture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationIcon(R.drawable.ic_action_home);

        intent = getIntent();

        picture = intent.getParcelableExtra(PICTURE_EXTRA);


        updateView();
    }



    // Button for saving the current picture to the selected thing
    void savePic() {

        Intent returnIntent = new Intent();
        returnIntent.putExtra(PICTURE_EXTRA, picture);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }


    // Button for taking a new picture
    void takePic() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CAPTURING_IMAGE);
        }

    }

    // button for deleting the picture
    // may put in a undo feature
    void deletePic() {
        if (UNDO == 0) {
            //delete
            undoPicture = picture;
            picture = null;
            //updateView();
            UNDO = 1;
            delete.setTitle("Undo");
        } else {
            //redo
            picture = undoPicture;
            undoPicture = null;
            //updateView();
            UNDO = 0;
            delete.setTitle("Delete");
        }
        //picture = null;
        updateView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.takePicture:
                // EITHER CALL THE METHOD HERE OR DO THE FUNCTION DIRECTLY
                takePic();
                return true;

            case R.id.saveButton:
                savePic();
                return true;

            case R.id.deleteButton:
                deletePic();
                return true;

<<<<<<< HEAD
            default:
                return super.onOptionsItemSelected(item);
        }
=======
            }
        });

        return true;
>>>>>>> a0f0fcec4112b71cc597e4032b9e8c4ba179a478
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//
//        takePicture = menu.findItem(R.id.takePicture);
//        takePicture.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                takePic();
//                return true;
//            }
//        });
//
//        save = menu.findItem(R.id.saveButton);
//        save.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                savePic();
//                return true;
//            }
//        });
//        delete = menu.findItem(R.id.deleteButton);
//        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                deletePic();
//                return true;
//
//            }
//        });


//
//        return true;
//    }

    @Override
    protected void onStart() {
        super.onStart();
        intent = getIntent();
        if(picture == null) {
            picture = intent.getParcelableExtra(PICTURE_EXTRA);

        }
        updateView();
    }

    private void updateView(){
        if(picture != null){
            image.setImageBitmap(picture);
        } else {
            image.setImageResource(android.R.color.transparent);
        }
    }


    //save the newly taken picture and update the view
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CAPTURING_IMAGE && resultCode == RESULT_OK){
            Bundle extras  = intent.getExtras();
            picture = (Bitmap) extras.get("data");

            updateView();

        }
    }
}








