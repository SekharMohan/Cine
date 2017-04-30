package com.cine.views.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.cine.R;
import com.cine.service.model.AdsModel;
import com.cine.utils.ToastUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 29-04-2017.
 */

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.MyViewHolder> {

    private ArrayList<AdsModel> adsList;
    private Context context;
    private static final String KEY = "AIzaSyDu8qNvu0-dSvuAmsPQmIkqT0gH1YfTf1k";

    public AdsAdapter(ArrayList<AdsModel> adsList, Context mContext) {
        this.adsList = adsList;
        this.context = mContext;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ad_row_layout, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(adsList.get(position).getFirst_post_images()!=null){
            if(adsList.get(position).getFirst_post_images().equals("Not Available")){
                holder.firstPostImage.setVisibility(View.GONE);
            }else {
                holder.firstPostImage.setVisibility(View.VISIBLE);
                Picasso.with(context).load("http://www.buyarecaplates.com/" + adsList.get(position).getFirst_post_images()).into(holder.firstPostImage);
            }
        }else{
            holder.firstPostImage.setVisibility(View.GONE);

        }

        if(adsList.get(position).getSecond_post_images()!=null) {
            if(adsList.get(position).getSecond_post_images().equals("Not Available")){
                holder.secondPostImage.setVisibility(View.GONE);
            }else {

                holder.secondPostImage.setVisibility(View.VISIBLE);
                Picasso.with(context).load("http://www.buyarecaplates.com/" + adsList.get(position).getSecond_post_images()).into(holder.secondPostImage);
            }
        }else{
            holder.secondPostImage.setVisibility(View.GONE);
        }

        if(adsList.get(position).getThird_post_images()!=null) {
            if(adsList.get(position).getThird_post_images().equals("Not Available")){
                holder.thirdPostImage.setVisibility(View.GONE);
            }else {

                holder.thirdPostImage.setVisibility(View.VISIBLE);
                Picasso.with(context).load("http://www.buyarecaplates.com/" + adsList.get(position).getThird_post_images()).into(holder.thirdPostImage);
            }
        }else{
            holder.thirdPostImage.setVisibility(View.GONE);
        }

        if(adsList.get(position).getFirst_post_video()!=null) {
            if(adsList.get(position).getFirst_post_video().equals("Not Available")){
                holder.firstPostVideo.setVisibility(View.GONE);
            }else {
                String youTubeUrl= adsList.get(position).getFirst_post_video();
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
                holder.youTubeThumpOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context, KEY, videoId, 0, true, true);
                        context.startActivity(intent);
                    }
                });
                holder.youTubeThumpOne.initialize(KEY, new YouTubeThumbnailView.OnInitializedListener() {
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
                holder.firstPostVideo.setVisibility(View.GONE);
                //holder.firstPostVideo.setVideoURI(Uri.parse(adsList.get(position).getFirst_post_video()));
            }
        }else{
            holder.firstPostVideo.setVisibility(View.GONE);
        }

        if(adsList.get(position).getSecond_post_video()!=null) {
            if(adsList.get(position).getSecond_post_video().equals("Not Available")){
                holder.secondPostVideo.setVisibility(View.GONE);
            }else {
                String youTubeUrl= adsList.get(position).getSecond_post_video();
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
                holder.youTubeThumpTwo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context, KEY, videoId, 0, true, true);
                        context.startActivity(intent);
                    }
                });
                holder.youTubeThumpTwo.initialize(KEY, new YouTubeThumbnailView.OnInitializedListener() {
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
                holder.secondPostVideo.setVisibility(View.GONE);
                //holder.secondPostVideo.setVideoURI(Uri.parse(adsList.get(position).getSecond_post_video()));
            }
        }else{
            holder.secondPostVideo.setVisibility(View.GONE);
        }

        if(adsList.get(position).getThird_post_video()!=null) {
            if(adsList.get(position).getThird_post_video().equals("Not Available")){
                holder.thirdPostVideo.setVisibility(View.GONE);
            }else {
                String youTubeUrl= adsList.get(position).getThird_post_video();
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
                holder.youTubeThumpThree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context, KEY, videoId, 0, true, true);
                        context.startActivity(intent);
                    }
                });
                holder.youTubeThumpThree.initialize(KEY, new YouTubeThumbnailView.OnInitializedListener() {
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
                holder.thirdPostVideo.setVisibility(View.GONE);
                //holder.thirdPostVideo.setVideoURI(Uri.parse(adsList.get(position).getThird_post_video()));
            }
        }else{
            holder.thirdPostVideo.setVisibility(View.GONE);
        }
     /*   Picasso.with(context).load("http://www.buyarecaplates.com/" + adsList.get(position).getFirst_post_images()).into(holder.firstPostImage);
        Picasso.with(context).load("http://www.buyarecaplates.com/" + adsList.get(position).getSecond_post_images()).into(holder.secondPostImage);
        Picasso.with(context).load("http://www.buyarecaplates.com/" + adsList.get(position).getThird_post_images()).into(holder.thirdPostImage);
        holder.firstPostVideo.setVisibility(View.GONE);
        holder.secondPostVideo.setVisibility(View.GONE);
        holder.thirdPostVideo.setVisibility(View.GONE);*/

    }


    @Override
    public int getItemCount() {
        return adsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView firstPostImage, secondPostImage, thirdPostImage;
        public VideoView firstPostVideo, secondPostVideo,thirdPostVideo;
        public RelativeLayout relativeLayoutVideoOne, relativeLayoutVideoTwo, relativeLayoutVideoThree;

        public YouTubeThumbnailView youTubeThumpOne, youTubeThumpTwo, youTubeThumpThree;

        public MyViewHolder(View view) {
            super(view);
            firstPostImage = (ImageView) view.findViewById(R.id.firstPostImage);
            secondPostImage = (ImageView) view.findViewById(R.id.secondPostImage);
            thirdPostImage = (ImageView) view.findViewById(R.id.thirdPostImage);
            firstPostVideo = (VideoView) view.findViewById(R.id.firstPostVideo);
            secondPostVideo = (VideoView) view.findViewById(R.id.secondPostVideo);
            thirdPostVideo = (VideoView) view.findViewById(R.id.thirdPostVideo);
            youTubeThumpOne = (YouTubeThumbnailView) view.findViewById(R.id.youtube_thumbnailFirst);
            youTubeThumpTwo = (YouTubeThumbnailView) view.findViewById(R.id.youtube_thumbnailtwo);
            youTubeThumpThree = (YouTubeThumbnailView) view.findViewById(R.id.youtube_thumbnail_three);
            relativeLayoutVideoOne  = (RelativeLayout)view.findViewById(R.id.relVideoYouTubeViewFirst);
            relativeLayoutVideoTwo  = (RelativeLayout)view.findViewById(R.id.relVideoYouTubeViewTwo);
            relativeLayoutVideoThree  = (RelativeLayout)view.findViewById(R.id.relVideoYouTubeViewThree);
        }
    }
}
