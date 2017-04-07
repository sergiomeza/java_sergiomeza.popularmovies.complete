package info.sergiomeza.popularmovies.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.sergiomeza.popularmovies.R;
import info.sergiomeza.popularmovies.model.ApiCombined;
import info.sergiomeza.popularmovies.model.Movie;
import info.sergiomeza.popularmovies.model.Review;
import info.sergiomeza.popularmovies.model.Video;
import info.sergiomeza.popularmovies.presenter.DetailPresenter;
import info.sergiomeza.popularmovies.ui.adapter.ReviewAdapter;
import info.sergiomeza.popularmovies.ui.adapter.VideoAdapter;
import info.sergiomeza.popularmovies.ui.view.DetailView;
import info.sergiomeza.popularmovies.ui.view.OnItemClickListener;

/**
 * Created by sergiomeza on 4/4/17.
 */

public class DetailActivity extends AppCompatActivity implements DetailView, OnItemClickListener {
    DetailPresenter mPresenter;
    Movie mMovie;
    Boolean mIsFavorite = false;
    MenuItem mMenuItem;

    VideoAdapter mVideoAdapter;
    ReviewAdapter mReviewAdapter;

    //Views
    @BindView(R.id.mCollapsingDetail) CollapsingToolbarLayout mCollapsingDetail;
    @BindView(R.id.mImgDetail)ImageView mImgDetail;
    @BindView(R.id.mToolbar) Toolbar mToolbar;
    @BindView(R.id.mImgPoster) ImageView mImgPoster;
    @BindView(R.id.mTxtMovieOverview) TextView mTxtMovieOverview;
    @BindView(R.id.mTxtMovieTitle)TextView mTxtMovieTitle;
    @BindView(R.id.mTxtMovieVotes) TextView mTxtMovieVotes;
    @BindView(R.id.mTxtMovieYear) TextView mTxtMovieYear;
    @BindView(R.id.mRecyclerVideos) RecyclerView mRecyclerVideos;
    @BindView(R.id.mRecyclerReviews) RecyclerView mRecyclerReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

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
         * Video and review Recyclers
         */
        initRecycler(mRecyclerVideos, mVideoAdapter);
        initRecycler(mRecyclerReviews, mReviewAdapter);

        /**
         * Load the View with the extras
         */
        mPresenter = new DetailPresenter(this, this);
        mPresenter.loadMovie(getIntent());
    }

    @Override
    public void onMovieLoaded(Movie mMovie) {
        this.mMovie = mMovie;
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
        mPresenter.getIffavorite(mMovie.id);

        //Load movie videos
        mPresenter.loadMovieReviewsVideos(mMovie.id);
    }

    @Override
    public void onVideoReviewsLoaded(ApiCombined mCombined) {
        /*
         * Video list items to recyclerview
         */
        if(!mCombined.mVideos.results.isEmpty()){
            mVideoAdapter = new VideoAdapter(this, mCombined.mVideos.results);
            mRecyclerVideos.setAdapter(mVideoAdapter);
            mRecyclerVideos.getAdapter().notifyDataSetChanged();
        }

        if(!mCombined.mReviews.results.isEmpty()){
            mReviewAdapter = new ReviewAdapter(mCombined.mReviews.results);
            mRecyclerReviews.setAdapter(mReviewAdapter);
            mRecyclerReviews.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onListError(String mErrorMessage) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        mMenuItem = menu.findItem(R.id.menu_favorite);
        mutatefavoriteIcon();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorite:
                if(mIsFavorite)
                    mPresenter.removeFromfavorite(this.mMovie.id);
                else
                    mPresenter.addToFavorite(this.mMovie);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieError(String mError) {
        Toast.makeText(this, mError, Toast.LENGTH_SHORT).show();
        supportFinishAfterTransition();
    }

    @Override
    public void onFavoriteAdded(String mUri) {
        mIsFavorite = true;
        mutatefavoriteIcon();
        Toast.makeText(getApplicationContext(),
            getString(R.string.added_to_favorites, mMovie.title),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isFavorite(boolean mFav) {
        mIsFavorite = mFav;
    }

    @Override
    public void onFavoriteDeleted(boolean mError) {
        mIsFavorite = false;
        mutatefavoriteIcon();
    }

    /**
     * Change aspect of the favorite Icon
     */
    private void mutatefavoriteIcon(){
        if(mMenuItem != null) {
            Drawable drawable = mMenuItem
                    .getIcon();
            if (drawable != null) {
                drawable = mIsFavorite ? ContextCompat.getDrawable(this, R.drawable.ic_star_black_24dp)
                        : ContextCompat.getDrawable(this, R.drawable.ic_star_border_black_24dp);
                drawable.mutate();
                drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                mMenuItem.setIcon(drawable);
            }
        }
    }

    @Override
    public void onItemClick(Object mObject, View mView) {
        if(mObject instanceof Video){
            Intent mYoutubeIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/watch?v=" + ((Video) mObject).key));
            if(mYoutubeIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mYoutubeIntent);
            }
            else {
                Toast.makeText(this, R.string.error_no_app_can_handle,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * @param mRecycler
     * @param mAdapter
     * Init recyclerView Method
     */
    private void initRecycler(RecyclerView mRecycler, RecyclerView.Adapter mAdapter){
        mRecycler.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(mLinearLayoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.setAdapter(mAdapter);
    }
}
