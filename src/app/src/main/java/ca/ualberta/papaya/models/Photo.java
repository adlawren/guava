package ca.ualberta.papaya.models;

import android.graphics.Bitmap;

/**
 * Created by mghumphr on 3/10/16.
 */
public class Photo extends ElasticModel {

    protected transient final Class<?> kind = Photo.class;

    private Bitmap thumbnail;
    private Bitmap image;

    public Photo(){
        // todo: construct as blank photo?
    }

    public void setImage(Bitmap original){

    }

    public Bitmap getThumbnail(){
        return thumbnail;
    }

    public Bitmap getImage(){
        return image;
    }

}
