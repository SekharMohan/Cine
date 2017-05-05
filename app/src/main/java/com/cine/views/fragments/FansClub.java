package com.cine.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cine.CineApplication;
import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.model.FeedModel;
import com.cine.service.model.UserWallModel;
import com.cine.service.model.subcategory.fans.FansSubCategory;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.ItemClickListener;
import com.cine.utils.LocalStorage;
import com.cine.utils.ToastUtil;
import com.cine.utils.ValidationUtil;
import com.cine.views.widgets.HomeFeedAdapter;
import com.cine.views.widgets.Loader;
import com.cine.views.widgets.SubCategoryAdapter;
import com.cine.views.widgets.UserWallAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;


public class FansClub extends Fragment implements ICallBack<String>,ItemClickListener {

    @BindArray(R.array.category)
    String[] fansCategory;
    @BindView(R.id.fansSelectionSpinner)
    AppCompatSpinner fansSelectionSpinner;
    @BindView(R.id.fansFeedView)
    RecyclerView fansFeedView;
    @BindView(R.id.sfansSubCategoryViewRecycler)
    RecyclerView fansSubCategoryRecyclerView;
    @BindView(R.id.linearFansSubCategoryFrag)
    RelativeLayout fansSubCategoryRelativeLayout;
    @BindView(R.id.selectFansSubCatTExtView)
    AppCompatTextView selectFansSubCatTExtView;
    @BindView(R.id.fansSubCatcard_view)
    CardView fansSubCatCardView;
    @BindView(R.id.fans_radio_group)
    RadioGroup fansRadioGroup;
    @BindView(R.id.fansRadioWall)
    RadioButton wallRadioButton;
    @BindView(R.id.fansRadioUser)
    RadioButton userRadioButton;

    private UserInteraction userActive;
    private CineApplication app = CineApplication.getInstance();
    private SubCategoryAdapter subCategoryAdapter;
    private List<String> subCategoryList;
    private String selectedFansCategory;
    private String selectedFansSubCategory;

    public FansClub() {
        // Required empty public constructor
    }

    public interface UserInteraction{
        public boolean isUserActive();
        public void setUserAction();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fans, container, false);
        ButterKnife.bind(this,view);
//        apiCall();
        init();
        return  view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            spinnerSetup(fansSelectionSpinner, fansCategory, getString(R.string.category_hint));

