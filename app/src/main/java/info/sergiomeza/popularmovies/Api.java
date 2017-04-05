package info.sergiomeza.popularmovies;

import android.content.Intent;

import info.sergiomeza.popularmovies.model.ApiResponse;
import info.sergiomeza.popularmovies.model.ApiResponseReviews;
import info.sergiomeza.popularmovies.model.ApiResponseVideos;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sergiomeza on 4/4/17.
 */

public interface Api {
    @GET("popular/")
    Call<ApiResponse> getPopular(@Query("api_key")String mApikey);

    @GET("top_rated/")
    Call<ApiResponse> getTop(@Query("api_key")String mApikey);

    @GET("{id}/videos")
    Observable<ApiResponseVideos> getMovieVideos(@Path("id")Integer mId,
                                                 @Query("api_key")String mApikey);

    @GET("{id}/reviews")
    Observable<ApiResponseReviews> getMovieReviews(@Path("id")Integer mId,
                                                       @Query("api_key")String mApikey);
}
