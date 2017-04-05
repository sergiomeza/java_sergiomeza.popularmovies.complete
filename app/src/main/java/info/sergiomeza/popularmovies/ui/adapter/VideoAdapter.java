package info.sergiomeza.popularmovies.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.sergiomeza.popularmovies.R;
import info.sergiomeza.popularmovies.model.Video;
import info.sergiomeza.popularmovies.ui.view.OnItemClickListener;

/**
 * Created by sergiomeza on 4/5/17.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private OnItemClickListener onItemClick;
    private List<Video> mListVideo;

    public VideoAdapter(OnItemClickListener onItemClick, List<Video> mListVideo) {
        this.onItemClick = onItemClick;
        this.mListVideo = mListVideo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        return new VideoAdapter.ViewHolder(mInflater.inflate(R.layout.item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(mListVideo.get(position), onItemClick);
    }

    @Override
    public int getItemCount() {
        return mListVideo.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mImgVideoPlay) ImageView mImg;
        @BindView(R.id.mTxtMovieVideoName) TextView mTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            //Inject butterknife into the view
            ButterKnife.bind(this, itemView);
        }

        public void bindItem(final Video mVideo, final OnItemClickListener mOnItemClick){
            mTxt.setText(mVideo.name);
            mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClick.onItemClick(mVideo, mImg);
                }
            });
        }
    }
}