        /*if(LocalStorage.feedModel!=null && LocalStorage.feedModel.getCategories()!=null) {
            setUp();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        apiCall();
    }


    private void apiCall() {
//        if(app.getUserInfo()!=null) {
            Params params = new Params();

            params.addParam("cg_api_req_name", "getfullfanposts");
            //params.addParam("cg_username", app.getUserInfo().getCg_info().getCgusername());
            params.addParam("cg_username","keerthi944");
            WebServiceWrapper.getInstance().callService(getContext(), WebService.FANS_URL, params, this);
  //      }
    }


    private void init() {
        userActive = (UserInteraction) getActivity();
        subCategoryList = new ArrayList<String>();

        fansSubCategoryRelativeLayout.setVisibility(View.GONE);
        fansSubCatCardView.setVisibility(View.GONE);
        fansRadioGroup.setVisibility(View.GONE);
        fansRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // TODO Auto-generated method stub
                if(wallRadioButton.isChecked()) {
                    callWallPostFansSubCategoryApi(selectedFansCategory, selectedFansSubCategory);
                    //apiCallFansSubCategory(selectedFansSubCategory);
                    dismissLoader();
                } else if(userRadioButton.isChecked()) {
                    selectFansSubCatTExtView.setText(selectedFansSubCategory);
                    fansSubCategoryRecyclerView.setVisibility(View.GONE);
                    fansRadioGroup.setVisibility(View.VISIBLE);
                    callUserPostApi(selectedFansCategory, selectedFansSubCategory);

                }
            }
        });

        fansSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (userActive.isUserActive()) {
                    try {
                        userActive.setUserAction();
                        selectedFansCategory = fansCategory[position];
                        if(!ValidationUtil.checkEmptyFields(selectedFansCategory)) {
                            fansFeedApi(selectedFansCategory);
                            apiCallFansSubCategory(selectedFansCategory);
                        }
                        dismissLoader();
                    } catch (IndexOutOfBoundsException ex) {
                        dismissLoader();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                    dismissLoader();
            }
        });

        fansSubCategoryRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fansSubCategoryRecyclerView.setVisibility(View.VISIBLE);
                fansRadioGroup.setVisibility(View.GONE);
            }
        });
    }

    private void callUserPostApi(String selectedFansCategory, String selectedFansSubCategory) {
        Loader.showProgressBar(getContext());
        Params params = new Params();

        params.addParam("cg_api_req_name", "getusers");
        //params.addParam("cg_user_name", app.getUserInfo().getCg_info().getCgusername());
        params.addParam("cg_user_name", "prabu944");
        params.addParam("cg_category", "FanClub");
        params.addParam("cg_subcategory", selectedFansCategory + "-" + selectedFansSubCategory);
        WebServiceWrapper.getInstance().callService(getContext(), WebService.FANS_URL, params, new ICallBack<String>() {
            @Override
            public void onSuccess(String response) {
                try {

                    ArrayList<UserWallModel> userWallList = new Gson().fromJson(response, new TypeToken<ArrayList<UserWallModel>>(){}.getType());
                    if (userWallList.size() > 0) {
                        setUserFeedAdapter(userWallList);
                    } else {
                        updateErrorUI("No post available");
                    }
                    dismissLoader();
                } catch (Exception e) {
                    updateErrorUI("No users available");
                    dismissLoader();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String response) {
                dismissLoader();
                updateErrorUI(response);
            }
        });

    }

    private void setUserFeedAdapter(ArrayList<UserWallModel> userWallList) {
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        fansFeedView.setLayoutManager(mLayoutManager);
        UserWallAdapter adapter =new UserWallAdapter(userWallList,getContext());
        fansFeedView.setAdapter(adapter);
    }

    private void fansFeedApi(String selectedFansCategory) {
        Loader.showProgressBar(getContext());
        Params params = new Params();

        params.addParam("cg_api_req_name", "getfullfanposts");
      //  params.addParam("cg_username", app.getUserInfo().getCg_info().getCgusername());
        params.addParam("cg_username", "keerthi944");
        params.addParam("cg_fancat", selectedFansCategory);
        WebServiceWrapper.getInstance().callService(getContext(), WebService.FANS_URL, params, new ICallBack<String>() {
            @Override
            public void onSuccess(String response) {
                LocalStorage.feedModel = new Gson().fromJson(response, FeedModel.class);
                if (LocalStorage.feedModel.getCommonwall_posts().length > 0) {
                    setFeedAdapter();
                } else {
                    updateErrorUI("No post available");
                }
                dismissLoader();
            }

            @Override
            public void onFailure(String response) {
                dismissLoader();
                updateErrorUI("No post available");
            }
        });
    }


    private void apiCallFansSubCategory(String userCategory){
       // Loader.showProgressBar(getContext());

       // if(!ValidationUtil.checkEmptyFields(userCategory)){


            Params request = new Params();

            request.addParam("cg_username","keerthi944");
            request.addParam("cg_api_req_name","getfansubcat");
            request.addParam("cg_user_fanmcat",userCategory);
            WebServiceWrapper.getInstance().callService(getContext(), WebService.FANS_URL, request, new ICallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    FansSubCategory fansModel = new Gson().fromJson(response,FansSubCategory.class);
                    if(fansModel.getSubcategories()!=null){
                        int length = fansModel.getSubcategories().length;
                        subCategoryList.clear();
                       // String[] subCat = new String[length];
                        for(int i = 0;i < length;i++){

                            com.cine.service.model.subcategory.fans.Subcategories subCatObject = fansModel.getSubcategories()[i];
                            subCategoryList.add(subCatObject.getScat_name());
                        }
                        //spinnerSetup(spiSubCategory,subCat,getString(R.string.sub_category_hint));
                        setValuesForSubCategory(subCategoryList);
                    }else{
                        updateErrorUI("Something went wrong");
                    }
                    dismissLoader();
                }

                @Override
                public void onFailure(String response) {
                    updateErrorUI(response);
                    dismissLoader();
                }
            });


        /*}else {
            dismissLoader();
        }*/
    }

    private void setValuesForSubCategory(List<String> subCategoryList){
        dismissLoader();
        fansSubCatCardView.setVisibility(View.VISIBLE);
        fansSubCategoryRelativeLayout.setVisibility(View.VISIBLE);
        fansSubCategoryRecyclerView.setVisibility(View.VISIBLE);
        fansRadioGroup.setVisibility(View.GONE);
        selectFansSubCatTExtView.setText("Select Sub category");
        subCategoryAdapter = new SubCategoryAdapter(getContext(), subCategoryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        fansSubCategoryRecyclerView.setLayoutManager(mLayoutManager);
        fansSubCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        fansSubCategoryRecyclerView.setAdapter(subCategoryAdapter);
        subCategoryAdapter.setClickListener(this);
    }

    private void dismissLoader() {
        Loader.dismissProgressBar();
    }

    private void updateErrorUI(String errorMsg) {
        ToastUtil.showErrorUpdate(getContext(), errorMsg);
    }

    public void spinnerSetup(AppCompatSpinner spinner, String[] values, String hint){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }

        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(values);
        adapter.add(hint);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());
    }

    @Override
    public void onClick(View view, int position) {
        selectedFansSubCategory = subCategoryList.get(position);


        //   Toast.makeText(getContext(), selectedSub, Toast.LENGTH_LONG).show();
        selectFansSubCatTExtView.setText(selectedFansSubCategory);
        fansSubCategoryRecyclerView.setVisibility(View.GONE);
        fansRadioGroup.setVisibility(View.VISIBLE);
        wallRadioButton.setChecked(true);
        callWallPostFansSubCategoryApi(selectedFansCategory, selectedFansSubCategory);
    }

    private void callWallPostFansSubCategoryApi(String selectedFansCategory, String selectedFansSubCategory) {

       //     if(app.getUserInfo()!=null) {
        if(!ValidationUtil.checkEmptyFields(selectedFansCategory,selectedFansCategory)) {
            Loader.showProgressBar(getContext());
            Params params = new Params();
            params.addParam("cg_api_req_name", "getfullfanposts");
            // params.addParam("cg_username", app.getUserInfo().getCg_info().getCgusername());
            params.addParam("cg_username", "keerthi944");
            params.addParam("cg_scat", selectedFansCategory + "-" + selectedFansSubCategory);
            WebServiceWrapper.getInstance().callService(getContext(), WebService.FANS_URL, params, new ICallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    LocalStorage.feedModel = new Gson().fromJson(response, FeedModel.class);

                    if (LocalStorage.feedModel.getCommonwall_posts().length > 0) {
                        setFeedAdapter();
                    } else {
                        updateErrorUI("No post available");
                    }
                    dismissLoader();
                }

                @Override
                public void onFailure(String response) {
                    dismissLoader();
                    updateErrorUI(response);
                }
            });
        }else{
            dismissLoader();
        }

    //    }


    }

    @Override
    public void onSuccess(String response) {
        LocalStorage.feedModel = new Gson().fromJson(response,FeedModel.class);
        setFeedAdapter();
        //setUp();
        dismissLoader();

    }

    private void setFeedAdapter() {
        dismissLoader();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        fansFeedView.setLayoutManager(mLayoutManager);
        HomeFeedAdapter adapter =new HomeFeedAdapter(LocalStorage.feedModel.getCommonwall_posts(),getContext(), false);
        fansFeedView.setAdapter(adapter);
    }

    @Override
    public void onFailure(String response) {
        updateErrorUI(response);
        dismissLoader();;
    }
}
