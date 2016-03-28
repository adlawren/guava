package ca.ualberta.papaya.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;


/**
 * Created by mghumphr on 3/10/16.
 *
 * Model Class for photos that will be attached to a specific Thing object
 * @see ElasticModel
 */
public class Photo extends ElasticModel {

    protected transient Class<?> kind;


    private transient Bitmap image;
    private String imageBase64;

    public Photo(){
        super();
        kind = Photo.class;

        image = null;

    }

    public void setImage(Bitmap original){
        if(original != null){
            image = original;

            ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
            original.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);

            byte[] b = byteArrayBitmapStream.toByteArray();
            imageBase64 = Base64.encodeToString(b, Base64.DEFAULT);
        }
    }


    public Bitmap getImage(){

        if (image == null && imageBase64 != null) {
            byte[] decodeString = Base64.decode(imageBase64, Base64.DEFAULT);
            image = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        }
        return image;
    }
}
