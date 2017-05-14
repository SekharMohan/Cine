    package com.cine.views.widgets;

    import android.content.Context;
    import android.text.Editable;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseExpandableListAdapter;
    import android.widget.ImageView;
    import android.widget.TextView;

    import com.cine.CineApplication;
    import com.cine.R;
    import com.cine.service.WebService;
    import com.cine.service.WebServiceWrapper;
    import com.cine.service.model.FeedModel;
    import com.cine.service.model.userinfo.Cg_info;
    import com.cine.service.network.Params;
    import com.cine.service.network.callback.ICallBack;
    import com.cine.utils.LocalStorage;
    import com.google.gson.Gson;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    import butterknife.BindView;
    import butterknife.ButterKnife;

    /**
     * Created by Sekhar Madhiyazhagan on 5/13/2017.
     */

    public class CommentsAdapter extends BaseExpandableListAdapter {
        private LayoutInflater inflater;

        List<Map<String ,String>> commentItem = new ArrayList<>();
        List<String> reply = new ArrayList<>();
        Context mContext;
        public int position;
        public CineApplication app = CineApplication.getInstance();
        FeedModel.Commonwall_posts post;
        public CommentsAdapter(Context context,FeedModel.Commonwall_posts post,int postion){
            this.post = post;
            this.position = postion;
            if(post.getPost_comments()!=null &&!post.getPost_comments().isEmpty()) {
                setComments(post.getPost_comments());
            }
           /* String[] commentArr = post.getPost_comments().split(",");
            setComments();
            comments = Arrays.asList(commentArr);
            String[] replyArr = post.getPost_comment_replies().split(",");
            reply =Arrays.asList(replyArr);*/
            mContext = context;
            this.inflater = LayoutInflater.from(context);
        }

    void setComments(String post){
        /*65-prabu944-Test 1,64-prabu944-test*/
        String[] commentArr = post.split(",");
        for(String commnt:commentArr){
            String cmtsDesc[] = commnt.split("-");
            Map<String,String> whoComment = new HashMap<>();
            whoComment.put(cmtsDesc[1],cmtsDesc[2]);

            commentItem.add(whoComment);
        }

    }

        @Override
        public int getGroupCount() {
            return commentItem.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return reply.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.comment_item, parent, false);
            }
            CommentsItem comts = new CommentsItem(convertView);

            Map<String , String> getComments = commentItem.get(groupPosition);
            for(Map.Entry entry:getComments.entrySet()){
                comts.tvUserName.setText(entry.getKey().toString());
                comts.tvComments.setText(entry.getValue().toString());

            }
    comts.tvCommentCount.setText(commentItem.size()+" people have discussed");

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
            if (view == null) {
                view = inflater.inflate(R.layout.reply_item, parent, false);
            }
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public class CommentsItem {
            @BindView(R.id.comment_avatar)
            ImageView imProfile;
            @BindView(R.id.comment_username)
            TextView tvUserName;
            @BindView(R.id.comment_text)
            TextView tvComments;
            @BindView(R.id.comment_date)
            TextView tvDate;
            @BindView(R.id.comment_count)
            TextView tvCommentCount;
            CommentsItem(View v){
                ButterKnife.bind(this,v);
            }

        }
        public class ReplyItem {
            @BindView(R.id.comment_avatar)
            ImageView imProfile;
            @BindView(R.id.comment_username)
            TextView tvUserName;
            @BindView(R.id.comment_text)
            TextView tvComments;
            @BindView(R.id.comment_date)
            TextView tvDate;
            ReplyItem(View v){
                ButterKnife.bind(this,v);
            }

        }

        public void callCommentsApi(Editable text){
            if(app.getUserInfo()!=null) {

                Cg_info obj = app.getUserInfo().getCg_info();
                Loader.showProgressBar(mContext);
                Params params = new Params();

                params.addParam("REQUEST_METHOD", "POST");
                params.addParam("cg_api_req_name", "newcomment");
                params.addParam("cg_username", obj.getCgusername());

                params.addParam("post_id",post.getPost_id());
                params.addParam("comment", text);
                WebServiceWrapper.getInstance().callService(mContext, WebService.FEEDS_URL, params, new ICallBack<String>() {
                    @Override
                    public void onSuccess(String response) {
                        apiCall();
                    }

                    @Override
                    public void onFailure(String response) {
                        dismissLoader();
                    }
                });
            }
        }

        private void dismissLoader() {
            Loader.dismissProgressBar();
        }
        private void apiCall() {

            Params params=new Params();

            params.addParam("cg_api_req_name","getposts");
            params.addParam("cg_username",app.getUserInfo().getCg_info().getCgusername());
            WebServiceWrapper.getInstance().callService(mContext, WebService.FEEDS_URL, params, new ICallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    LocalStorage.feedModel = new Gson().fromJson(response,FeedModel.class);
                    post = LocalStorage.feedModel.getCommonwall_posts()[position];
                    commentItem.clear();
                    if(post.getPost_comments()!= null && !post.getPost_comments().isEmpty()){
                        setComments(post.getPost_comments());
                    }
                    notifyDataSetChanged();
                    dismissLoader();
                }

                @Override
                public void onFailure(String response) {

                    dismissLoader();
                }
            });
        }
    }
