package com.cine.views.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cine.R;
import com.cine.service.model.FeedModel;
import com.cine.utils.LocalStorage;
import com.cine.views.widgets.Loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Category extends Fragment {
    @BindView(R.id.categorySelectionSpinner)
    AppCompatSpinner spCategory;
    @BindView(R.id.subCategorySelectionSpinner)
    AppCompatSpinner spSubCagegory;
    private Map<String ,String> category;
    private Map<String,String> subCategory;
    private List<String> categoryArr;
    private List<String> subCategoryArr;

    public Category() {
        // Required empty public constructor
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
        init();
        return  view;
    }

    private void init() {

        category = new HashMap<>();
        subCategory = new HashMap<>();
        if(LocalStorage.feedModel!=null && LocalStorage.feedModel.getCategories()!=null) {
            setUp();
        }
    }

    private void setUp() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Loader.showProgressBar(getContext());
            }

            @Override
            protected Void doInBackground(Void... params) {
                FeedModel.Categories[] catArr = LocalStorage.feedModel.getCategories();
                int length = LocalStorage.feedModel.getCategories().length;
                categoryArr = new ArrayList<String>();
                subCategoryArr = new ArrayList<String>();
                for(FeedModel.Categories cat:catArr){
String catName = cat.getMaincategory_name();
                    String subCatName = cat.getSubcategory_names();
                    category.put(catName,cat.getCategory_id());
                    subCategory.put(subCatName,cat.getCategory_id());
                    categoryArr .add(catName);
                    subCategoryArr.add(subCatName);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                spinnerSetup(spCategory,categoryArr,"Select category");
                spinnerSetup(spSubCagegory,subCategoryArr,"Select sub category");
                Loader.dismissProgressBar();

            }
        }.execute();
    }
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
}
