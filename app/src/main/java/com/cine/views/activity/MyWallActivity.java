package com.cine.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cine.CineApplication;
import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.model.FeedModel;
import com.cine.service.model.GetCurrecntRequestsModel;
import com.cine.service.model.userinfo.UserPersonal;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.AppConstants;
import com.cine.utils.LocalStorage;
import com.cine.utils.ToastUtil;
import com.cine.utils.ValidationUtil;
import com.cine.views.widgets.CircularImageView;
import com.cine.views.widgets.HomeFeedAdapter;
import com.cine.views.widgets.Loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyWallActivity extends AppCompatActivity implements ICallBack<String>, View.OnClickListener {

    @BindView(R.id.myWallFeedView)
    public RecyclerView myWallFeedView;
    @BindView(R.id.nameTextView)
    public AppCompatTextView mwNameView;
    @BindView(R.id.professionTextView)
    public AppCompatTextView professionView;
    @BindView(R.id.genderStatusCityTextView)
    public AppCompatTextView genderCityStatusView;
    @BindView(R.id.birthdayTextView)
    public AppCompatTextView birthdayView;
    @BindView(R.id.recentProjectsTextView)
    public AppCompatTextView recentProjectsView;
    @BindView(R.id.aboutMeDescriptionTextView)
    public AppCompatTextView aboutMeView;
    @BindView(R.id.lastVisitedValueTextView)
    public AppCompatTextView lastVisitedView;
    @BindView(R.id.mwAwardsDescriptionTextView)
    public AppCompatTextView mwAwardsView;
    @BindView(R.id.mwCompletedProjectsDescriptionTextView)
    public AppCompatTextView mwCompletedProjView;
    @BindView(R.id.mwHobbiesDescriptionTextView)
    public AppCompatTextView mwHobbiesView;
    @BindView(R.id.mwKLDescriptionTextView)
    public AppCompatTextView mwKnownLangView;
    @BindView(R.id.mwRecentProjectsDescriptionTextView)
    public AppCompatTextView mwRecentProjView;
    @BindView(R.id.mwSkillsDescriptionTextView)
    public AppCompatTextView mwSkillsView;
    @BindView(R.id.mwProfileImage)
    public ImageView mwProfileImage;
    @BindView(R.id.contactReqLayout)
    LinearLayout contactReqLayout;
    @BindView(R.id.mwEmailReq)
    ImageButton mwEmailReq;
    @BindView(R.id.mwMobileNumReq)
    ImageButton mwMobileNumReq;
    @BindView(R.id.mwAddressReq)
    ImageButton mwAddressReq;
    @BindView(R.id.addressReqCount)
    TextView addressReqCount;
    @BindView(R.id.emailReqCount)
    TextView emailReqCount;
    @BindView(R.id.mobileReqCount)
    TextView mobileReqCount;

    CineApplication app = CineApplication.getInstance();
    private String userName;
    private String userType;
    private String userPicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wall);
        ButterKnife.bind(this);
        userName = getUserName();
        init();
        apiCallWallPost();
        setUserProfilePic(userName);

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(!userName.equals(app.getUserInfo().getCg_info().getCgusername())){
            AppConstants.isFromLanguage = false;
        }else{
            AppConstants.isFromLanguage = true;
        }
    }

    private String getUserName() {
        String username = "";
        if (getIntent().getExtras() != null) {
            username = getIntent().getStringExtra("username");

        } else {
            if (app.getUserInfo() != null) {
                username = app.getUserInfo().getCg_info().getCgusername();

            }
        }
        emailReqCount.setVisibility(View.GONE);
        addressReqCount.setVisibility(View.GONE);
        mobileReqCount.setVisibility(View.GONE);
        //ToastUtil.showErrorUpdate(this, username);
        return username;
    }

    private void init() {
        mwMobileNumReq.setOnClickListener(this);
        mwEmailReq.setOnClickListener(this);
        mwAddressReq.setOnClickListener(this);
        apiCallProfile();
        mwProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyWallActivity.this, ImageViewer.class);
                i.putExtra("downloadurl", "http://www.buyarecaplates.com/");
                i.putExtra("postimage", userPicName);
                startActivity(i);
            }
        });
    }


    /*API CALL - WALL POST*/
    private void apiCallWallPost() {
        if(app.getUserInfo()!=null) {
            Loader.showProgressBar(this);
            Params params = new Params();

            params.addParam("cg_api_req_name", "getposts");
            params.addParam("cg_username", userName);
            WebServiceWrapper.getInstance().callService(this, WebService.WALLPOST, params, this);
        }
    }
    private void apiCallProfile() {
        if(app.getUserInfo()!=null) {

            Params params = new Params();
/*		cg_api_req_name = getpageowner_details
		user_name = (pass current username)*/
                params.addParam("cg_api_req_name", "getpageowner_details");
            params.addParam("user_name", userName);
            WebServiceWrapper.getInstance().callService(this, WebService.USER_PROFILE_URL, params, new ICallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    try{
                        app.setUserPersonal((List<UserPersonal>) new Gson().fromJson(response,new TypeToken<ArrayList<UserPersonal>>(){}.getType()));
                        setValuesToViews();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String response) {
                    dismissLoader();

                }
            });
        }
    }

    private void setValuesToViews() {

        mwNameView.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getFull_name()) ? app.getUserPersonal().get(0).getFull_name() : "");
