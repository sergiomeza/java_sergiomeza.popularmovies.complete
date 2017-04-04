package info.sergiomeza.popularmovies.ui.view;

import info.sergiomeza.popularmovies.model.ApiResponse;

/**
 * Created by sergiomeza on 4/4/17.
 */

public interface MainView {
    void showLoading(boolean mResfresh);

    void hideLoading(boolean mError);

    void onRequestSuccess(ApiResponse mResponse);

    void onRequestError(String mErrorMessage);
}
