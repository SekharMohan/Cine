package com.cine.views.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.cine.CineApplication;
import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.model.FeedModel;
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


public class MyWallActivity extends AppCompatActivity implements ICallBack<String> {

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
    @BindView(R.id.center)
    public CircularImageView civProfileImage;
    @BindView(R.id.contactReqLayout)
    LinearLayout contactReqLayout;
    @BindView(R.id.mwEmailReq)
    ImageButton mwEmailReq;
    @BindView(R.id.mwMobileNumReq)
    ImageButton mwMobileNumReq;
    @BindView(R.id.mwAddressReq)
    ImageButton mwAddressReq;
    CineApplication app = CineApplication.getInstance();
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wall);
        ButterKnife.bind(this);
        userName = getUserName();
        init();
        apiCallWallPost();
        setUserProfilePic(userName);
        AppConstants.isFromLanguage = false;
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
        //ToastUtil.showErrorUpdate(this, username);
        return username;
    }

    private void init() {
        apiCallProfile();
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
        dismissLoader();

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
        myWallFeedView.setAdapter(adapter);
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

                            Picasso.with(MyWallActivity.this).load("http://www.buyarecaplates.com/" + json.getString("userimgurl")).into(civProfileImage);
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

}