lastVisitedView.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getLast_visited()) ? app.getUserPersonal().get(0).getLast_visited() : "");
        professionView.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getSubcategory()) ? app.getUserPersonal().get(0).getSubcategory() : "");

        genderCityStatusView.setText(app.getUserPersonal().get(0).getFrom_city() + "," + app.getUserPersonal().get(0).getState());
        if(!ValidationUtil.checkEmptyFields(app.getUserPersonal().get(0).getBirth_day(),app.getUserPersonal().get(0).getBirth_month(),app.getUserPersonal().get(0).getBirth_year())){
            birthdayView.setText(app.getUserPersonal().get(0).getBirth_day() + "-" + app.getUserPersonal().get(0).getBirth_month() + "-" + app.getUserPersonal().get(0).getBirth_year());
        }

        recentProjectsView.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getRecent_projects()) ? app.getUserPersonal().get(0).getRecent_projects() : "");
        aboutMeView.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getWho_are_you()) ? app.getUserPersonal().get(0).getWho_are_you() : "");
        mwAwardsView.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getAwards()) ? app.getUserPersonal().get(0).getAwards() : "");
        mwCompletedProjView.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getCompleted_projects()) ? app.getUserPersonal().get(0).getCompleted_projects() : "");
        mwHobbiesView.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getHobbies()) ? app.getUserPersonal().get(0).getHobbies() : "");
        mwKnownLangView.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getKnown_languages()) ? app.getUserPersonal().get(0).getKnown_languages() : "");
        mwRecentProjView.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getRecent_projects()) ? app.getUserPersonal().get(0).getRecent_projects() : "");
        mwSkillsView.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getSkills()) ? app.getUserPersonal().get(0).getSkills() : "");
    }

    private void dismissLoader() {
        Loader.dismissProgressBar();
    }

    @Override
    public void onSuccess(String response) {

        LocalStorage.feedModel = new Gson().fromJson(response,FeedModel.class);
        setMyWallFeedAdapter();
        //dismissLoader();

    }

    @Override
    public void onFailure(String response) {
        updateErrorUI(response);
        dismissLoader();
    }

    private void updateErrorUI(String errorMsg) {
        ToastUtil.showErrorUpdate(this, errorMsg);
    }

    private void setMyWallFeedAdapter() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        myWallFeedView.setLayoutManager(mLayoutManager);
        myWallFeedView.setFocusable(false);
        HomeFeedAdapter adapter =new HomeFeedAdapter(LocalStorage.feedModel.getCommonwall_posts(), this, false, LocalStorage.feedModel.getUser_datas());
        userType = LocalStorage.feedModel.getUser_datas()[0].getMaincategory();
        myWallFeedView.setAdapter(adapter);
        dismissLoader();
        //setCurrentRequests(userType);
    }

    private void setCurrentRequests(String reqUserType) {
        if((!reqUserType.equals("Fans")) && (userName.equals(app.getUserInfo().getCg_info().getCgusername()))){
            Params params = new Params();
/*		cg_api_req_name = getpageowner_details
		user_name = (pass current username)*/
            params.addParam("cg_api_req_name", "getuserrequests");
            params.addParam("cg_session_user_name", userName);
            WebServiceWrapper.getInstance().callService(this, WebService.REQUEST_URL, params, new ICallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    try{
                        LocalStorage.getCurrecntRequestsModel = new Gson().fromJson(response,GetCurrecntRequestsModel.class);
                        if(LocalStorage.getCurrecntRequestsModel.getEmailrequests() != null){
                            emailReqCount.setVisibility(View.VISIBLE);
                            emailReqCount.setText(String.valueOf(LocalStorage.getCurrecntRequestsModel.getEmailrequests().length));
                        }else{
                            emailReqCount.setVisibility(View.GONE);
                        }
                        if(LocalStorage.getCurrecntRequestsModel.getAddressrequests() != null){
                            addressReqCount.setVisibility(View.VISIBLE);
                            addressReqCount.setText(String.valueOf(LocalStorage.getCurrecntRequestsModel.getAddressrequests().length));
                        }else{
                            addressReqCount.setVisibility(View.GONE);
                        }
                        if(LocalStorage.getCurrecntRequestsModel.getMobilerequests() != null){
                            mobileReqCount.setVisibility(View.VISIBLE);
                            mobileReqCount.setText(String.valueOf(LocalStorage.getCurrecntRequestsModel.getMobilerequests().length));
                        }else{
                            mobileReqCount.setVisibility(View.GONE);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String response) {
                    dismissLoader();

                }
            });
        }
    }

    private void setUserProfilePic(String selectedUserName) {
        //if(app.getUserInfo()!=null) {

            Params params = new Params();
/*		cg_api_req_name = getpageowner_details
		user_name = (pass current username)*/
            params.addParam("cg_api_req_name", "getuserdata");
            params.addParam("cg_username", selectedUserName);
            WebServiceWrapper.getInstance().callService(this, WebService.FEEDS_URL, params, new ICallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    try{
                        JSONObject json = new JSONObject(response);
                        if(!json.getString("userimgurl").isEmpty()) {
                            userPicName = json.getString("userimgurl");
                            Picasso.with(MyWallActivity.this).load("http://www.buyarecaplates.com/" + json.getString("userimgurl")).into(mwProfileImage);
                        }else {
                            updateErrorUI("Unable to fetch profile picture");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String response) {
                    dismissLoader();

                }
            });
//        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.mwAddressReq:
               /* if(userName.equals(app.getUserInfo().getCg_info().getCgusername())){
                    invokeReqRecPopup(view , addressReqCount.getText().toString());
                }else{*/
                    invokeCreateReqPopip(view, "address");
                //}
                break;
            case R.id.mwEmailReq:
                //if(userName.equals(app.getUserInfo().getCg_info().getCgusername())) {
                 //   invokeReqRecPopup(view, emailReqCount.getText().toString());
                //}else{

                    invokeCreateReqPopip(view, "Email");
                //}
                break;
            case R.id.mwMobileNumReq:
                //if(userName.equals(app.getUserInfo().getCg_info().getCgusername())) {
                 //   invokeReqRecPopup(view, mobileReqCount.getText().toString());
                //}else{
                    invokeCreateReqPopip(view, "mobile number");
                //}
                break;
        }

    }

    private void invokeCreateReqPopip(View view,final String type) {
        PopupMenu popup = new PopupMenu(this, view);
        //popup.getMenuInflater().inflate(R.menu.send_req, popup.getMenu());

        popup.getMenu().add("Send "+ type + " request");

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                ToastUtil.showErrorUpdate(MyWallActivity.this, "Menu Clicked");
                sendRequestToUser(type);
                return true;
            }
        });

        popup.show();
    }

    private void sendRequestToUser(String type) {
        String requestAction = "";
        if(type.equals("address")){
            requestAction = "sendeadsreq";
        }else if(type.equals("Email")){
            requestAction = "sendemailreq";
        }else if(type.equals("mobile number")){
            requestAction = "sendemobilereq";
        }

        Params params = new Params();
        params.addParam("cg_api_req_name", "createrequest");
        params.addParam("cg_session_user_name", app.getUserInfo().getCg_info().getCgusername());
        params.addParam("cg_page_user_name", userName);
        params.addParam("cg_request_action", requestAction);
        WebServiceWrapper.getInstance().callService(this, WebService.REQUEST_URL, params, new ICallBack<String>() {
            @Override
            public void onSuccess(String response) {
                try{
                    JSONObject json = new JSONObject(response);
                    if(json.getString("request_status").equals("1")) {

                        ToastUtil.showErrorUpdate(MyWallActivity.this, json.getString("cg_requestmsg"));
                    }else {
                        updateErrorUI("Unable to fetch profile picture");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String response) {
                dismissLoader();

            }
        });
//
    }

    private void invokeReqRecPopup(View view, String count) {

        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.received_req_menu, popup.getMenu());
        if(Integer.parseInt(count) > 0){
            popup.getMenu().add(1, R.id.receivedReq, 1, "Show received requests");
        }else{
            popup.getMenu().add(1, R.id.receivedReq, 1, "No new requests");
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        popup.show();
    }

}
