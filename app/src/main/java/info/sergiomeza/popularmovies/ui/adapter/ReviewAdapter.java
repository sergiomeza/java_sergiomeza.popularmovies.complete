package info.sergiomeza.popularmovies.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.sergiomeza.popularmovies.R;
import info.sergiomeza.popularmovies.model.Review;

/**
 * Created by sergiomeza on 4/5/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<Review> mListReview;

    public ReviewAdapter(List<Review> mListVideo) {
        this.mListReview = mListVideo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        return new ReviewAdapter.ViewHolder(mInflater.inflate(R.layout.item_review, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(mListReview.get(position));
    }

    @Override
    public int getItemCount() {
        return mListReview.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mTxtMovieReviewAuthor) TextView mTxtAuthor;
        @BindView(R.id.mTxtMovieReviewContent) TextView mTxtContent;

        public ViewHolder(View itemView) {
            super(itemView);
            //Inject butterknife into the view
            ButterKnife.bind(this, itemView);
        }

        public void bindItem(final Review mReview){
            mTxtAuthor.setText(mReview.author);
            mTxtContent.setText(mReview.content);
        }
    }
}
