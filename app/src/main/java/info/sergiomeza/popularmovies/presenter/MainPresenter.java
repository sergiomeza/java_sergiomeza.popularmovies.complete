package info.sergiomeza.popularmovies.presenter;

import android.content.Context;

import info.sergiomeza.popularmovies.Api;
import info.sergiomeza.popularmovies.R;
import info.sergiomeza.popularmovies.model.ApiResponse;
import info.sergiomeza.popularmovies.ui.view.MainView;
import info.sergiomeza.popularmovies.util.Const;
import info.sergiomeza.popularmovies.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sergiomeza on 4/4/17.
 * MainView Presenter
 */

public class MainPresenter {
    private Api api;
    private Call<ApiResponse> mCall;
    private MainView mMainView;
    private Context mContext;

    /**
     * @param mMainView
     * @param mContext
     * Constructor
     */
    public MainPresenter(MainView mMainView, Context mContext) {
        this.mMainView = mMainView;
        this.mContext = mContext;
        /*
         * Create Retrofit
         */
        this.api = Const.retrofitSingleton().create(Api.class);
    }


    /**
     * @param mPage
     * @param mRefresh
     * @param mMethod
     * request Movies to the Server
     */
    public final void getMovies(int mPage, final boolean mRefresh, String mMethod) {
        if(new Util(this.mContext).isConnectedToInternet()){
            this.mMainView.showLoading(mRefresh);
            if(mMethod.equals(Const.ApiMethods.POPULAR.getState())){
                this.mCall = api.getPopular(Const.API_KEY);
            }

            if(mMethod.equals(Const.ApiMethods.TOP_RATED.getState())){
                this.mCall = api.getTop(Const.API_KEY);
            }

            if(mCall != null){
                mCall.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        Boolean mResponse;
                        try {
                            if (response.body() != null) {
                                mResponse = false;
                                mMainView.onRequestSuccess(response.body());
                            }
                            else {
                                mResponse = true;
                                mMainView.onRequestError(response.message());
                            }
                        }catch (Exception e){
                            mResponse = true;
                            mMainView.onRequestError(mContext.getString(R.string.error_request, e.getCause()));
                        }

                        mMainView.hideLoading(mResponse);
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        mMainView.hideLoading(true);
                        mMainView.onRequestError(mContext.getString(R.string.error_request, t.getCause()));
                    }
                });
            }
        } else {
            /*
             * Show the no internet connection error on the view
             */
            mMainView.onRequestError(mContext.getString(R.string.error_no_internet));
        }
    }
}
