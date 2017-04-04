package info.sergiomeza.popularmovies.ui.activity;

import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.sergiomeza.popularmovies.R;
import info.sergiomeza.popularmovies.model.Movie;
import info.sergiomeza.popularmovies.presenter.DetailPresenter;
import info.sergiomeza.popularmovies.ui.view.DetailView;

/**
 * Created by sergiomeza on 4/4/17.
 */

public class DetailActivity extends AppCompatActivity implements DetailView {
    DetailPresenter mPresenter;

    //Views
    @BindView(R.id.mCollapsingDetail) CollapsingToolbarLayout mCollapsingDetail;
    @BindView(R.id.mImgDetail)ImageView mImgDetail;
    @BindView(R.id.mToolbar) Toolbar mToolbar;
    @BindView(R.id.mImgPoster) ImageView mImgPoster;
    @BindView(R.id.mTxtMovieOverview) TextView mTxtMovieOverview;
    @BindView(R.id.mTxtMovieTitle)TextView mTxtMovieTitle;
    @BindView(R.id.mTxtMovieVotes) TextView mTxtMovieVotes;
    @BindView(R.id.mTxtMovieYear) TextView mTxtMovieYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            /**
             * Initialize the toolbar
             */
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    supportFinishAfterTransition();
                }
            });

            /**
             * Load the View with the extras
             */
            mPresenter = new DetailPresenter(this, this);
            mPresenter.loadMovie(getIntent());
        }
    }

    @Override
    public void onMovieLoaded(Movie mMovie) {
        Picasso.with(this).load(mMovie.mediumbackgroundUrl())
                .into(mImgDetail);
        mCollapsingDetail.setTitle(mMovie.getTitle());
        mTxtMovieTitle.setText(mMovie.getOriginal_title());
        mTxtMovieYear.setText(mMovie.parseDate());
        mTxtMovieVotes.setText(getString(R.string.movie_votes,
                String.valueOf(mMovie.getVote_average())));
        Picasso.with(this).load(mMovie.smallImageUrl())
                .into(mImgPoster);
        mTxtMovieOverview.setText(mMovie.getOverview());
    }

    @Override
    public void onMovieError(String mError) {
        Toast.makeText(this, mError, Toast.LENGTH_SHORT).show();
        supportFinishAfterTransition();
    }
}
