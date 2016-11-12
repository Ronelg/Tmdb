package com.tmdb.android.util;

/**
 * Created by ronel on 11/11/2016.
 */

public class ImageUtils {

    public static final String PREFIX = "https://image.tmdb.org/t/p/original";

    public static String getImagePath(String url){
        return PREFIX + url;
    }
}
