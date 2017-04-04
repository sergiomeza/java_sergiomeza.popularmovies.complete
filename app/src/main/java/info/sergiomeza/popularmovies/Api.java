package info.sergiomeza.popularmovies;

import info.sergiomeza.popularmovies.model.ApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sergiomeza on 4/4/17.
 */

public interface Api {
    @GET("popular/")
    Call<ApiResponse> getPopular(@Query("api_key")String mApikey);

    @GET("top_rated/")
    Call<ApiResponse> getTop(@Query("api_key")String mApikey);
}
