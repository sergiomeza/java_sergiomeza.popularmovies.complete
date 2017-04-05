package info.sergiomeza.popularmovies.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sergiomeza on 4/4/17.
 */

public class Const {
    public static final String API_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static final String API_KEY = "7eab02746f447d2dfb75cce7f37506ca";
    public static final String DETAIL_DATA = "MOVIE_DATA";

    public enum ApiMethods {
        POPULAR("POPULAR"),
        TOP_RATED("TOP"),
        FAVS("FAVS");

        private final String state;
        public final String getState() {
            return this.state;
        }

        ApiMethods(String state) {
            this.state = state;
        }
    }

    public static Retrofit retrofitSingleton(){
        return new Retrofit.Builder()
                .baseUrl(Const.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
