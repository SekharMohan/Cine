    package com.cine.views.widgets;

    import android.content.Context;
    import android.support.v7.widget.AppCompatEditText;
    import android.text.Editable;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseExpandableListAdapter;
    import android.widget.ExpandableListView;
    import android.widget.ImageButton;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
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
        private HashMap<Map<String ,String>, List<Map<String ,String>>> expandableListDetail = new HashMap<>();
        private HashMap<Map<String,String>,String> cmtIDList = new HashMap<>();
        Context mContext;
        TextView tvCommentCount;
        public int position;
        ExpandableListView listView;
        public CineApplication app = CineApplication.getInstance();
        FeedModel.Commonwall_posts post;
        public CommentsAdapter(Context context, FeedModel.Commonwall_posts post, int postion, TextView tvCommentCount,ExpandableListView listView){
            this.listView = listView;
            this.tvCommentCount = tvCommentCount;
            this.post = post;
            this.position = postion;
            if(post.getPost_comments()!=null &&!post.getPost_comments().isEmpty()) {
                setComments(post.getPost_comments());
            }
            mContext = context;
            this.inflater = LayoutInflater.from(context);
        }



        void setComments(String postComments){
        /*65-prabu944-Test 1,64-prabu944-test*/
        String[] commentArr = postComments.split(",");
        for(String commnt:commentArr){
            String cmtsDesc[] = commnt.split("-");
            Map<String,String> whoComment = new HashMap<>();
            whoComment.put(cmtsDesc[1],cmtsDesc[2]);
            commentItem.add(whoComment);
            /*65:41-prabu944-you,64:39-prabu944-hai,64:40-prabu944-hai hai hai,65:42-prabu944-lam,65:45-iamsanthosh-க்ஹ்*/
            if(post.getPost_comment_replies()!=null && !post.getPost_comment_replies().isEmpty()){
                String replySplit[] = post.getPost_comment_replies().split(",");
                List<Map<String,String>> replylist = new ArrayList<>();
                for(String reply:replySplit) {
                    String replyId[] = reply.split(":");
                    if(cmtsDesc[0].equals(replyId[0])){
                        String getReplyStr[] = replyId[1].split("-");
                        Map<String,String> whoReplied = new HashMap<>();
                        whoReplied.put(getReplyStr[1],getReplyStr[2]);
                        cmtIDList.put(whoReplied,replyId[0]);
                  replylist.add(whoReplied);
                    }
                }
                if(replylist.size()>0) {
                    expandableListDetail.put(whoComment, replylist);
                }
            }
        }if(commentItem.size()>0){
                expandAll();
            }

    }

        private void expandAll() {
            for(int i =0 ;i<commentItem.size();i++){
                listView.expandGroup(i);
            }
        }

        @Override
        public int getGroupCount() {
            return commentItem.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            Map<String ,String>  test = this.commentItem.get(groupPosition);
            int size = this.expandableListDetail.get(test)
                    .size();
            return size;

        }

        @Override
        public Object getGroup(int groupPosition) {
            return commentItem.get(groupPosition);
        }

        @Override
        public Map<String,String> getChild(int listPosition, int expandedListPosition) {
           return this.expandableListDetail.get(this.commentItem.get(listPosition))
                    .get(expandedListPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
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
    tvCommentCount.setText(commentItem.size()+" People are commented");

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
            final Map<String,String> child =  getChild(groupPosition, childPosition);
            if (view == null) {
                view = inflater.inflate(R.layout.reply_item, parent, false);
            }
            final ReplyItem comts = new ReplyItem(view);


            for(Map.Entry entry:child.entrySet()){
                comts.tvUserName.setText(entry.getKey().toString());
                comts.tvComments.setText(entry.getValue().toString());

            }
            comts.tvReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comts.llReplySection.setVisibility(View.VISIBLE);
                }
            });
            comts.replyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!comts.edtReply.getText().toString().isEmpty()){
                        callReplyApi(comts.edtReply.getText(),cmtIDList.get(child));
                        comts.llReplySection.setVisibility(View.GONE);
                    }
                }
            });
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
            @BindView(R.id.reply_section)
            LinearLayout llReplySection;
            @BindView(R.id.reply)
                    TextView tvReply;
            @BindView(R.id.comment_action_send)
            ImageButton replyButton ;
            @BindView(R.id.writeComment)
            AppCompatEditText edtReply;
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
        void callReplyApi(Editable text, String id){
            Loader.showProgressBar(mContext);
            Params params=new Params();

            params.addParam("REQUEST_METHOD","POST");
            params.addParam("cg_api_req_name","newreply");
            params.addParam("cg_username","prabu944");

            params.addParam("comment_id",id);
            params.addParam("reply",text);
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
