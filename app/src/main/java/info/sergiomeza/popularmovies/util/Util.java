package info.sergiomeza.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import info.sergiomeza.popularmovies.R;

/**
 * Created by sergiomeza on 4/4/17.
 */

public class Util {
    private Context mContext;

    public Util(Context mContext) {
        this.mContext = mContext;
    }

    public Boolean isConnectedToInternet(){
        ConnectivityManager cm = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public View inflateError(String mMessage) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.app_status_view, null);
        TextView mText = (TextView) mView.findViewById(R.id.mTxtMessageStatus);
        mText.setText(mMessage);
        return mView;
    }
}
