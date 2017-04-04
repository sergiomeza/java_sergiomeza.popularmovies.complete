package info.sergiomeza.popularmovies.ui.view;

import android.view.View;

import info.sergiomeza.popularmovies.model.Movie;

//
public interface OnMovieItemClickListener {
    void onItemClick(Movie mMovie, View mView);
}
