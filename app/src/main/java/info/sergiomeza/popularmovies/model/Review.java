package info.sergiomeza.popularmovies.model;

/**
 * Created by sergiomeza on 4/5/17.
 */

public class Review {
    public String id;
    public String author;
    public String content;
    public String url;

    public Review(String  id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }
}
