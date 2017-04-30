package com.cine.views.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.model.FeedModel;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.LocalStorage;
import com.cine.utils.ToastUtil;
import com.cine.views.widgets.HomeFeedAdapter;
import com.cine.views.widgets.Loader;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Category extends Fragment implements ICallBack<String>{
    @BindView(R.id.categorySelectionSpinner)
    AppCompatSpinner spCategory;
    @BindView(R.id.subCategorySelectionSpinner)
    AppCompatSpinner spSubCagegory;
    @BindView(R.id.feedCategoryView)
    RecyclerView categoryFeed;
    private Map<String ,String> category;
    private Map<String,String> subCategory;
    private List<String> categoryArr;
    private List<String> subCategoryArr;
    private UserInteraction userActive;

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
        ButterKnife.bind(this,view);
        apiCall();
        init();
        return  view;
    }

    private void init() {
        userActive = (UserInteraction) getActivity();
        subCategory = new HashMap<>();
        category = new HashMap<>();
        categoryArr = new ArrayList<String>();
        subCategoryArr = new ArrayList<String>();
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(userActive.isUserActive()&& categoryArr.size()> position){
                    userActive.setUserAction();
                    String selectedCategory = categoryArr.get(position);
                    categoryFeedApi(selectedCategory);
                    String selectedCategoryId = category.get(selectedCategory);
                    subCategoryArr.clear();
                    String subCatName = subCategory.get(selectedCategoryId);
                    if(subCatName != null && !subCatName.isEmpty()) {
                        String subcatNameArr[] = subCatName.split(",");
                        for (String subCat : subcatNameArr) {
                            subCategoryArr.add(subCat.trim());

                        }
                        spinnerSetup(spSubCagegory,subCategoryArr,"Select sub category");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spSubCagegory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });

    }

    private void setFeedAdapter() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        categoryFeed.setLayoutManager(mLayoutManager);
        HomeFeedAdapter adapter =new HomeFeedAdapter(LocalStorage.feedModel.getCommonwall_posts(),getContext());
        categoryFeed.setAdapter(adapter);
    }
    private void categoryFeedApi(String category){

        Loader.showProgressBar(getContext());
        Params params=new Params();

        params.addParam("cg_api_req_name","getposts");
        params.addParam("cg_username","prabu944");
        params.addParam("cg_mcat",category);
        WebServiceWrapper.getInstance().callService(getContext(), WebService.WALLPOST_URL, params, new ICallBack<String>() {
            @Override
            public void onSuccess(String response) {
                LocalStorage.feedModel = new Gson().fromJson(response,FeedModel.class);
                if(LocalStorage.feedModel.getCommonwall_posts().length>0) {
                    setFeedAdapter();
                }else {
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
    private void callWallPostSubCategoryApi(String subcategory){

        Loader.showProgressBar(getContext());
        Params params=new Params();

        params.addParam("cg_api_req_name","getposts");
        params.addParam("cg_username","prabu944");
        params.addParam("cg_scat",subcategory);
        WebServiceWrapper.getInstance().callService(getContext(), WebService.WALLPOSTSUBCAT, params, new ICallBack<String>() {
            @Override
            public void onSuccess(String response) {
                LocalStorage.feedModel = new Gson().fromJson(response,FeedModel.class);

                if(LocalStorage.feedModel.getCommonwall_posts().length>0) {
                    setFeedAdapter();
                }else {
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
    private void showLoader(){
        Loader.showProgressBar(getContext());
    }

    private void apiCall() {
        Params params=new Params();

        params.addParam("cg_api_req_name","getposts");
        params.addParam("cg_username","prabu944");
        WebServiceWrapper.getInstance().callService(getContext(), WebService.FEEDS_URL,params,this);
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
    }

    @Override
    public void onSuccess(String response) {
        LocalStorage.feedModel = new Gson().fromJson(response,FeedModel.class);
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
