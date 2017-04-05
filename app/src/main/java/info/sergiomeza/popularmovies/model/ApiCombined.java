package info.sergiomeza.popularmovies.model;

/**
 * Created by sergiomeza on 4/5/17.
 */

public class ApiCombined {
    public ApiResponseVideos mVideos;
    public ApiResponseReviews mReviews;

    public ApiCombined(ApiResponseVideos mVideos, ApiResponseReviews mReviews) {
        this.mVideos = mVideos;
        this.mReviews = mReviews;
    }
}
