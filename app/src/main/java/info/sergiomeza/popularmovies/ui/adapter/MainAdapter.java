package info.sergiomeza.popularmovies.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.sergiomeza.popularmovies.R;
import info.sergiomeza.popularmovies.model.Movie;
import info.sergiomeza.popularmovies.ui.view.OnItemClickListener;

/**
 * Created by sergiomeza on 4/4/17.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private OnItemClickListener onMoviewClick;
    private List<Movie> mListMovie;

    /**
     * @param onMoviewClick When click on the item happens
     * @param mListMovie Movie list Items
     */
    public MainAdapter(OnItemClickListener onMoviewClick, List<Movie> mListMovie) {
        this.onMoviewClick = onMoviewClick;
        this.mListMovie = mListMovie;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(mInflater.inflate(R.layout.item_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(mListMovie.get(position), onMoviewClick);
    }

    @Override
    public int getItemCount() {
        return mListMovie.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mImgGrid)ImageView mImg;

        public ViewHolder(View itemView) {
            super(itemView);
            //Inject butterknife into the view
            ButterKnife.bind(this, itemView);
        }

        private void bindItem(final Movie mMovie, final OnItemClickListener mMovieClick){
            /**
             * Load image url with picasso
             */
            Picasso.with(itemView.getContext())
                    .load(mMovie.smallImageUrl())
                    .into(mImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMovieClick.onItemClick(mMovie, mImg);
                }
            });
        }
    }
}