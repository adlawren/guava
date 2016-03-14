package ca.ualberta.papaya.models;

import android.graphics.Bitmap;

/**
 * Created by mghumphr on 3/10/16.
 */
public class Photo extends ElasticModel {

    protected transient Class<?> kind;

    private Bitmap thumbnail;
    private Bitmap image;

    public Photo(){
        super();
        kind = Photo.class;
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
