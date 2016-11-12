package com.tmdb.android.data;

import com.google.gson.annotations.SerializedName;

/**
 * Base class for paging model types
 *
 * Created by ronel on 11/11/2016.
 */

public class TmdbItem {

    @SerializedName("iii")
    public final long id;
    public int page;

    public TmdbItem(long id) {
        this.id = id;
    }

    /**
     * Equals check based on the id field
     */
    @Override
    public boolean equals(Object o) {
        return (o.getClass() == getClass() && ((TmdbItem) o).id == id);
    }
}
