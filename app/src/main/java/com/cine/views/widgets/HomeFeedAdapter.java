package com.cine.views.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.cine.CineApplication;
import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.model.FeedModel;
import com.cine.service.model.userinfo.Cg_info;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.AppUtils;
import com.cine.utils.LocalStorage;
import com.cine.utils.ToastUtil;
import com.cine.views.activity.ImageViewer;
import com.cine.views.activity.MainActivity;
import com.cine.views.activity.MyWallActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;


/**
 * Created by DELL on 19-03-2017.
 */

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.MyViewHolder> {

    private FeedModel.Commonwall_posts[] commonwall_posts;
    private FeedModel.User_datas[] user_datas;
    Context mContext;
    private boolean isFromHome;
    public CineApplication app = CineApplication.getInstance();
    MediaController ctrl;
    private static final String KEY = "AIzaSyDu8qNvu0-dSvuAmsPQmIkqT0gH1YfTf1k";
    private  String videoUrl;
    String imageBitMap;


    public HomeFeedAdapter(FeedModel.Commonwall_posts[] commonwall_posts, Context mcontext, boolean isFromHome, FeedModel.User_datas[] user_datas) {

        this.commonwall_posts = commonwall_posts;
        this.mContext = mcontext;
        this.isFromHome = isFromHome;
        this.user_datas = user_datas;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_row_layout, parent, false);

        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final FeedModel.Commonwall_posts post = commonwall_posts[position];

        /*"post_id": "160",
                "post_username": "iamsanthosh",
                "post_user_fullname": "Santhosh kumar",
                "post_user_image": "photos/1488479400282922383.jpg",
                "post_text": "",
                "post_ucat": "Direction",
                "post_uscat": "Movie Director",
                "post_langid": "4",
                "post_date": "1488484987",
                "post_photos": "1488484992839836041.png, 1488484992839836041.png",
                "post_video_url": null,
                "post_comments": "43-Hi,44-Check",
                "post_comment_replies": "",
                "post_likes": ""
                "User_datas": [
                  {
                "full_name": "Prabu Vaiyapuri",
                "first_name": "Prabu",
                "last_name": "Vaiyapuri",
                "user_profile": "photos/1493663400272331991.jpg",
                "email": "prabu.conqer@gmail.com",
                "maincategory": "Casting",
                "subcategory": "Hero",
                "userlang_id": "4"
                }*/
        if(post.getPost_user_language()!=null) {
            holder.languageView.setText(AppUtils.getLanguage(post.getPost_user_language()));
        }else{
            holder.languageView.setText(AppUtils.getLanguage(post.getPost_langid()));
        }
        if(isFromHome){
            if(holder.getAdapterPosition() == 0){
                Picasso.with(mContext).load("http://www.buyarecaplates.com/" + user_datas[0].getUser_profile()).into(holder.statusProfilePic);
                holder.relativeLayoutStatusUpdate.setVisibility(View.VISIBLE);
                    holder.cardViewForStatus.setVisibility(View.VISIBLE);
                holder.pickPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) mContext).selectImage(new ICallBack<String>() {
                            @Override
                            public void onSuccess(String response) {
                                imageBitMap =response;
                            }

                            @Override
                            public void onFailure(String response) {

                            }
                        });
                    }
                });
                holder.pickVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) mContext).showVideoPopup(new ICallBack<String>() {
                            @Override
                            public void onSuccess(String response) {
                                videoUrl = response;
                            }

                            @Override
                            public void onFailure(String response) {

                            }
                        });
                    }
                });
                holder.sendStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String status =holder.statusEditText.getText().toString();
                        if(status!=null & !status.isEmpty()||(imageBitMap!=null&&!imageBitMap.isEmpty()) || (videoUrl!=null&&!videoUrl.isEmpty())) {
                            sendStatusApi(status);
                            holder.statusEditText.setText("");
                        }else {
                   updateUserToast("Enter the status to post ");
                        }
                    }
                });
                }else{
                holder.relativeLayoutStatusUpdate.setVisibility(View.GONE);
                holder.cardViewForStatus.setVisibility(View.GONE);
            }
        }else{
            holder.relativeLayoutStatusUpdate.setVisibility(View.GONE);
            holder.cardViewForStatus.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(post.getPost_likes())){
            holder.nameOfLikedPersonsTextView.setVisibility(View.VISIBLE);
            holder.nameOfLikedPersonsTextView.setText(post.getPost_likes() + "likes this");
        }else{
            holder.nameOfLikedPersonsTextView.setVisibility(View.GONE);
        }
        Picasso.with(mContext).load("http://www.buyarecaplates.com/" + user_datas[0].getUser_profile()).into(holder.feedCommentUserProfilePic);
        holder.hoursView.setText(AppUtils.getDateFromMilliSeconds(post.getPost_date()) + ", ");
       holder.nameView.setText(post.getPost_user_fullname());
        Picasso.with(mContext).load("http://www.buyarecaplates.com/" + post.getPost_user_image()).into(holder.userProfilePic);
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

             /*  MediaController mc = new MediaController(mContext);
               mc.setAnchorView(holder.feedVideoView);
               mc.setMediaPlayer(holder.feedVideoView);
               holder.feedVideoView.setVideoURI(Uri.parse(post.getPost_video_url()));
               holder.feedVideoView.setMediaController(mc);
               holder.feedVideoView.requestFocus();
               holder.feedVideoView.start();
               holder.feedVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
*/
               holder.relVideoYouTubeView.setVisibility(View.VISIBLE);
               if(post.getPost_video_url().contains("youtube") || post.getPost_video_url().contains("youtu.be")) {
                   holder.feedVideoView.setVisibility(View.GONE);
                   holder.youTubeThump.setVisibility(View.VISIBLE);

                   String youTubeUrl= post.getPost_video_url();
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
                   holder.youTubeThump.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) mContext, KEY, videoId , 0, true, true);
                           mContext.startActivity(intent);
                       }
                   });
                   holder.youTubeThump.initialize(KEY, new YouTubeThumbnailView.OnInitializedListener() {
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
               }else{
                   holder.youTubeThump.setVisibility(View.GONE);
                   holder.feedVideoView.setVisibility(View.VISIBLE);
                 /*  holder.feedVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                       @Override
                       public boolean onError(MediaPlayer mp, int what, int extra) {
                           Log.d("video", "setOnErrorListener ");
                           return true;
                       }
                   });

                  MediaController mc = new MediaController(mContext);
                   mc.setAnchorView(holder.feedVideoView);
                   mc.setMediaPlayer(holder.feedVideoView);*/
                   holder.feedVideoView.setVideoURI(Uri.parse(post.getPost_video_url()));
                   //holder.feedVideoView.setMediaController(mc);
                   //holder.feedVideoView.requestFocus();
                   //holder.feedVideoView.start();
               }

           }else{
               holder.feedVideoView.setVisibility(View.GONE);
               holder.youTubeThump.setVisibility(View.GONE);
               holder.relVideoYouTubeView.setVisibility(View.GONE);
           }

       }else{
           if(!TextUtils.isEmpty(post.getPost_text())){
               holder.postTextView.setVisibility(View.VISIBLE);
               holder.postTextView.setText(post.getPost_text());
           }else{
               holder.postTextView.setVisibility(View.GONE);
           }
           holder.relVideoYouTubeView.setVisibility(View.GONE);

           if(post.getPost_photos()!=null) {
               holder.feedImageView.setVisibility(View.VISIBLE);
               Picasso.with(mContext).load("http://www.buyarecaplates.com/vpb-wall-photos/" + post.getPost_photos()).into(holder.feedImageView);
           }else{
               holder.feedImageView.setVisibility(View.GONE);
           }
       }
        holder.nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wallIntent = new Intent(mContext, MyWallActivity.class);
                wallIntent.putExtra("username", post.getPost_username());
                mContext.startActivity(wallIntent);
            }
        });

        holder.likeButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                /// button click event
                if (holder.likeButton.getTextColors().getDefaultColor() == Color.parseColor("#FFFFFF")) {
                    holder.likeButton.setBackgroundResource(R.drawable.button_click);
                    holder.likeButton.setTextColor(Color.BLACK);
                } else {
                    holder.likeButton.setBackgroundResource(R.drawable.like_button_normal);
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
        holder.feedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, ImageViewer.class);
                //i.putExtra("docimage", documentFilesListResponseArrayList);


                i.putExtra("downloadurl", "http://www.buyarecaplates.com/vpb-wall-photos/");
                i.putExtra("postimage", post.getPost_photos());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commonwall_posts.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircularImageView userProfilePic, statusProfilePic, feedCommentUserProfilePic;
        public AppCompatTextView hoursView, nameView, professionView, languageView, userNameCommented, commmentedText, postTextView, nameOfLikedPersonsTextView;
        public AppCompatImageView feedImageView;
        public VideoView feedVideoView;
        public Button likeButton, commentButton, replyForComment,pickPhoto,pickVideo,sendStatus;
        public AppCompatImageButton sendReply;
        public EditText commentEditText,statusEditText;
        public CardView cardViewForStatus;
        public RelativeLayout relativeLayoutStatusUpdate, relVideoYouTubeView;

        public YouTubeThumbnailView youTubeThump;
        public YouTubePlayerSupportFragment youTubePlayerSupportFragment;

        public MyViewHolder(View view) {
            super(view);
            relativeLayoutStatusUpdate = (RelativeLayout)view.findViewById(R.id.relativeLayoutStatusUpdate);
            statusEditText = (EditText) view.findViewById(R.id.statusView);
            pickPhoto = (Button) view.findViewById(R.id.pickImage);
            pickVideo = (Button) view.findViewById(R.id.pickVideo);
            sendStatus = (Button) view.findViewById(R.id.sendStatus);
            relVideoYouTubeView  = (RelativeLayout)view.findViewById(R.id.relVideoYouTubeView);
            cardViewForStatus = (CardView)view.findViewById(R.id.card_view_status);
            userProfilePic = (CircularImageView) view.findViewById(R.id.feedUserProfilePic);
            statusProfilePic = (CircularImageView) view.findViewById(R.id.userProfilePic);
            feedCommentUserProfilePic = (CircularImageView) view.findViewById(R.id.feedCommentUserProfilePic);
            hoursView = (AppCompatTextView) view.findViewById(R.id.hoursView);
            nameView = (AppCompatTextView) view.findViewById(R.id.nameView);
            professionView = (AppCompatTextView) view.findViewById(R.id.professionView);
            languageView = (AppCompatTextView) view.findViewById(R.id.languageView);
            commmentedText = (AppCompatTextView) view.findViewById(R.id.commentedText);
            userNameCommented = (AppCompatTextView) view.findViewById(R.id.userNameWhoCommented);
            postTextView = (AppCompatTextView) view.findViewById(R.id.postTextView);
            nameOfLikedPersonsTextView = (AppCompatTextView) view.findViewById(R.id.nameOfLikedPersonsTextView);
            feedImageView = (AppCompatImageView) view.findViewById(R.id.feedImageView);
            feedVideoView = (VideoView) view.findViewById(R.id.feedVideoView);
            likeButton = (Button) view.findViewById(R.id.likeButton);
            commentButton = (Button) view.findViewById(R.id.commentButton);
            replyForComment = (Button) view.findViewById(R.id.replyForCommentButton);
            sendReply = (AppCompatImageButton) view.findViewById(R.id.sendComment);
            commentEditText = (EditText) view.findViewById(R.id.commentEditText);
            likeButton.setBackgroundResource(R.drawable.like_button_normal);
            likeButton.setTextColor(Color.parseColor("#FFFFFF"));
            youTubeThump = (YouTubeThumbnailView) view.findViewById(R.id.youtube_thumbnail);

           // youTubePlayerSupportFragment = new YouTubePlayerSupportFragment();


        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.likeButton:

                    break;
            }
        }
    }

    private  void sendStatusApi(String status){
        /*
REQUEST_METHOD = POST
		cg_api_req_name = cgnewpost
		cg_postuser_name = (pass current username)
		cg_page_id = feeds
cg_user_maincat = (pass user main category value)
cg_user_subcat = (pass user sub category value)
cg_user_clag = (pass user current language id  value)
cg_user_post = (pass user entered status text value)
*/

        if(app.getUserInfo()!=null){
            showLoader();
        Cg_info obj =   app.getUserInfo().getCg_info();
            Params query = new Params();
            if(imageBitMap!=null){
                query.addParam("cg_post_imgs","YES");
                query.addParam("tmp_name",imageBitMap);
            }else{
                if(videoUrl !=null){
                    query.addParam("cg_post_video",videoUrl);
                }
            }

                query.addParam("REQUEST_METHOD","POST");
            query.addParam("cg_api_req_name","cgnewpost");
            query.addParam("cg_postuser_name",obj.getCgusername());
            query.addParam("cg_page_id","feeds");
            query.addParam("cg_user_maincat",obj.getUsermaincategory());
            query.addParam("cg_user_subcat",obj.getUsersubcategory());
            query.addParam("cg_user_clag",obj.getUser_languageid());
            query.addParam("cg_user_post",status);
            WebServiceWrapper.getInstance().callService(mContext, WebService.FEEDS_URL, query, new ICallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject json = new JSONObject(response);
                        imageBitMap = null;
                        videoUrl = null;
if(json.getString("status").equals("1")){
    apiCall();

}else{
    dimissLoader();
    updateUserToast(json.getString("cg_msg"));
}

                    }catch (Exception e){
                        dimissLoader();
                        e.printStackTrace();
                    }

                    System.out.println();
                }

                @Override
                public void onFailure(String response) {
                    dimissLoader();
                }
            });
        }
    }

private void updateUserToast(String msg){
    ToastUtil.showErrorUpdate(mContext,msg);
}
private void dimissLoader(){
    Loader.dismissProgressBar();
}

private void showLoader(){
    Loader.showProgressBar(mContext);
}

    private void apiCall() {

        Params params=new Params();

        params.addParam("cg_api_req_name","getposts");
        params.addParam("cg_username",app.getUserInfo().getCg_info().getCgusername());
        WebServiceWrapper.getInstance().callService(mContext, WebService.FEEDS_URL, params, new ICallBack<String>() {
            @Override
            public void onSuccess(String response) {
                LocalStorage.feedModel = new Gson().fromJson(response,FeedModel.class);
                commonwall_posts = LocalStorage.feedModel.getCommonwall_posts();
                notifyDataSetChanged();
                dimissLoader();
            }

            @Override
            public void onFailure(String response) {
                updateUserToast(response);
              dimissLoader();
            }
        });
    }
}
