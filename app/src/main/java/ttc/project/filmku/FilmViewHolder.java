package ttc.project.filmku;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by AR-Laptop on 11/25/2017.
 */

public class FilmViewHolder extends RecyclerView.ViewHolder {
    public TextView tvPosition, tvFilmName;
    public FilmViewHolder(View itemView) {
        super(itemView);
        tvPosition = (TextView) itemView.findViewById(R.id.tvPosition);
        tvFilmName = (TextView) itemView.findViewById(R.id.tvFilmName);
    }
}
