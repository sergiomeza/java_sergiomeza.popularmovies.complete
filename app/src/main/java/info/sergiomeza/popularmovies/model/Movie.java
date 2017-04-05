package info.sergiomeza.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import info.sergiomeza.popularmovies.util.Const;

/**
 * Created by sergiomeza on 4/4/17.
 */

public class Movie implements Parcelable {
    public int id;
    public String poster_path;
    public String overview;
    public String release_date;
    public String original_title = "";
    public String title = "";
    public String backdrop_path = "";
    public double vote_average = 0.0;


    public Movie() {
    }

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

    protected Movie(Parcel in) {
        id = in.readInt();
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        original_title = in.readString();
        title = in.readString();
        backdrop_path = in.readString();
        vote_average = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeString(original_title);
        parcel.writeString(title);
        parcel.writeString(backdrop_path);
        parcel.writeDouble(vote_average);
    }
}
