package info.sergiomeza.popularmovies.presenter;

import android.content.AsyncQueryHandler;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import com.google.gson.Gson;

import info.sergiomeza.popularmovies.Api;
import info.sergiomeza.popularmovies.BuildConfig;
import info.sergiomeza.popularmovies.R;
import info.sergiomeza.popularmovies.data.AsyncQueryHandlerDetail;
import info.sergiomeza.popularmovies.data.MoviesContract;
import info.sergiomeza.popularmovies.model.ApiCombined;
import info.sergiomeza.popularmovies.model.ApiResponseReviews;
import info.sergiomeza.popularmovies.model.ApiResponseVideos;
import info.sergiomeza.popularmovies.model.Movie;
import info.sergiomeza.popularmovies.ui.view.DetailView;
import info.sergiomeza.popularmovies.util.Const;
import info.sergiomeza.popularmovies.util.Util;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sergiomeza on 4/4/17.
 */

public class DetailPresenter {
    private DetailView mDetailView;
    private Context mContext;
    private Api api;

    private final int GET_OPERATION = 11;
    private final int DELETE_OPERATION = 12;
    private final int ADD_OPERATION = 13;
    private AsyncQueryHandlerDetail mQueryHandler;

    public DetailPresenter(DetailView mDetailView, Context mContext) {
        this.mDetailView = mDetailView;
        this.mContext = mContext;

        /*
         * Create Retrofit
         */
        this.api = new Retrofit.Builder()
                .baseUrl(Const.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(Api.class);

        //Async query handler for the View
        mQueryHandler = new AsyncQueryHandlerDetail(mContext.getContentResolver(),
                mDetailView);
    }

    /**
     * @param mIntent
     */
    public void loadMovie(Intent mIntent){
        if(mIntent.getExtras().containsKey(Const.DETAIL_DATA)) {
            Movie mMovie = mIntent.getParcelableExtra(Const.DETAIL_DATA);
            mDetailView.onMovieLoaded(mMovie);
        }
        else {
            mDetailView.onMovieError(mContext.getString(R.string.error_no_data_detail));
        }
    }

    /**
     * @param mMovie
     * Add a movie to the favorites
     */
    public void addToFavorite(Movie mMovie){
        ContentValues mCv = new ContentValues();
        mCv.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_ID, mMovie.getId());
        //I'm Saving the Movie Movie in JSON format to avoid the creation of every Column
        mCv.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_DATA, new Gson().toJson(mMovie));
        //Calling the async handler
        mQueryHandler.startInsert(ADD_OPERATION, null, MoviesContract.FavoritesEntry.CONTENT_URI, mCv);
    }

    /**
     * @param mMovieId
     * @return
     * Check if the movie is already a favorite movie
     */
    public void getIffavorite(int mMovieId){
        Uri mUri = ContentUris.withAppendedId(MoviesContract.FavoritesEntry.CONTENT_URI, mMovieId);
        mQueryHandler.startQuery(GET_OPERATION, null, mUri, null, null, null, null);
    }

    /**
     * @param mMovieId
     * Delete the movie from the favorites
     */
    public void removeFromfavorite(int mMovieId){
        Uri mUri = ContentUris.withAppendedId(MoviesContract.FavoritesEntry.CONTENT_URI, mMovieId);
        mQueryHandler.startDelete(DELETE_OPERATION, null, mUri, null, null);
    }

    /**
     * @param mMovieId
     * Load Movie videos using observable and rx to make the request on the same time.
     * Thanks for the advice :))
     */
    public void loadMovieReviewsVideos(int mMovieId){
        if(new Util(this.mContext).isConnectedToInternet()){
            Observable.zip(api.getMovieVideos(mMovieId, BuildConfig.THE_MOVIE_DB_API_TOKEN),
                    api.getMovieReviews(mMovieId, BuildConfig.THE_MOVIE_DB_API_TOKEN),
                    new BiFunction<ApiResponseVideos, ApiResponseReviews, ApiCombined>(){
                        @Override
                        public ApiCombined apply(ApiResponseVideos apiResponseVideos,
                                                       ApiResponseReviews apiResponseReviews) throws Exception {
                            return new ApiCombined(apiResponseVideos, apiResponseReviews);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ApiCombined>() {
                        @Override
                        public void onSubscribe(Disposable d) {}

                        @Override
                        public void onNext(ApiCombined value) {
                            mDetailView.onVideoReviewsLoaded(value);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mDetailView.onListError(e.getMessage());
                        }

                        @Override
                        public void onComplete() {}
                    });

        }
    }
}
