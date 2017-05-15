package com.cine.views.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cine.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

/**
 * Created by DELL on 14-05-2017.
 */

public class GalleryVideoAdapter extends RecyclerView.Adapter<GalleryVideoAdapter.MyViewHolder> {

    private List<String> videoList;
    private Context context;
    private static final String KEY = "AIzaSyDu8qNvu0-dSvuAmsPQmIkqT0gH1YfTf1k";

    public GalleryVideoAdapter(List<String> videoList, Context mContext) {
        this.videoList = videoList;
        this.context = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_gallery_row, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(!TextUtils.isEmpty(videoList.get(position))) {
            String youTubeUrl = videoList.get(position);
            final String videoId = youTubeUrl.substring(youTubeUrl.indexOf('=') + 1, youTubeUrl.length());
            final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                @Override
                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                }

                @Override
                public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                    youTubeThumbnailView.setVisibility(View.VISIBLE);


                }
            };
            holder.mwGalleryYoutubeThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context, KEY, videoId, 0, true, true);
                    context.startActivity(intent);
                }
            });
            holder.mwGalleryYoutubeThumb.initialize(KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                    youTubeThumbnailLoader.setVideo(videoId);
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    System.out.println(youTubeInitializationResult.toString());
                    //In case of failure
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public YouTubeThumbnailView mwGalleryYoutubeThumb;


        public MyViewHolder(View view) {
            super(view);
            mwGalleryYoutubeThumb = (YouTubeThumbnailView) view.findViewById(R.id.mwYoutubeThumbnail);

        }
    }
}

