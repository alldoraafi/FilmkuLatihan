package ttc.project.filmku;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by AR-Laptop on 11/25/2017.
 */

public class FilmViewHolder extends RecyclerView.ViewHolder {
    public TextView tvFilmName;
    public ImageView ivPoster;
    public FilmViewHolder(View itemView) {
        super(itemView);
        tvFilmName = (TextView) itemView.findViewById(R.id.tvFilmName);
        ivPoster = itemView.findViewById(R.id.ivPoster);
    }
}
