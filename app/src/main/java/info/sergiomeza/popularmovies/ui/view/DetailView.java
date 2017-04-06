package info.sergiomeza.popularmovies.ui.view;

import java.util.List;

import info.sergiomeza.popularmovies.model.ApiCombined;
import info.sergiomeza.popularmovies.model.Movie;
import info.sergiomeza.popularmovies.model.Review;
import info.sergiomeza.popularmovies.model.Video;

/**
 * Created by sergiomeza on 4/4/17.
 */

public interface DetailView {
    void onMovieLoaded(Movie mMovie);

    void onVideoReviewsLoaded(ApiCombined mCombined);

    void onListError(String mErrorMessage);

    void onMovieError(String mError);

    void onFavoriteAdded(String mUri);

    void isFavorite(boolean mFav);

    void onFavoriteDeleted(boolean mError);
}
