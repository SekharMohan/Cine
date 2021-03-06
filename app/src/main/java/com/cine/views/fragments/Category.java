package com.cine.views.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.ImageView;
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
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.AppConstants;
import com.cine.utils.AppUtils;
import com.cine.utils.ItemClickListener;
import com.cine.utils.LocalStorage;
import com.cine.utils.ToastUtil;
import com.cine.views.widgets.HomeFeedAdapter;
import com.cine.views.widgets.Loader;
import com.cine.views.widgets.SubCategoryAdapter;
import com.cine.views.widgets.UserWallAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Category extends Fragment implements ICallBack<String>,ItemClickListener {
    @BindView(R.id.categorySelectionSpinner)
    AppCompatSpinner spCategory;
    @BindView(R.id.subCategorySelectionSpinner)
    AppCompatSpinner spSubCagegory;
    @BindView(R.id.feedCategoryView)
    RecyclerView categoryFeed;
    @BindView(R.id.subCategoryViewRecycler)
    RecyclerView subCategoryRecyclerView;
    @BindView(R.id.linearSubCategoryFrag)
    RelativeLayout subCategoryRelativeLayout;
    @BindView(R.id.selectSubCatTExtView)
    AppCompatTextView selectSubCatTExtView;
    @BindView(R.id.subCatcard_view)
    CardView subCatCardView;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.radioWall)
    RadioButton wallRadioButton;
    @BindView(R.id.radioUser)
    RadioButton userRadioButton;
    @BindView(R.id.catalertRelLayout)
    RelativeLayout alertsRelativeLayout;
    @BindView(R.id.catalertImage)
    ImageView alertsImage;
    @BindView(R.id.catalertDescription)
    AppCompatTextView alertsDescription;
    @BindView(R.id.catalertsTitle)
    AppCompatTextView alertsTitle;


    private Map<String ,String> category;
    private Map<String,String> subCategory;
    private List<String> categoryArr;
    private List<String> subCategoryArr;
    private UserInteraction userActive;
    private SubCategoryAdapter subCategoryAdapter;
    private String selectedSub;
    private String selectedCategory;
    private int catSpinnerCheck;
