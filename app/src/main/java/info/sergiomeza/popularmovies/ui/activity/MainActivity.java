package info.sergiomeza.popularmovies.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.sergiomeza.popularmovies.R;
import info.sergiomeza.popularmovies.model.ApiResponse;
import info.sergiomeza.popularmovies.model.Movie;
import info.sergiomeza.popularmovies.presenter.MainPresenter;
import info.sergiomeza.popularmovies.ui.adapter.MainAdapter;
import info.sergiomeza.popularmovies.ui.view.OnItemClickListener;
import info.sergiomeza.popularmovies.ui.view.MainView;
import info.sergiomeza.popularmovies.util.Const;
import info.sergiomeza.popularmovies.util.Util;

public class MainActivity extends AppCompatActivity implements MainView,
        OnItemClickListener {
    //Presenter
    MainPresenter mPresenter;
    //Actual selection
    private String mMethodSelected = Const.ApiMethods.POPULAR.getState();
    private MainAdapter mAdapter;

    //BindViews into the activity with butterknife
    @BindView(R.id.mRecycler) RecyclerView mRecycler;
    @BindView(R.id.mLinearError) LinearLayout mLinearError;
    @BindView(R.id.mSwipe) SwipeRefreshLayout mSwipe;
    @BindView(R.id.mProgressBar) ProgressBar mProgressBar;

    private static final String MOVIE_LIST_STORE = "MOVIE_STORE";
    private static final String MOVIE_LIST_STORE_POSITION = "MOVIE_POSITION";
    private static final String TITLE_TYPE_STATE = "TITLE_SELECTED";
    private static final String MOVIE_TYPE_SELECTION = "MOVIE_TYPE_SELE";
    private ArrayList<Movie> mListMovie;

    GridLayoutManager mGridLayoutManager;
    Parcelable mListState;
    private int mMenuSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Inject butterknife into the view
        ButterKnife.bind(this);
        mPresenter = new MainPresenter(this, this);

        final int columns = getResources().getInteger(R.integer.gallery_columns);
        mRecycler.setHasFixedSize(true);
        mGridLayoutManager = new GridLayoutManager(this, columns);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(mGridLayoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.setAdapter(mAdapter);

        if(savedInstanceState != null){
            mListMovie = savedInstanceState.getParcelableArrayList(MOVIE_LIST_STORE);
            mAdapter = new MainAdapter(this, mListMovie);
        } else {
            mPresenter.getMovies(1, false, mMethodSelected);
        }

        /*
         * Swipe to refresh
         */
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
                if(mMethodSelected.equals(Const.ApiMethods.FAVS.getState()))
                    mPresenter.getFavoriteMovies(true);
                else
                    mPresenter.getMovies(1, true, mMethodSelected);
            }
        });
    }

    /**
     * @param mMenuItem
     * @param mMethod
     * @return Set selection and changes the actionbar Title matchin the same title of the menu item
     */
    private Boolean setSelection(MenuItem mMenuItem, String mMethod){
        /*
         * Calling the presenter with the method selected
         */
        if(mMethod.equals(Const.ApiMethods.FAVS.getState()))
            mPresenter.getFavoriteMovies(false);
        else
            mPresenter.getMovies(1, false, mMethod);
        this.setTitle(mMenuItem.getTitle());
        mMethodSelected = mMethod;
        mMenuItem.setChecked(true);
        mMenuSelected = mMenuItem.getItemId();

        return true;
    }

    /**
     * @param menu
     * @return load Options menu into the activity and set the selection that invokes the presenter
     * to fetch the data from the api
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        /*
         * Set selected the option if the mMenuSelected is saved in the saveInstance
         */
        if(mMenuSelected != 0){
            MenuItem selected = menu.findItem(mMenuSelected);
            selected.setChecked(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @param item
     * @return when an item of the group radio menu is selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_popular:
                setSelection(item, Const.ApiMethods.POPULAR.getState());
                break;

            case R.id.menu_top_rated:
                setSelection(item, Const.ApiMethods.TOP_RATED.getState());
                break;

            case R.id.menu_favorites:
                setSelection(item, Const.ApiMethods.FAVS.getState());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        final int columns = getResources().getInteger(R.integer.gallery_columns);
        mRecycler.setLayoutManager(new GridLayoutManager(this, columns));
    }

    @Override
    public void showLoading(boolean mRefresh) {
        if(mRefresh) {
            mSwipe.setRefreshing(true);
        }
        else {
            mProgressBar.setVisibility(View.VISIBLE);
            mLinearError.setVisibility(View.GONE);
            mRecycler.setVisibility(View.GONE);
        }
    }

    /**
     * @param mError if some error has occurred
     *               we hide or show views depending if an error has occurred
     */
    @Override
    public void hideLoading(boolean mError) {
        mProgressBar.setVisibility(View.GONE);
        mSwipe.setRefreshing(false);
        if(mError) {
            mLinearError.setVisibility(View.VISIBLE);
            mRecycler.setVisibility(View.GONE);
        }
        else {
            mLinearError.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param mResponse Request is success, we pass the movie list to the adapter and display to
     *                  the user
     */
    @Override
    public void onRequestSuccess(ApiResponse mResponse) {
        if(!mResponse.getResults().isEmpty()){
            mAdapter = new MainAdapter(this, mResponse.getResults());
            mRecycler.setAdapter(mAdapter);
            mRecycler.getAdapter().notifyDataSetChanged();
            mListMovie = new ArrayList<>(mResponse.getResults());
        }
    }

    @Override
    public void onFavoritesSuccess(List<Movie> mFavorites) {
        if(!mFavorites.isEmpty()){
            mAdapter = new MainAdapter(this, mFavorites);
            mRecycler.setAdapter(mAdapter);
            mRecycler.getAdapter().notifyDataSetChanged();
            mListMovie = new ArrayList<>(mFavorites);
        }
    }

    /**
     * @param mErrorMessage Error message displayed in the view
     */
    @Override
    public void onRequestError(String mErrorMessage) {
        /*
         * We inflate the error view custom class into de Empty Linear layout of the XML file of
         * the activity passing the error string to display to the user
         */
        mLinearError.removeAllViews();
        mLinearError.addView(new Util(this).inflateError(mErrorMessage));
        Toast.makeText(this, mErrorMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * When a movie is clicked
     */
    @Override
    public void onItemClick(Object mMovie, View mView) {
        Intent mIntentDetail = new Intent(this, DetailActivity.class);
        mIntentDetail.putExtra(Const.DETAIL_DATA, (Movie)mMovie);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                mView, getString(R.string.transition_image));
        ActivityCompat.startActivity(this,
                mIntentDetail, options.toBundle());
    }

    /**
     * When the user is on Favorites and resumes the activity, we fetch again the
     * favorites list from DB
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(mListState != null){
            mGridLayoutManager.onRestoreInstanceState(mListState);
        }

        if(mMethodSelected.equals(Const.ApiMethods.FAVS.getState())){
            mPresenter.getFavoriteMovies(true);
        }
    }

    /**
     * @param outState
     * Saving the Current list of movies in the state with the title
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_LIST_STORE, mListMovie);
        outState.putString(TITLE_TYPE_STATE, this.getTitle().toString());
        outState.putInt(MOVIE_TYPE_SELECTION, mMenuSelected);
        mListState = mGridLayoutManager.onSaveInstanceState();
        outState.putParcelable(MOVIE_LIST_STORE_POSITION, mListState);
    }

    /**
     * @param savedInstanceState
     * When the app restores the state, we add the list of movies to the adapter of recyclerView
     * and set the title to the supportActionBar
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mListState = savedInstanceState.getParcelable(MOVIE_LIST_STORE_POSITION);
        mMenuSelected = savedInstanceState.getInt(MOVIE_TYPE_SELECTION);
        mRecycler.setAdapter(mAdapter);
        mRecycler.getAdapter().notifyDataSetChanged();
        this.setTitle(savedInstanceState.getString(TITLE_TYPE_STATE));
    }
}
