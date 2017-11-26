package ttc.project.filmku;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by AR-Laptop on 11/25/2017.
 */

public class FilmAdapter extends RecyclerView.Adapter<FilmViewHolder> {
    Context mContext;
    ArrayList<String> titles;

    public FilmAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void swapData(ArrayList<String> titles){
        this.titles = titles;
    }

    @Override
    public FilmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_view, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmViewHolder holder, int position) {
        holder.tvPosition.setText(String.valueOf(position+1));
        holder.tvFilmName.setText(titles.get(position));

        final Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra("film_title", titles.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (titles==null) return 0;
        return titles.size();
    }
}
