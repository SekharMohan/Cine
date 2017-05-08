package com.cine.views.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cine.CineApplication;
import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.model.FeedModel;
import com.cine.service.model.category.Mincategories;
import com.cine.service.model.category.ProfessionalCategoryModel;
import com.cine.service.model.subcategory.ProfSubCategory;
import com.cine.service.model.subcategory.Subcategories;
import com.cine.service.model.userinfo.UserPersonal;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.LocalStorage;
import com.cine.utils.LogUtils;
import com.cine.utils.ToastUtil;
import com.cine.utils.ValidationUtil;
import com.cine.utils.permission.Permission;
import com.cine.views.widgets.CircularImageView;
import com.cine.views.widgets.Loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditProfile extends AppCompatActivity {
    @BindView(R.id.center)
    public CircularImageView civProfileImage;
    private Permission permission;
    @BindView(R.id.editProfileEmail)
    AppCompatEditText etEmail;
    @BindView(R.id.editProfileCityFrom)
    AppCompatEditText etCity;
    @BindView(R.id.addressView)
    AppCompatEditText etAddress;
    @BindView(R.id.editProfileMobileNum)
    AppCompatEditText etMobile;
    @BindView(R.id.editProfileLivingCity)
    AppCompatEditText etCurrentCity;
    @BindView(R.id.editProfileState)
    AppCompatEditText etState;
    @BindView(R.id.editProfileFullName)
    AppCompatEditText etName;
    @BindView(R.id.martialStatusSpinner)
    AppCompatSpinner spMaritalStatus;
    @BindView(R.id.categorySpinner)
    AppCompatSpinner spCategorySpinner;
    @BindView(R.id.subCategorySpinner)
    AppCompatSpinner spSubCategory;
    @BindView(R.id.countrySelectionSpinner)
    AppCompatSpinner countrySpinner;
    @BindView(R.id.birthDayPrivacySpinner)
    AppCompatSpinner bdayPrivacySpinner;
    @BindView(R.id.genderRadioGroup)
    RadioGroup rgGender;
@BindView(R.id.editProfileDayView)
AppCompatEditText etDay;
    @BindView(R.id.editProfileMonthView)
    AppCompatEditText etMonth;
    @BindView(R.id.editProfileYearView)
    AppCompatEditText etYear;
    @BindView(R.id.whoAreYouView)
    AppCompatEditText etWhoAreYou;
    @BindView(R.id.recentProjectsView)
    AppCompatEditText etRecentProjects;
    @BindView(R.id.completedProjectsView)
    AppCompatEditText etCompletedProjects;
    @BindView(R.id.awardsView)
    AppCompatEditText etAwards;
    @BindView(R.id.knownLanguagesView)
    AppCompatEditText etLanguagess;
    @BindView(R.id.skillsView)
    AppCompatEditText etSkills;
    @BindView(R.id.hobbiesView)
    AppCompatEditText etHobbies;
    @BindView(R.id.likesView)
    AppCompatEditText etLikes;
    @BindArray(R.array.marital_status)
    String[] maritalStatus;
    @BindArray(R.array.birthday_privacy)
    String[] birthDayPrivacy;
    @BindArray(R.array.country)
    String[] country;
    @BindArray(R.array.category)
    String[] fansarrCategory;

    @BindView(R.id.maleGender)
    RadioButton rbMale;
    @BindView(R.id.femaleGender)
    RadioButton rbFemale;
    @BindView(R.id.otherGender)
    RadioButton rbOthers;
    @BindView(R.id.etSaveButton)
    Button saveButton;
    CineApplication app = CineApplication.getInstance();
    String[] arrProCat;
    String[] arrSubProCat;
    Map<String,String> profCategory = new HashMap<>();
    private Map<String,String> subCategory;
    private List<String> categoryArr;
    private List<String> subCategoryArr;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_profile, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                updateEditProfileApi();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.editProfileYearView,R.id.editProfileMonthView,R.id.editProfileDayView})
    void setDOB(){
      final  Calendar dateSelected = Calendar.getInstance();
         DatePickerDialog datePickerDialog;
        Calendar newCalendar = dateSelected;
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                etDay.setText(String.valueOf(dayOfMonth));
                etMonth.setText(String.valueOf(monthOfYear));
                etYear.setText(String.valueOf(year));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

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


    private void init() {
        spinnerSetup(spMaritalStatus,maritalStatus,"Select Marital status");
        spinnerSetup(bdayPrivacySpinner, birthDayPrivacy, "Select privacy option");
        spinnerSetup(countrySpinner, country, "Select Country");
        apiCallProfessionalCategory();
        apiCallProfile();

        setUserProfilePic();

        permission =  new Permission(this);
        civProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEditProfileApi();
            }
        });
    }

    private void apiCallProfessionalCategory() {
        Params requestQuery = new Params();
        requestQuery.addParam("REQUEST_METHOD","POST");
        requestQuery.addParam("cg_usertype", "cineprofession");
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
                    spinnerSetup(spCategorySpinner,arrProCat,getString(R.string.category_hint));

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

    }

    public void spinnerSetSubCategory(AppCompatSpinner spinner, String[] values, String hint, String userSubCatValue){
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
        for(int i = 0;i < values.length; i++ ){
            if(userSubCatValue.equals(values[i])) {
                spinner.setSelection(i, false);
            }
        }

    }

    private void setUserProfilePic() {
        if(app.getUserInfo()!=null) {

            Params params = new Params();
/*		cg_api_req_name = getpageowner_details
		user_name = (pass current username)*/
            params.addParam("cg_api_req_name", "getuserdata");
            params.addParam("cg_username", app.getUserInfo().getCg_info().getCgusername());
            WebServiceWrapper.getInstance().callService(this, WebService.FEEDS_URL, params, new ICallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    try{
                        JSONObject json = new JSONObject(response);
                        if(!json.getString("userimgurl").isEmpty()) {

                            Picasso.with(EditProfile.this).load("http://www.buyarecaplates.com/" + json.getString("userimgurl")).into(civProfileImage);
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
        }
    }



    private void selectImage() {

        final CharSequence[] items = { "Take Photo", "Gallery",
                "Cancel" };

        TextView title = new TextView(this);
        title.setText("Add Photo!");
        title.setBackgroundColor(Color.BLACK);
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);


        AlertDialog.Builder builder = new AlertDialog.Builder(
                EditProfile.this);



        builder.setCustomTitle(title);

        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    if(permission.hasPermission(Manifest.permission.CAMERA)){
                        takePhoto();
                    }else {
                        permission.requestPermission(Manifest.permission.CAMERA,100);
                    }

                } else if (items[item].equals("Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select Picture"),
                            1);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            takePhoto();
        }else {
            updateErrorUI("Need camera permission");
        }
    }

    void takePhoto(){

        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        Uri selectedImage = null;
        try {
            switch (requestCode) {


                case 0:
                    if (resultCode == RESULT_OK) {
                        if (data.getData() == null) {
                            bitmap = (Bitmap) data.getExtras().get("data");
                        } else {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        }

                        break;
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK) {
                        if (data.getData() == null) {
                            bitmap = (Bitmap) data.getExtras().get("data");
                        } else {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        }
                    }
                    break;

            }
            if (bitmap != null) {
                try {
                /*REQUEST_METHOD = POST
		cg_api_req_name = updateusephoto
		user_name = (pass current username)
		tmp_name = (pass user selected new profile image)
*/
                    showLoader();
                    Params query = new Params();
                    query.addParam("REQUEST_METHOD", "POST");
                    query.addParam("cg_api_req_name", "updateusephoto");
                    query.addParam("user_name", "prabu944");
                    query.addParam("tmp_name", getStringImage(bitmap));
                    final Bitmap finalBitmap = bitmap;
                    WebServiceWrapper.getInstance().callService(this, WebService.USER_PROFILE_URL, query, new ICallBack<String>() {
                        @Override
                        public void onSuccess(String response) {
                            dismissLoader();
                            civProfileImage.setImageBitmap(finalBitmap);
                            try {
                                JSONObject json = new JSONObject(response);
                                if (json.getString("status").equals("1")) {


                                }
                                updateErrorUI(json.getString("cg_msg"));
                                LogUtils.logRequestDetails("Prof pic", json.getString("cg_msg"));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(String response) {
                            dismissLoader();
                            updateErrorUI(response);
                            LogUtils.logRequestDetails("Prof pic",response);
                        }
                    });
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void updateErrorUI(String errorMsg) {
        ToastUtil.showErrorUpdate(this, errorMsg);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    void showLoader(){
        Loader.showProgressBar(this);
    }

    void dismissLoader(){
        Loader.dismissProgressBar();
    }

    void updateEditProfileApi(){
        /*lcity=Salem Tamil&cg_statename=Tamilnadu Tamil&cg_country=India Tamil&cg_marriage=Single&cg_uerrcat=Casting&cg_uerscat=Hero
        &cg_gender=Male&cg_bday=12&cg_bmonth=12&cg_byear=2001&cg_bdysecure=Full&cg_aboutu=Hai&cg_rprjts=Hai
        &cg_cptpjts=Hai&cg_cawards=Hai&cg_eknwlanguages=Hai&cg_eknwskills=Hai&cg_eknwhobbies=Hai&cg_eknwlikes=Hai
 */
        String category = spCategorySpinner.getItemAtPosition(spCategorySpinner.getSelectedItemPosition()).toString();
        String subCategory = spSubCategory.getItemAtPosition(spSubCategory.getSelectedItemPosition()).toString();
        String martialStatus = spMaritalStatus.getItemAtPosition(spMaritalStatus.getSelectedItemPosition()).toString();
        String country = countrySpinner.getItemAtPosition(countrySpinner.getSelectedItemPosition()).toString();
        String bdayPrivacy = bdayPrivacySpinner.getItemAtPosition(bdayPrivacySpinner.getSelectedItemPosition()).toString();
        int redioButtonID = rgGender.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioButton = (RadioButton) findViewById(redioButtonID);

        Params query = new Params();
        query.addParam("cg_api_req_name","updatepageowner_details");
        query.addParam("cg_user_name", app.getUserInfo().getCg_info().getCgusername());
        query.addParam("cg_fullname",(!TextUtils.isEmpty(etName.getText().toString()) ? etName.getText().toString() : ""));
        query.addParam("cg_email",(!TextUtils.isEmpty(etEmail.getText().toString()) ? etEmail.getText().toString() : ""));
        query.addParam("cg_mobile",(!TextUtils.isEmpty(etMobile.getText().toString()) ? etMobile.getText().toString() : ""));
        query.addParam("cg_address",(!TextUtils.isEmpty(etAddress.getText().toString()) ? etAddress.getText().toString() : ""));
        query.addParam("cg_fcity",(!TextUtils.isEmpty(etCity.getText().toString()) ? etCity.getText().toString() : ""));
        query.addParam("lcity",(!TextUtils.isEmpty(etCurrentCity.getText().toString()) ? etCurrentCity.getText().toString() : ""));
        query.addParam("cg_statename",(!TextUtils.isEmpty(etState.getText().toString()) ? etState.getText().toString() : ""));
        query.addParam("cg_country",country);
        query.addParam("cg_marriage",martialStatus);
        query.addParam("cg_uerrcat",category);
        query.addParam("cg_uerscat",subCategory);
        query.addParam("cg_gender", radioButton.getText());
        query.addParam("cg_bday",(!TextUtils.isEmpty(etDay.getText().toString()) ? etDay.getText().toString() : ""));
        query.addParam("cg_bmonth",(!TextUtils.isEmpty(etMonth.getText().toString()) ? etMonth.getText().toString() : ""));
        query.addParam("cg_byear",(!TextUtils.isEmpty(etYear.getText().toString()) ? etYear.getText().toString() : ""));
        query.addParam("cg_bdysecure",bdayPrivacy);
        query.addParam("cg_aboutu",(!TextUtils.isEmpty(etWhoAreYou.getText().toString()) ? etWhoAreYou.getText().toString() : ""));
        query.addParam("cg_rprjts",(!TextUtils.isEmpty(etRecentProjects.getText().toString()) ? etRecentProjects.getText().toString() : ""));
        query.addParam("cg_cptpjts",(!TextUtils.isEmpty(etCompletedProjects.getText().toString()) ? etCompletedProjects.getText().toString() : ""));
        query.addParam("cg_cawards",(!TextUtils.isEmpty(etAwards.getText().toString()) ? etAwards.getText().toString() : ""));
        query.addParam("cg_eknwlanguages",(!TextUtils.isEmpty(etLanguagess.getText().toString()) ? etLanguagess.getText().toString() : ""));
        query.addParam("cg_eknwskills",(!TextUtils.isEmpty(etSkills.getText().toString()) ? etSkills.getText().toString() : ""));
        query.addParam("cg_eknwhobbies",(!TextUtils.isEmpty(etHobbies.getText().toString()) ? etHobbies.getText().toString() : ""));
        query.addParam("cg_eknwlikes",(!TextUtils.isEmpty(etLikes.getText().toString()) ? etLikes.getText().toString() : ""));
        WebServiceWrapper.getInstance().callService(this, WebService.USER_PROFILE_URL, query, new ICallBack<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    if(json.getString("status").equals("1")){
                        updateErrorUI("Profile Updated");
                    }else {
                        updateErrorUI("Oops! Something wrong");
                    }
                    dismissLoader();
                }catch (Exception e){
                    updateErrorUI("Please give valid details");

                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(String response) {
                updateErrorUI(response);
                dismissLoader();
            }
        });
    }

    private void setSubCatSpinner(String subCat, final String userSubCatValue){
        Params request = new Params();
        request.addParam("REQUEST_METHOD","POST");
        request.addParam("cg_usertype","cineprofession");
        request.addParam("cg_user_maincatid",subCat);

        WebServiceWrapper.getInstance().callService(this, WebService.SIGNUP_URL, request, new ICallBack<String>() {
            @Override
            public void onSuccess(String response) {
                ProfSubCategory proSubCategory = new Gson().fromJson(response,ProfSubCategory.class);
                if(proSubCategory!=null){
                    int length = proSubCategory.getSubcategories().length;
                    arrSubProCat = new String[length];
                    for(int i = 0;i < length;i++){
                        Subcategories subCatObject = proSubCategory.getSubcategories()[i];
                        arrSubProCat[i] = subCatObject.getSubcat_name();
                    }
                    //spinnerSetup(spSubCategory,arrSubProCat,getString(R.string.sub_category_hint));
                    spinnerSetSubCategory(spSubCategory,arrSubProCat,getString(R.string.sub_category_hint), userSubCatValue);
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


    private void setValuesToViews() {

        etEmail.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getEmail()) ? app.getUserPersonal().get(0).getEmail() : "");
        etMobile.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getEmail()) ? app.getUserPersonal().get(0).getEmail() : "");
etAddress.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getAddress()) ? app.getUserPersonal().get(0).getAddress() : "");
        etCity.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getFrom_city()) ? app.getUserPersonal().get(0).getFrom_city() : "");
        etCurrentCity.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getTo_city()) ? app.getUserPersonal().get(0).getTo_city() : "");
        etState.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getState()) ? app.getUserPersonal().get(0).getState() : "");
        etName.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getFull_name()) ? app.getUserPersonal().get(0).getFull_name() : "");
        etDay.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getBirth_day()) ? app.getUserPersonal().get(0).getBirth_day() : "");
        etMonth.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getBirth_month()) ? app.getUserPersonal().get(0).getBirth_month() : "");
        etYear.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getBirth_year()) ? app.getUserPersonal().get(0).getBirth_year() : "");
        etWhoAreYou.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getWho_are_you()) ? app.getUserPersonal().get(0).getWho_are_you() : "");
        etRecentProjects.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getRecent_projects()) ? app.getUserPersonal().get(0).getRecent_projects() : "");
        etCompletedProjects.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getCompleted_projects()) ? app.getUserPersonal().get(0).getCompleted_projects() : "");
        etAwards.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getAwards()) ? app.getUserPersonal().get(0).getAwards() : "");
        etLanguagess.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getKnown_languages()) ? app.getUserPersonal().get(0).getKnown_languages() : "");
        etSkills.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getSkills()) ? app.getUserPersonal().get(0).getSkills() : "");
        etHobbies.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getHobbies()) ? app.getUserPersonal().get(0).getHobbies() : "");
        etLikes.setText(!TextUtils.isEmpty(app.getUserPersonal().get(0).getLikes()) ? app.getUserPersonal().get(0).getLikes() : "");
        if(!TextUtils.isEmpty(app.getUserPersonal().get(0).getMarital_status())){
            if(app.getUserPersonal().get(0).getMarital_status().equals(maritalStatus[0])){
                spMaritalStatus.setSelection(0,false);
            }else{
                spMaritalStatus.setSelection(1,false);
            }
        }
        countrySpinner.setSelection(0, false);
        if(!TextUtils.isEmpty(app.getUserPersonal().get(0).getGender())){
            if(app.getUserPersonal().get(0).getGender().equalsIgnoreCase("Male"))
            {
                rbMale.setChecked(true);
            }
            else if(app.getUserPersonal().get(0).getGender().equalsIgnoreCase("Female")){

                rbFemale.setChecked(true);
            }
            else if(app.getUserPersonal().get(0).getGender().equalsIgnoreCase("Others"))
            {
                rbOthers.setChecked(true);
            }
        }
        if(!TextUtils.isEmpty(app.getUserPersonal().get(0).getMaincategory())) {
            setSubCatSpinner(profCategory.get(app.getUserPersonal().get(0).getMaincategory()), app.getUserPersonal().get(0).getSubcategory());
            int selectedPosition = Integer.parseInt(profCategory.get(app.getUserPersonal().get(0).getMaincategory()));
            spCategorySpinner.setSelection(selectedPosition-1,false);
        }
        if(!TextUtils.isEmpty(app.getUserPersonal().get(0).getSubcategory())) {
            LogUtils.logRequestDetails("SubCat", app.getUserPersonal().get(0).getSubcategory());
            for(int i = 0; i < arrSubProCat.length ; i ++){
                if(app.getUserPersonal().get(0).getSubcategory().equals(arrSubProCat[i])){
                    spSubCategory.setSelection(i ,false );
                    LogUtils.logRequestDetails("SubCat", arrSubProCat[i]);
                }
            }
        }

    }/*   "full_name": "Prabu Vaiyapuri",
                "first_name": "Prabu",
                "last_name": "Vaiyapuri",
                "email": "prabu.conqer@gmail.com",
                "mobile": "9578011100",
                "address": "4/82, North Thottaamilm, Pottanam Namakkal.Tamill",
                "from_city": "Namakkal Tamil",
                "to_city": "Salem Tamil",
                "state": "Tamilnadu Tamil",
                "country": "India Tamil",
                "gender": "Male",
                "marital_status": "Single",
                "maincategory": "Casting",
                "subcategory": "Hero",
                "birth_day": "12",
                "birth_month": "12",
                "birth_year": "2001",
                "birthday_privacy": "",
                "who_are_you": "Hai",
                "recent_projects": "Hai",
                "completed_projects": "Hai",
                "awards": "Hai",
                "known_languages": "Hai",
                "skills": "Hai",
                "hobbies": "Hai",
                "likes": "Hai"*/

    private void apiCallProfile() {
        if(app.getUserInfo()!=null) {

            Params params = new Params();
/*		cg_api_req_name = getpageowner_details
		user_name = (pass current username)*/
            params.addParam("cg_api_req_name", "getpageowner_details");
            params.addParam("user_name", app.getUserInfo().getCg_info().getCgusername());
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


}
