package info.sergiomeza.popularmovies.model;

import java.util.List;

/**
 * Created by sergiomeza on 4/5/17.
 */

public class ApiResponseReviews {
    int page = 0;
    public List<Review> results;
    int total_results;
    int total_pages;

    public ApiResponseReviews(int page, List<Review> results, int total_results, int total_pages) {
        this.page = page;
        this.results = results;
        this.total_results = total_results;
        this.total_pages = total_pages;
    }
}
