package ttc.project.filmku;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by AR-Laptop on 12/10/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder> {
    Context context;
    ArrayList<String> reviews, reviewers;

    public ReviewAdapter(Context context) {
        this.context = context;
    }

    public void swapData(ArrayList<String> reviews, ArrayList<String> reviewers){
        this.reviews = reviews;
        this.reviewers = reviewers;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.tvReview.setText("\""+reviews.get(position)+"\"");
        holder.tvReviewer.setText("by \""+reviewers.get(position)+"\"");
    }

    @Override
    public int getItemCount() {
        if (reviews==null)return 0;
        return reviews.size();
    }
}
