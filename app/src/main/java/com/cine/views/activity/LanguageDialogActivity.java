package com.cine.views.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cine.CineApplication;
import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.model.FeedModel;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.AppConstants;
import com.cine.utils.LocalStorage;
import com.cine.utils.ToastUtil;
import com.cine.views.widgets.Loader;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 06-05-2017.
 */

public class LanguageDialogActivity extends Activity {

    @BindView(R.id.languageDialogSpinner)
    AppCompatSpinner languageSpinner;
    private int spinnerSelectionCheck = 0;
    CineApplication app = CineApplication.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.logo_small);
        setContentView(R.layout.dialogue_language);
        AppConstants.isFromLanguage = true;
        ButterKnife.bind(this);
        setLanguageToSpinner();
        init();
    }

    private void init() {
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(++spinnerSelectionCheck>1) {
                    int selectedLanguageId = position + 1;
                    Params params = new Params();

                    params.addParam("cg_api_req_name", "change_current_state");
                    //params.addParam("cg_user_name", app.getUserInfo().getCg_info().getCgusername());
                    params.addParam("cg_ur_name", app.getUserInfo().getCg_info().getCgusername());
                    params.addParam("cg_state_id", selectedLanguageId);

                    WebServiceWrapper.getInstance().callService(LanguageDialogActivity.this, WebService.FEEDS_URL, params, new ICallBack<String>() {
                        @Override
                        public void onSuccess(String response) {
                            dismissLoader();
                            try {

                                JSONObject json = new JSONObject(response);
                                if (json.getString("cg_state_change_msg").equals("Language Changed Successfully")) {
                                    updateErrorUI("Language Changed Successfully");
                                    finish();
                                } else {
                                    updateErrorUI("Language update failed");
                                }

                            } catch (Exception e) {
                                updateErrorUI("Language update failed");

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
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setLanguageToSpinner() {
        Params params=new Params();

        params.addParam("cg_api_req_name","getposts");
        params.addParam("cg_username", app.getUserInfo().getCg_info().getCgusername());
        WebServiceWrapper.getInstance().callService(this, WebService.FEEDS_URL,params,new ICallBack<String>() {
            @Override
            public void onSuccess(String response) {
                dismissLoader();
                LocalStorage.feedModel = new Gson().fromJson(response, FeedModel.class);
                String[] arrLanguage = getResources().getStringArray(R.array.language);
                String hint = getString(R.string.language_hint);
                int lanugaugeId = Integer.parseInt(LocalStorage.feedModel.getCommonwall_posts()[0].getPost_langid());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(LanguageDialogActivity.this, android.R.layout.simple_spinner_dropdown_item) {

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
                adapter.addAll(arrLanguage);
                adapter.add(hint);
                languageSpinner.setAdapter(adapter);
                languageSpinner.setSelection(lanugaugeId-1, false);
                spinnerSelectionCheck= 0;
            }

            @Override
            public void onFailure(String response) {
                updateErrorUI(response);
                dismissLoader();
            }
        });
    }

    private void dismissLoader() {
        Loader.dismissProgressBar();
    }

    private void updateErrorUI(String errorMsg) {
        ToastUtil.showErrorUpdate(this, errorMsg);
    }
}
