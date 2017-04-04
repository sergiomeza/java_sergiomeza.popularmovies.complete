package info.sergiomeza.popularmovies.presenter;

import android.content.Context;
import android.content.Intent;

import info.sergiomeza.popularmovies.R;
import info.sergiomeza.popularmovies.model.Movie;
import info.sergiomeza.popularmovies.ui.view.DetailView;
import info.sergiomeza.popularmovies.util.Const;

/**
 * Created by sergiomeza on 4/4/17.
 */

public class DetailPresenter {
    private DetailView mDetailView;
    private Context mContext;

    public DetailPresenter(DetailView mDetailView, Context mContext) {
        this.mDetailView = mDetailView;
        this.mContext = mContext;
    }

    public void loadMovie(Intent mIntent){
        if(mIntent.getExtras().containsKey(Const.DETAIL_DATA)) {
            Movie mMovie = (Movie) mIntent.getSerializableExtra(Const.DETAIL_DATA);
            mDetailView.onMovieLoaded(mMovie);
        }
        else {
            mDetailView.onMovieError(mContext.getString(R.string.error_no_data_detail));
        }
    }
}
