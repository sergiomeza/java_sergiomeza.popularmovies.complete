package info.sergiomeza.popularmovies.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import info.sergiomeza.popularmovies.ui.view.DetailView;

/**
 * Created by sergiomeza on 4/6/17.
 */

public class AsyncQueryHandlerDetail extends AsyncQueryHandler {
    DetailView mDetailView;

    public AsyncQueryHandlerDetail(ContentResolver cr, DetailView mDetailView) {
        super(cr);
        this.mDetailView = mDetailView;
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        super.onInsertComplete(token, cookie, uri);
        if(uri != null) {
            mDetailView.onFavoriteAdded(uri.toString());
        }
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);
        mDetailView.isFavorite(cursor.moveToNext());
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        super.onDeleteComplete(token, cookie, result);
        if(result > 0)
            mDetailView.onFavoriteDeleted(false);
        else
            mDetailView.onFavoriteDeleted(true);
    }
}
