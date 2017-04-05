package info.sergiomeza.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by sergiomeza on 4/5/17.
 */

public class MoviesContract {

    public static final String AUTHORITY = "info.sergiomeza.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoritesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites_movies";
        public static final String COLUMN_MOVIE_DATA = "movie_data";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
