package com.cine.views.widgets;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import com.cine.R;
import com.cine.service.model.FeedModel;
import com.squareup.picasso.Picasso;


/**
 * Created by DELL on 19-03-2017.
 */

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.MyViewHolder> {

    private FeedModel.Commonwall_posts[] commonwall_posts;
    Context mContext;

    public HomeFeedAdapter(FeedModel.Commonwall_posts[] commonwall_posts, Context mcontext) {
        this.mContext = mcontext;
        this.commonwall_posts = commonwall_posts;
    }

    @Override
    public HomeFeedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_row_layout, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(HomeFeedAdapter.MyViewHolder holder, int position) {
        FeedModel.Commonwall_posts post = commonwall_posts[position];
       /* "post_id":"37",
                "post_username":"iamsanthosh",
                "post_text":"",
                "post_ucat":"Direction",
                "post_uscat":"Movie Director",
                "post_langid":"4",
                "post_date":"1480787483",
                "post_photos":"1480787492236474623.jpg",
                "post_video_url":null,
                "post_comments":"",
                "post_comment_replies":"",
                "post_likes":""*/
       if(post.getPost_photos()==null){
           holder.feedImageView.setVisibility(View.GONE);
           holder.feedVideoView.setVisibility(View.VISIBLE);
           if(post.getPost_video_url()!=null) {
               holder.feedVideoView.setVideoURI(Uri.parse(post.getPost_video_url()));
               holder.feedVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                   @Override
                   public boolean onError(MediaPlayer mp, int what, int extra) {
                       Log.d("video", "setOnErrorListener ");
                       return true;
                   }
               });
           }

       }else{
           holder.feedVideoView.setVisibility(View.GONE);
           holder.feedImageView.setVisibility(View.VISIBLE);
           if(post.getPost_photos()!=null)
           Picasso.with(mContext).load("http://www.buyarecaplates.com/vpb-wall-photos/"+post.getPost_photos()).into(holder.feedImageView);
       }

    }

    @Override
    public int getItemCount() {
        return commonwall_posts.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView userProfilePic;
        public AppCompatTextView hoursView, nameAndProfessionView, languageView, userNameCommented, commmentedText;
        public AppCompatImageView feedImageView;
        public VideoView feedVideoView;
        public Button likeButton, commentButton, replyForComment;
        public AppCompatImageButton sendReply;
        public AppCompatEditText commentEditText;

        public MyViewHolder(View view) {
            super(view);
            userProfilePic = (CircularImageView) view.findViewById(R.id.feedUserProfilePic);
            hoursView = (AppCompatTextView) view.findViewById(R.id.hoursView);
            nameAndProfessionView = (AppCompatTextView) view.findViewById(R.id.nameAndProfessionView);
            languageView = (AppCompatTextView) view.findViewById(R.id.languageView);
            commmentedText = (AppCompatTextView) view.findViewById(R.id.commentedText);
            userNameCommented = (AppCompatTextView) view.findViewById(R.id.userNameWhoCommented);
            feedImageView = (AppCompatImageView) view.findViewById(R.id.feedImageView);
            feedVideoView = (VideoView) view.findViewById(R.id.feedVideoView);
            likeButton = (Button) view.findViewById(R.id.likeButton);
            commentButton = (Button) view.findViewById(R.id.commentButton);
            replyForComment = (Button) view.findViewById(R.id.replyForCommentButton);
            sendReply = (AppCompatImageButton) view.findViewById(R.id.sendComment);
            commentEditText = (AppCompatEditText) view.findViewById(R.id.commentEditText);


        }
    }
}
