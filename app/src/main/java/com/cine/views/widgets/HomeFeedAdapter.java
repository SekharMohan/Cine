package com.cine.views.widgets;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import com.cine.R;

/**
 * Created by DELL on 19-03-2017.
 */

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.MyViewHolder> {
    @Override
    public HomeFeedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_row_layout, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(HomeFeedAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
