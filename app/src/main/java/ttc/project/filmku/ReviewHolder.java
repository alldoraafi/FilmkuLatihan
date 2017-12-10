package ttc.project.filmku;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by AR-Laptop on 12/10/2017.
 */

public class ReviewHolder extends RecyclerView.ViewHolder {
    public TextView tvReview, tvReviewer;
    public ReviewHolder(View itemView) {
        super(itemView);
        tvReview = itemView.findViewById(R.id.tvReview);
        tvReviewer = itemView.findViewById(R.id.tvReviewer);
    }
}
