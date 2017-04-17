package com.cine.views.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.model.category.Mincategories;
import com.cine.service.model.category.ProfessionalCategoryModel;
import com.cine.service.model.subcategory.ProfSubCategory;
import com.cine.service.model.subcategory.Subcategories;
import com.cine.service.model.subcategory.fans.FansSubCategory;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.ToastUtil;
import com.cine.utils.ValidationUtil;
import com.cine.views.widgets.Loader;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SingUpFinal extends AppCompatActivity {
    @BindView(R.id.sub_category_spinner)
    AppCompatSpinner spiSubCategory;
    @BindView(R.id.language_spinner)
    AppCompatSpinner spiLanguage;
    @BindView(R.id.category_spinner)
    AppCompatSpinner spiCategory;
    @BindArray(R.array.category)
    String[] arrCategory;
    @BindArray(R.array.language)
    String[] arrLanguage;
    @BindView(R.id.createMyAccountButton)
    public AppCompatButton createMyAccountButton;
    @BindView(R.id.regAadhar)
    AppCompatEditText edtAadhar;
    @BindView(R.id.regEmailId)
    AppCompatEditText edtEmailId;
    @BindView(R.id.regName)
    AppCompatEditText edtName;
    @BindView(R.id.regPassword)
    AppCompatEditText edtPassword;
    @BindView(R.id.regUserName)
    AppCompatEditText edtUserName;
    @BindView(R.id.reglName)
    AppCompatEditText edtLName;
    String userType;
    String[] arrProCat;
    Map<String,String> profCategory = new HashMap<>();
    Map<String,Integer> languageAndId = new HashMap<>();
    public static Intent getStartIntent(Context context) {
        return new Intent(context, SingUpFinal.class);
    }
    boolean listenrEnabler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_two);
        ButterKnife.bind(this);
        init();
    }
    private void showLoader(){
        Loader.showProgressBar(this);
    }
    private void init() {

        int length = arrLanguage.length;
        for(int i=0 ; i<length;i++){
            languageAndId.put(arrLanguage[i],(i+1));
        }

        spinnerSetup(spiLanguage,arrLanguage,getString(R.string.language_hint));


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null&&bundle.containsKey("user_type")){
            userType = bundle.getString("user_type");
            if(userType.equals(getString(R.string.user_type_professional))){
                showLoader();
                apiCallProfessionalCategory();
            }else{
                spinnerSetup(spiCategory,arrCategory,getString(R.string.category_hint));
            }
        }
      /*  spiLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spiCategory!=null){
                    spiCategory.setAdapter(null);
                }
                if(spiSubCategory!=null){
                    spiSubCategory.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
        createMyAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEvent();
            }

        });
        spiCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(listenrEnabler) {
                    if (arrProCat != null && userType.equals(getString(R.string.user_type_professional))) {
                        apiCallProfSubCategory(profCategory.get(arrProCat[position]));
                    } else {
                        if (userType.equals(getString(R.string.user_type_fans))) {
                            apiCallFansSubCategory(userType, arrCategory[position]);
                        }
                    }
                }

                if(spiSubCategory!=null){
                    spiSubCategory.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        listenrEnabler = true;
    }

    private void apiCallFansSubCategory( String user,String userCategory){
        showLoader();
    String language =spiLanguage.getItemAtPosition(spiLanguage.getSelectedItemPosition()).toString();
        int lanugaugeId=0;
        if(languageAndId!=null) {
             lanugaugeId = languageAndId.get(language);
        }else{
            updateErrorUI("Select language");
        }
    if(!ValidationUtil.checkEmptyFields(user,language,userCategory,String.valueOf(lanugaugeId))){


        Params request = new Params();
        request.addParam("REQUEST_METHOD","POST");
        request.addParam("cg_usertype",user);
        request.addParam("cg_user_language",language);
        request.addParam("cg_user_language_id",lanugaugeId);
        request.addParam("cg_user_fanmcat",userCategory);
        WebServiceWrapper.getInstance().callService(this, WebService.SIGNUP_URL, request, new ICallBack<String>() {
            @Override
            public void onSuccess(String response) {
                FansSubCategory fansModel = new Gson().fromJson(response,FansSubCategory.class);
                if(fansModel!=null){
                    int length = fansModel.getSubcategories().length;
                    String[] subCat = new String[length];
                    for(int i = 0;i < length;i++){

                      com.cine.service.model.subcategory.fans.Subcategories subCatObject = fansModel.getSubcategories()[i];
                              subCat[i] = subCatObject.getScat_name();
                    }
                    spinnerSetup(spiSubCategory,subCat,getString(R.string.sub_category_hint));
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


    }else {

    }
}
    private void apiCallProfSubCategory(String subCat) {
        showLoader();
        if(!ValidationUtil.checkEmptyFields(userType,subCat)){
            Params request = new Params();
            request.addParam("REQUEST_METHOD","POST");
            request.addParam("cg_usertype",userType);
            request.addParam("cg_user_maincatid",subCat);

            WebServiceWrapper.getInstance().callService(this, WebService.SIGNUP_URL, request, new ICallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    ProfSubCategory proSubCategory = new Gson().fromJson(response,ProfSubCategory.class);
                    if(proSubCategory!=null){
                        int length = proSubCategory.getSubcategories().length;
                        String[] subCat = new String[length];
                        for(int i = 0;i < length;i++){
                            Subcategories subCatObject = proSubCategory.getSubcategories()[i];
                            subCat[i] = subCatObject.getSubcat_name();
                        }
                        spinnerSetup(spiSubCategory,subCat,getString(R.string.sub_category_hint));

                    }else {
                        updateErrorUI("Oops! Something wrong");
                    }
                    dismissLoader();
                }

                @Override
                public void onFailure(String response) {
                    updateErrorUI(response);
                    dismissLoader();
                }
            });
        }


    }

    private void apiCallProfessionalCategory() {
        if (!ValidationUtil.checkEmptyFields(userType)) {
            Params requestQuery = new Params();
            requestQuery.addParam("REQUEST_METHOD","POST");
            requestQuery.addParam("cg_usertype", userType);
            WebServiceWrapper.getInstance().callService(this, WebService.SIGNUP_URL, requestQuery, new ICallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    ProfessionalCategoryModel model = new Gson().fromJson(response,ProfessionalCategoryModel.class);
                    if(model!=null){
                        int length = model.getMincategories().length;
                        arrProCat = new String[length];
                        for(int i=0;i< length;i++)
                        {
                            Mincategories minCatObject = model.getMincategories()[i];
                            String catItem = minCatObject.getMaincat_name();
                            arrProCat[i] = catItem;
                            profCategory.put(catItem,minCatObject.getMaincat_id());

                        }
                        spinnerSetup(spiCategory,arrProCat,getString(R.string.category_hint));

                    }else{
                        updateErrorUI("Oops! Something worng");
                    }
                    dismissLoader();
                }

                @Override
                public void onFailure(String response) {
                    updateErrorUI(response);
                    dismissLoader();

                }
            });
        } else {
            updateErrorUI(getString(R.string.login_field_empty_warning));
        }

    }

    public void spinnerSetup(AppCompatSpinner spinner,String[] values,String hint){
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {

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

private void apiCallRegister(){

    String language =spiLanguage.getItemAtPosition(spiLanguage.getSelectedItemPosition()).toString();
    int lanugaugeId = languageAndId.get(language);
    String category =spiCategory.getItemAtPosition(spiCategory.getSelectedItemPosition()).toString();
    String subCategory = spiSubCategory.getItemAtPosition(spiSubCategory.getSelectedItemPosition()).toString();
    String fname = edtName.getText().toString();
    String uName = edtUserName.getText().toString();
    String aadhar = edtAadhar.getText().toString();
    String psw = edtPassword.getText().toString();
    String email = edtEmailId.getText().toString();
    String lName = edtLName.getText().toString();

    if(!ValidationUtil.checkEmptyFields(lName,String.valueOf(lanugaugeId),language,category,subCategory,userType,fname,uName,aadhar,psw,email)){
        showLoader();
        Params request = new Params();
        request.addParam("REQUEST_METHOD","POST");
        request.addParam("cg_usertype",userType);
        request.addParam("cg_user_language",language);
        if(userType.equals(getString(R.string.user_type_fans))) {
            request.addParam("cg_user_language_id", lanugaugeId);
            request.addParam("cg_user_fmaincat", category);
            request.addParam("cg_user_fsaincat", subCategory);
        }else{
            request.addParam("cg_user_maincat", category);
            request.addParam("cg_user_saincat", subCategory);
        }
        request.addParam("cg_user_fname",fname);
        request.addParam("cg_user_lname",lName);
        request.addParam("cg_user_name",uName);
        request.addParam("cg_user_email",email);
        request.addParam("cg_user_password",psw);

        WebServiceWrapper.getInstance().callService(this, WebService.SIGNUP_URL, request, new ICallBack<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                if((int)jsonObject.get("status")==1) {

                    startActivity(new Intent(SingUpFinal.this, LoginActivity.class));
                    finish();
                }else{
                    updateErrorUI(jsonObject.getString("cg_msg"));
                }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissLoader();
            }

            @Override
            public void onFailure(String response) {
                updateErrorUI(response);
                dismissLoader();
            }
        });




    }else{
        updateErrorUI("Select/Enter all the fields");
    }




}

    private void updateErrorUI(final String errorMsg) {

                ToastUtil.showErrorUpdate(SingUpFinal.this, errorMsg);


    }
    void onClickEvent(){
        apiCallRegister();
    }



    private void dismissLoader() {
        Loader.dismissProgressBar();
    }


}
