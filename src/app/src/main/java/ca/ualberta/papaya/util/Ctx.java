package ca.ualberta.papaya.util;

import android.content.Context;

/**
 * Created by mghumphr on 3/10/16.
 * Inspired by https://possiblemobile.com/2013/06/context/ [2016/03/10]
 *
 * Singleton used to pass Context information
 */
public class Ctx {

    private static Ctx sInstance;

    public static Ctx set(android.content.Context context) {
        return sInstance = new Ctx(context.getApplicationContext());
    }

    public static Context get() {
        return sInstance.mContext;
    }

    private android.content.Context mContext;

    private Ctx(android.content.Context context) {
        mContext = context;
    }
}
