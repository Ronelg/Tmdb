package com.tmdb.android.ui.moviedetails;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tmdb.android.R;
import com.tmdb.android.data.TmdbItem;
import com.tmdb.android.io.model.Trailer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronel on 12/11/2016.
 */

public class TrailersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Activity host;
    private final LayoutInflater layoutInflater;
    private List<Trailer> items;

    public TrailersAdapter(Activity hostActivity){
        this.host = hostActivity;
        layoutInflater = LayoutInflater.from(host);
        items = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final TrailerHolder holder = new TrailerHolder(layoutInflater.inflate(
                R.layout.trailers_list_content, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Trailer trailer = items.get(position);

        TrailerHolder trailerHolder = (TrailerHolder) holder;
        trailerHolder.title.setText(trailer.name);
        trailerHolder.subtitle.setText(trailer.type);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Trailer> newItems){
        this.items = newItems;
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    /* package */  class TrailerHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView subtitle;

        TrailerHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.trailer_title);
            subtitle = (TextView) itemView.findViewById(R.id.trailer_subtitle);
        }
    }
}
