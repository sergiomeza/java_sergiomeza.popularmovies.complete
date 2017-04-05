package info.sergiomeza.popularmovies.model;

import java.util.List;

/**
 * Created by sergiomeza on 4/5/17.
 */

public class ApiResponseVideos {
    public int id;
    public List<Video> results;

    public ApiResponseVideos(int id, List<Video> results) {
        this.id = id;
        this.results = results;
    }
}
