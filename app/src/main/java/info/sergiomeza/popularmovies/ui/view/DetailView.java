package info.sergiomeza.popularmovies.ui.view;

import info.sergiomeza.popularmovies.model.Movie;

/**
 * Created by sergiomeza on 4/4/17.
 */

public interface DetailView {
    void onMovieLoaded(Movie mMovie);

    void onMovieError(String mError);
}
