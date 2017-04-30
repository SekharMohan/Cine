package com.cine.views.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.cine.R;
import com.cine.service.model.EventsModel;
import com.cine.utils.ValidationUtil;
import com.cine.views.fragments.Events;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;

/**
 * Created by DELL on 01-05-2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {

    ArrayList<EventsModel> eventsList;
    Context mContext;
    private static final String KEY = "AIzaSyDu8qNvu0-dSvuAmsPQmIkqT0gH1YfTf1k";

    public EventsAdapter(ArrayList<EventsModel> eventsList, Context mContext) {
        this.eventsList = eventsList;
        this.mContext = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.events_row_layout, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(EventsAdapter.MyViewHolder holder, int position) {
        EventsModel eventsModel = eventsList.get(position);
        holder.evDate.setText(!TextUtils.isEmpty(eventsModel.getEvent_date()) ? eventsModel.getEvent_date() : "");
        holder.evTitle.setText(!TextUtils.isEmpty(eventsModel.getEvent_title()) ? eventsModel.getEvent_title() : "");
        holder.evDescription.setText(!TextUtils.isEmpty(eventsModel.getEvent_description()) ? eventsModel.getEvent_description() : "");
        if(eventsModel.getEvent_video()!=null) {
            if(eventsModel.getEvent_video().equals("Not Available")){
                holder.evVideo.setVisibility(View.GONE);
            }else {
                String youTubeUrl= eventsModel.getEvent_video();
                final String videoId = youTubeUrl.substring(youTubeUrl.indexOf('=')+1, youTubeUrl.length());
                final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }

                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailView.setVisibility(View.VISIBLE);


                    }
                };
                holder.evYouTubeThump.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) mContext, KEY, videoId, 0, true, true);
                        mContext.startActivity(intent);
                    }
                });
                holder.evYouTubeThump.initialize(KEY, new YouTubeThumbnailView.OnInitializedListener() {
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
                holder.evVideo.setVisibility(View.GONE);
                //holder.firstPostVideo.setVideoURI(Uri.parse(adsList.get(position).getFirst_post_video()));
            }
        }else{
            holder.evRelativeLayoutVideo.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView evImage;
        public VideoView evVideo;
        public RelativeLayout evRelativeLayoutVideo;
        public AppCompatTextView evTitle, evDescription, evDate;
        public YouTubeThumbnailView evYouTubeThump;

        public MyViewHolder(View view) {
            super(view);
            evImage = (ImageView) view.findViewById(R.id.evImage);

            evVideo = (VideoView) view.findViewById(R.id.evVideoView);

            evYouTubeThump = (YouTubeThumbnailView) view.findViewById(R.id.ev_youtube_thumbnail);

            evRelativeLayoutVideo  = (RelativeLayout)view.findViewById(R.id.evRelVideoYouTubeView);
            evTitle = (AppCompatTextView)view.findViewById(R.id.evTitle);
            evDescription = (AppCompatTextView)view.findViewById(R.id.evDescription);
            evDate = (AppCompatTextView)view.findViewById(R.id.evDate);
        }
    }

}