private CineApplication app = CineApplication.getInstance();
    public Category() {
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
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        radioGroup = (RadioGroup)view.findViewById(R.id.radio_group);
        wallRadioButton = (RadioButton)view.findViewById(R.id.radioWall);
        ButterKnife.bind(this,view);
//        apiCall();

        init();

        return  view;
    }

    private void init() {

        userActive = (UserInteraction) getActivity();
        subCategory = new HashMap<>();
        category = new HashMap<>();
        categoryArr = new ArrayList<String>();
        subCategoryArr = new ArrayList<String>();
        spSubCagegory.setVisibility(View.GONE);
        subCategoryRelativeLayout.setVisibility(View.GONE);
        subCatCardView.setVisibility(View.GONE);
        radioGroup.setVisibility(View.GONE);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // TODO Auto-generated method stub
                if(wallRadioButton.isChecked()) {

                    callWallPostSubCategoryApi(selectedSub);
                    dismissLoader();
                } else if(userRadioButton.isChecked()) {
                    selectSubCatTExtView.setText(selectedSub);
                    subCategoryRecyclerView.setVisibility(View.GONE);
                    radioGroup.setVisibility(View.VISIBLE);
                    callUserPostApi(selectedSub, selectedCategory);

                }
            }
        });
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //if(userActive.isUserActive()){
                if(++catSpinnerCheck > 1){
                    try {
                        userActive.setUserAction();
                        selectedCategory = categoryArr.get(position);
                        categoryFeedApi(selectedCategory);
                        String selectedCategoryId = category.get(selectedCategory);
                        subCategoryArr.clear();
                        String subCatName = subCategory.get(selectedCategoryId);
                        if (subCatName != null && !subCatName.isEmpty()) {
                            String subcatNameArr[] = subCatName.split(",");
                            for (String subCat : subcatNameArr) {
                                subCategoryArr.add(subCat.trim());

                            }
                            // spinnerSetup(spSubCagegory,subCategoryArr,"Select sub category");
                            setValuesForSubCategory(subCategoryArr);
                        }
                    }catch (IndexOutOfBoundsException ex){

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*spSubCagegory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(userActive.isUserActive()){
                    userActive.setUserAction();
                    callWallPostSubCategoryApi(subCategoryArr.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        subCategoryRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subCategoryRecyclerView.setVisibility(View.VISIBLE);
                radioGroup.setVisibility(View.GONE);
            }
        });

    }

    @SuppressLint("NewApi")
    private void setAlertsValue() {
        if(app.getAlertsList()!=null) {
            //ToastUtil.showErrorUpdate(getContext(), app.getAlertsList().get(0).getAlert_title());
            if(app.getAlertsList().get(0).getAlert_tyoe().equals("information")) {
                alertsRelativeLayout.setBackground(getResources().getDrawable(R.drawable.alertinfo));
            }else if(app.getAlertsList().get(0).getAlert_tyoe().equals("warning")){
                alertsRelativeLayout.setBackground(getResources().getDrawable(R.drawable.alerwarning));
            }else if(app.getAlertsList().get(0).getAlert_tyoe().equals("success")){
                alertsRelativeLayout.setBackground(getResources().getDrawable(R.drawable.alertsuccess));
            }else if(app.getAlertsList().get(0).getAlert_tyoe().equals("danger")){
                alertsRelativeLayout.setBackground(getResources().getDrawable(R.drawable.alertdanger));
            }
            String textColor = AppUtils.getAlertTextColor(app.getAlertsList().get(0).getAlert_tyoe());
            alertsTitle.setText(app.getAlertsList().get(0).getAlert_title());
            alertsTitle.setTextColor(Color.parseColor(textColor));
            alertsDescription.setText(app.getAlertsList().get(0).getAlert_description());
            alertsDescription.setTextColor(Color.parseColor(textColor));
            if (app.getAlertsList().get(0).getAlert_picture() != null) {
                alertsImage.setVisibility(View.VISIBLE);
            } else {
                alertsImage.setVisibility(View.GONE);
            }
        }
    }


    private void callUserPostApi(String selectedSub, String selectedCategory) {
       // Loader.showProgressBar(getContext());
        Params params = new Params();

        params.addParam("cg_api_req_name", "getusers");
        params.addParam("cg_user_name", app.getUserInfo().getCg_info().getCgusername());
        params.addParam("cg_category", selectedCategory);
        params.addParam("cg_subcategory", selectedSub);
        WebServiceWrapper.getInstance().callService(getContext(), WebService.WALLPOSTSUBCAT, params, new ICallBack<String>() {
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
                    dismissLoader();
                    updateErrorUI("No users available");

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
        categoryFeed.setLayoutManager(mLayoutManager);
        UserWallAdapter adapter =new UserWallAdapter(userWallList,getContext());
        categoryFeed.setAdapter(adapter);
    }

    private void setValuesForSubCategory(List<String> subCategoryList){
        subCatCardView.setVisibility(View.VISIBLE);
        subCategoryRelativeLayout.setVisibility(View.VISIBLE);
        subCategoryRecyclerView.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.GONE);
        selectSubCatTExtView.setText("Select Subcategory");
        subCategoryAdapter = new SubCategoryAdapter(getContext(), subCategoryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        subCategoryRecyclerView.setLayoutManager(mLayoutManager);
        subCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        subCategoryRecyclerView.setAdapter(subCategoryAdapter);
        subCategoryAdapter.setClickListener(this);
    }

    @Override
    public void onClick(View view, int position) {
        selectedSub = subCategoryArr.get(position);


     //   Toast.makeText(getContext(), selectedSub, Toast.LENGTH_LONG).show();
        selectSubCatTExtView.setText(selectedSub);
        subCategoryRecyclerView.setVisibility(View.GONE);
        radioGroup.setVisibility(View.VISIBLE);
        wallRadioButton.setChecked(true);
        callWallPostSubCategoryApi(selectedSub);
    }


    private void setFeedAdapter() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        categoryFeed.setLayoutManager(mLayoutManager);
        HomeFeedAdapter adapter =new HomeFeedAdapter(LocalStorage.feedModel.getCommonwall_posts(),getContext(), false, LocalStorage.feedModel.getUser_datas());
        categoryFeed.setAdapter(adapter);
    }
    private void categoryFeedApi(String category){
        if(app.getUserInfo()!=null) {

           // Loader.showProgressBar(getContext());
            Params params = new Params();

            params.addParam("cg_api_req_name", "getposts");
            params.addParam("cg_username", app.getUserInfo().getCg_info().getCgusername());
            params.addParam("cg_mcat", category);
            WebServiceWrapper.getInstance().callService(getContext(), WebService.WALLPOST_URL, params, new ICallBack<String>() {
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
        }
    }
    private void callWallPostSubCategoryApi(String subcategory){
        if(app.getUserInfo()!=null) {

          //  Loader.showProgressBar(getContext());
            Params params = new Params();

            params.addParam("cg_api_req_name", "getposts");
            params.addParam("cg_username", app.getUserInfo().getCg_info().getCgusername());
            params.addParam("cg_scat", subcategory);
            WebServiceWrapper.getInstance().callService(getContext(), WebService.WALLPOSTSUBCAT, params, new ICallBack<String>() {
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
        }
    }
    private void showLoader(){
        Loader.showProgressBar(getContext());
    }

    private void apiCall() {
        if(app.getUserInfo()!=null) {
            Params params = new Params();
            params.addParam("cg_api_req_name", "getposts");
            params.addParam("cg_username", app.getUserInfo().getCg_info().getCgusername());
            WebServiceWrapper.getInstance().callService(getContext(), WebService.FEEDS_URL, params, this);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(LocalStorage.feedModel!=null && LocalStorage.feedModel.getCategories()!=null) {
            setUp();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AppConstants.isFromLanguage) {
            apiCall();
            subCategoryRelativeLayout.setVisibility(View.GONE);
            subCatCardView.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            //super.setUserVisibleHint(true);
        }else{
            AppConstants.isFromLanguage = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // load data here

            setAlertsValue();
        }else{
            // fragment is no longer visible
        }
    }

    private void setUp() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                FeedModel.Categories[] catArr = LocalStorage.feedModel.getCategories();
                int length = LocalStorage.feedModel.getCategories().length;
                categoryArr.clear();
                for(FeedModel.Categories cat:catArr){
                    String catName = cat.getMaincategory_name();
                    category.put(catName,cat.getCategory_id());
                    String subCatName = cat.getSubcategory_names();
                    subCategory.put(cat.getCategory_id(),subCatName);

                    categoryArr .add(catName);

                }
                subCategoryArr.clear();
                return null;
            }



            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                spinnerSetup(spCategory,categoryArr,"Select category");



            }
        }.execute();
    }

    /* private void setUpSubcategory() {
         new AsyncTask<Void, Void, Void>() {
             @Override
             protected void onPreExecute() {
                 super.onPreExecute();

             }

             @Override
             protected Void doInBackground(Void... params) {
                 FeedModel.Categories[] catArr = LocalStorage.feedModel.getCategories();
                 int length = LocalStorage.feedModel.getCategories().length;


                 for(FeedModel.Categories cat:catArr){

                     String subCatName = cat.getSubcategory_names();
                     if(subCatName != null && !subCatName.isEmpty()) {
                         String subcatNameArr[] = subCatName.split(",");
                         for (String subCat : subcatNameArr) {
                             subCategoryArr.add(subCat.trim());
                             subCategory.put(subCat, cat.getCategory_id());
                         }
                     }

                 }

                 return null;
             } @Override
             protected void onPostExecute(Void aVoid) {
                 super.onPostExecute(aVoid);


                 spinnerSetup(spSubCagegory,subCategoryArr,"Select sub category");


             }
         }.execute();
     }*/
    public void spinnerSetup(AppCompatSpinner spinner, List values, String hint){
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
        catSpinnerCheck = 0;
    }

    @Override
    public void onSuccess(String response) {
        LocalStorage.feedModel = new Gson().fromJson(response,FeedModel.class);
        setFeedAdapter();

        setUp();
        dismissLoader();
    }


    @Override
    public void onFailure(String response) {
        updateErrorUI(response);
        dismissLoader();;
    }
    private void dismissLoader() {
        Loader.dismissProgressBar();
    }

    private void updateErrorUI(String errorMsg) {
        ToastUtil.showErrorUpdate(getContext(), errorMsg);
    }
}
