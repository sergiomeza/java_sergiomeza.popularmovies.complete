package info.sergiomeza.popularmovies.model;

/**
 * Created by sergiomeza on 4/5/17.
 */

public class Video {
    public String id;
    public String key;
    public String name;
    public String type;

    public Video() {
    }

    public Video(String id, String key, String name, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Video{" +
                "name='" + name + '\'' +
                '}';
    }
}
