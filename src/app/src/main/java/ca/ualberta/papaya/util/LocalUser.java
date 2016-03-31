package ca.ualberta.papaya.util;

import android.content.SharedPreferences;

import ca.ualberta.papaya.R;
import ca.ualberta.papaya.models.User;

/**
 * Created by mghumphr on 3/31/16.
 */
public class LocalUser {

    private static String userId = null;
    private static User user = null;

    private static String PREF_NAME = "LocalUser";
    private static String USER_ID_KEY = "LocalUser";

    public static String getId(){
        if(userId == null){
            SharedPreferences sharedPref = Ctx.get().getSharedPreferences(PREF_NAME, Ctx.get().MODE_PRIVATE);
            userId = sharedPref.getString(USER_ID_KEY, null);
        }
        return userId;
    }

    public static void setId(String id){
        userId = id;
        SharedPreferences sharedPref = Ctx.get().getSharedPreferences(PREF_NAME, Ctx.get().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER_ID_KEY, userId);
    }

    public static void getUser(final Observer observer){
        if(user != null){
            observer.update(user);
        } else {
            String id = getId();
            if (id == null) {
                observer.update(null);
            } else {
                User.getById(new Observer<User>() {
                    @Override
                    public void update(User u) {
                        user = u;
                        observer.update(user);
                    }
                }, User.class, id);
            }
        }
    }

}
