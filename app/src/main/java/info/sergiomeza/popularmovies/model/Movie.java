package info.sergiomeza.popularmovies.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import info.sergiomeza.popularmovies.util.Const;

/**
 * Created by sergiomeza on 4/4/17.
 */

public class Movie implements Serializable {
     int id;
     String poster_path;
     String overview;
     String release_date;
     String original_title = "";
     String title = "";
     String backdrop_path = "";
     double vote_average = 0.0;

     public Movie(int id, String poster_path, String overview, String release_date, String original_title,
                 String title, String backdrop_path, double vote_average) {
        this.id = id;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.original_title = original_title;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.vote_average = vote_average;
    }

    /**
     * Concatenation of kotlin making my life so much easier :))
     */
    public String smallImageUrl() {
        return Const.BASE_IMAGE_URL + "w185" + poster_path;
    }

    public String mediumImageUrl(){
        return Const.BASE_IMAGE_URL + "w342" + poster_path;
    }

    public String mediumbackgroundUrl() {
        return Const.BASE_IMAGE_URL + "w342" + backdrop_path;
    }

    public String parseDate(){
        Date mDate = null;
        try {
            mDate = new SimpleDateFormat("yyyy-MM-dd").parse(this.release_date);
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.setTime(mDate);
            return String.valueOf(mCalendar.get(Calendar.YEAR));
        } catch (ParseException e) {
            return "NaN";
        }
    }

    public int getId() {
        return id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public double getVote_average() {
        return vote_average;
    }
}
