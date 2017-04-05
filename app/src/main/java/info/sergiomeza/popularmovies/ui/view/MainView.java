package info.sergiomeza.popularmovies.ui.view;

import java.util.List;

import info.sergiomeza.popularmovies.model.ApiResponse;
import info.sergiomeza.popularmovies.model.Movie;

/**
 * Created by sergiomeza on 4/4/17.
 */

public interface MainView {
    void showLoading(boolean mResfresh);

    void hideLoading(boolean mError);

    void onRequestSuccess(ApiResponse mResponse);

    void onFavoritesSuccess(List<Movie> mFavorites);

    void onRequestError(String mErrorMessage);
}
