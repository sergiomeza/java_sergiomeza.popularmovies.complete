package info.sergiomeza.popularmovies.presenter;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

import info.sergiomeza.popularmovies.Api;
import info.sergiomeza.popularmovies.R;
import info.sergiomeza.popularmovies.data.MoviesContract;
import info.sergiomeza.popularmovies.model.ApiResponse;
import info.sergiomeza.popularmovies.model.Movie;
import info.sergiomeza.popularmovies.ui.view.MainView;
import info.sergiomeza.popularmovies.util.Const;
import info.sergiomeza.popularmovies.util.Util;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sergiomeza on 4/4/17.
 * MainView Presenter
 */

public class MainPresenter {
    private Api api;
    private Call<ApiResponse> mCall;
    private MainView mMainView;
    private Context mContext;
    /**
     * @param mMainView
     * @param mContext
     * Constructor
     */
    public MainPresenter(MainView mMainView, Context mContext) {
        this.mMainView = mMainView;
        this.mContext = mContext;
        /*
         * Create Retrofit
         */
        this.api = Const.retrofitSingleton().create(Api.class);
    }


    /**
     * @param mPage
     * @param mRefresh
     * @param mMethod
     * request Movies to the Server
     */
    public final void getMovies(int mPage, final boolean mRefresh, String mMethod) {
        if(new Util(this.mContext).isConnectedToInternet()){
            this.mMainView.showLoading(mRefresh);
            if(mMethod.equals(Const.ApiMethods.POPULAR.getState())){
                this.mCall = api.getPopular(Const.API_KEY);
            }

            if(mMethod.equals(Const.ApiMethods.TOP_RATED.getState())){
                this.mCall = api.getTop(Const.API_KEY);
            }

            if(mCall != null){
                mCall.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        Boolean mResponse;
                        try {
                            if (response.body() != null) {
                                mResponse = false;
                                mMainView.onRequestSuccess(response.body());
                            }
                            else {
                                mResponse = true;
                                mMainView.onRequestError(response.message());
                            }
                        }catch (Exception e){
                            mResponse = true;
                            mMainView.onRequestError(mContext.getString(R.string.error_request, e.getCause()));
                        }

                        mMainView.hideLoading(mResponse);
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        mMainView.hideLoading(true);
                        mMainView.onRequestError(mContext.getString(R.string.error_request, t.getCause()));
                    }
                });
            }
        } else {
            /*
             * Show the no internet connection error on the view
             */
            mMainView.onRequestError(mContext.getString(R.string.error_no_internet));
        }
    }

    /**
     * @param mRefresh
     * Get the movies marked as favorites
     */
    public final void getFavoriteMovies(final boolean mRefresh){
        this.mMainView.showLoading(mRefresh);
        AsyncQueryHandler mAsyncQuery = new AsyncQueryHandler(mContext.getContentResolver()){
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor mCursor) {
                super.onQueryComplete(token, cookie, mCursor);
                boolean mResponse;
                Gson mGson = new Gson();
                List<Movie> mFavorites = new ArrayList<>();
                if(mCursor.getCount() > 0) {
                    while (mCursor.moveToNext()) {
                        Movie mMovie = mGson.fromJson(mCursor.getString(
                                mCursor.getColumnIndex(MoviesContract.FavoritesEntry.COLUMN_MOVIE_DATA)),
                                Movie.class);
                        mFavorites.add(mMovie);
                    }
                    mMainView.onFavoritesSuccess(mFavorites);
                    mResponse = false;
                }
                else {
                    mResponse = true;
                    mMainView.onRequestError(mContext.getString(R.string.error_no_data,
                            mContext.getString(R.string.favorites)));
                }

                mMainView.hideLoading(mResponse);
            }
        };
        mAsyncQuery.startQuery(88, null, MoviesContract.FavoritesEntry.CONTENT_URI, null, null,
                null, null);
    }
}
