package info.sergiomeza.popularmovies.model;

import java.util.List;

/**
 * Created by sergiomeza on 4/4/17.
 */

public class ApiResponse {
     int page = 0;
     List<Movie> results;
     int total_results;
     int total_pages;

    public ApiResponse(int page, List<Movie> results, int total_results, int total_pages) {
        this.page = page;
        this.results = results;
        this.total_results = total_results;
        this.total_pages = total_pages;
    }

    public List<Movie> getResults() {
        return results;
    }
}
