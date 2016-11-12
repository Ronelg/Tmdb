package com.tmdb.android.io;

import android.content.ContentProviderOperation;
import android.content.Context;
import java.util.ArrayList;

/**
 * Created by ronel on 10/11/2016.
 */

public abstract class BaseHandler<T> {

    protected static Context mContext;

    public BaseHandler(Context context) {
        mContext = context;
    }

    public abstract void makeContentProviderOperations(ArrayList<ContentProviderOperation> list);

    public abstract void process(T elements);
}
