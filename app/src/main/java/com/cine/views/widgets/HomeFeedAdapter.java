package com.cine.views.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

import com.cine.R;
import com.cine.service.model.FeedModel;
import com.cine.views.activity.MyWallActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Created by DELL on 19-03-2017.
 */

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.MyViewHolder>  {

    private FeedModel.Commonwall_posts[] commonwall_posts;

    Context mContext;
    MediaController ctrl;
    private static final String KEY = "AIzaSyDu8qNvu0-dSvuAmsPQmIkqT0gH1YfTf1k";


    public HomeFeedAdapter(FeedModel.Commonwall_posts[] commonwall_posts, Context myWallActivity) {
        this.commonwall_posts = commonwall_posts;
        this.mContext = myWallActivity;


    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.feed_row_layout, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        FeedModel.Commonwall_posts post = commonwall_posts[position];
       /* "post_id": "37",
            "post_username": "iamsanthosh",
            "post_user_fullname": "Santhosh kumar",
            "post_user_image": "photos/1488479400282922383.jpg",
            "post_text": "",
            "post_ucat": "Direction",
            "post_uscat": "Movie Director",
            "post_langid": "4",
            "post_date": "1480787483",
            "post_photos": "1480787492236474623.jpg",
            "post_video_url": null,
            "post_comments": "",
            "post_comment_replies": "",
            "post_likes": ""*/

        if(!TextUtils.isEmpty(post.getPost_likes())){
            holder.nameOfLikedPersonsTextView.setVisibility(View.VISIBLE);
            holder.nameOfLikedPersonsTextView.setText(post.getPost_likes() + "likes this");
        }else{
            holder.nameOfLikedPersonsTextView.setVisibility(View.GONE);
        }
       holder.nameView.setText(post.getPost_username());
       holder.professionView.setText((!TextUtils.isEmpty(post.getPost_uscat())) ? ", " + post.getPost_uscat() : "");
       if(post.getPost_photos()==null){
           holder.feedImageView.setVisibility(View.GONE);

           if(!TextUtils.isEmpty(post.getPost_text())){
               holder.postTextView.setVisibility(View.VISIBLE);
               holder.postTextView.setText(post.getPost_text());
           }else{
               holder.postTextView.setVisibility(View.GONE);
           }
           if(post.getPost_video_url()!=null) {

               if(post.getPost_video_url().contains("youtube")) {
                   holder.youTubeThump.setVisibility(View.VISIBLE);

                   final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                       @Override
                       public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                       }

                       @Override
                       public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                           youTubeThumbnailView.setVisibility(View.VISIBLE);


                       }
                   };
                   holder.youTubeThump.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) mContext, KEY, "a5FvgdI6h-c", 0, true, true);
                           mContext.startActivity(intent);
                       }
                   });
                   holder.youTubeThump.initialize(KEY, new YouTubeThumbnailView.OnInitializedListener() {
                       @Override
                       public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                           youTubeThumbnailLoader.setVideo("94BzBOpv42g");
                           youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                       }

                       @Override
                       public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                           System.out.println(youTubeInitializationResult.toString());
                           //In case of failure
                       }
                   });
               }else{
                   holder.youTubeThump.setVisibility(View.GONE);

               }

           }else{
           }

       }else{
           if(!TextUtils.isEmpty(post.getPost_text())){
               holder.postTextView.setVisibility(View.VISIBLE);
               holder.postTextView.setText(post.getPost_text());
           }else{
               holder.postTextView.setVisibility(View.GONE);
           }


           if(post.getPost_photos()!=null) {
               holder.feedImageView.setVisibility(View.VISIBLE);
               Picasso.with(mContext).load("http://www.buyarecaplates.com/vpb-wall-photos/" + post.getPost_photos()).into(holder.feedImageView);
           }else{
               holder.feedImageView.setVisibility(View.GONE);
           }
       }

        holder.likeButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                /// button click event
                if (holder.likeButton.getTextColors().getDefaultColor() == Color.parseColor("#FFFFFF")) {
                    holder.likeButton.setBackgroundResource(R.drawable.button_click);
                    holder.likeButton.setTextColor(Color.BLACK);
                } else {
                    holder.likeButton.setBackgroundResource(R.drawable.feed_button_normal);
                    holder.likeButton.setTextColor(Color.parseColor("#FFFFFF"));

                }
            }
        });
        holder.commentEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(holder.commentEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }


    @Override
    public int getItemCount() {
        return commonwall_posts.length;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircularImageView userProfilePic;
        public AppCompatTextView hoursView, nameView, professionView, languageView, userNameCommented, commmentedText, postTextView, nameOfLikedPersonsTextView;
        public AppCompatImageView feedImageView;
        public YouTubeThumbnailView youTubeThump;
        public YouTubePlayerSupportFragment feedVideoView;
        public Button likeButton, commentButton, replyForComment;
        public AppCompatImageButton sendReply;
        public EditText commentEditText;

        public MyViewHolder(View view) {
            super(view);
            userProfilePic = (CircularImageView) view.findViewById(R.id.feedUserProfilePic);
            hoursView = (AppCompatTextView) view.findViewById(R.id.hoursView);
            nameView = (AppCompatTextView) view.findViewById(R.id.nameView);
            professionView = (AppCompatTextView) view.findViewById(R.id.professionView);
            languageView = (AppCompatTextView) view.findViewById(R.id.languageView);
            commmentedText = (AppCompatTextView) view.findViewById(R.id.commentedText);
            userNameCommented = (AppCompatTextView) view.findViewById(R.id.userNameWhoCommented);
            postTextView = (AppCompatTextView) view.findViewById(R.id.postTextView);
            nameOfLikedPersonsTextView = (AppCompatTextView) view.findViewById(R.id.nameOfLikedPersonsTextView);
            feedImageView = (AppCompatImageView) view.findViewById(R.id.feedImageView);
            youTubeThump = (YouTubeThumbnailView) view.findViewById(R.id.youtube_thumbnail);

                feedVideoView = new YouTubePlayerSupportFragment();


            likeButton = (Button) view.findViewById(R.id.likeButton);
            commentButton = (Button) view.findViewById(R.id.commentButton);
            replyForComment = (Button) view.findViewById(R.id.replyForCommentButton);
            sendReply = (AppCompatImageButton) view.findViewById(R.id.sendComment);
            commentEditText = (EditText) view.findViewById(R.id.commentEditText);
            likeButton.setBackgroundResource(R.drawable.feed_button_normal);
            likeButton.setTextColor(Color.parseColor("#FFFFFF"));

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.likeButton:

                    break;
            }
        }
    }
  }
