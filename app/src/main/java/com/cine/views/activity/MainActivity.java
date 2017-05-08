    package com.cine.views.activity;

    import android.Manifest;
    import android.app.Dialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.graphics.Bitmap;
    import android.graphics.Color;
    import android.net.Uri;
    import android.os.Bundle;
    import android.provider.MediaStore;
    import android.support.annotation.NonNull;
    import android.support.design.widget.TabLayout;
    import android.support.v4.app.Fragment;
    import android.support.v4.app.FragmentManager;
    import android.support.v4.app.FragmentPagerAdapter;
    import android.support.v4.view.MenuItemCompat;
    import android.support.v4.view.ViewPager;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.AppCompatSpinner;
    import android.support.v7.widget.AppCompatTextView;

    import android.support.v7.widget.RecyclerView;
    import android.support.v7.widget.SearchView;
    import android.support.v7.widget.Toolbar;
    import android.text.Editable;
    import android.text.TextUtils;
    import android.text.TextWatcher;
    import android.util.Base64;
    import android.view.Gravity;
    import android.view.LayoutInflater;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.ViewGroup;
    import android.view.Window;
    import android.view.WindowManager;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;

    import android.widget.TextView;



    import com.cine.CineApplication;
    import com.cine.R;
    import com.cine.service.WebService;
    import com.cine.service.WebServiceWrapper;
    import com.cine.service.model.Alerts;
    import com.cine.service.model.FeedModel;
    import com.cine.service.network.Params;
    import com.cine.service.network.callback.ICallBack;
    import com.cine.utils.AppConstants;
    import com.cine.utils.LocalStorage;
    import com.cine.utils.ToastUtil;
    import com.cine.utils.permission.Permission;
    import com.cine.views.fragments.Bonus;
    import com.cine.views.fragments.Category;
    import com.cine.views.fragments.Events;
    import com.cine.views.fragments.FansClub;
    import com.cine.views.fragments.Home;
    import com.cine.views.fragments.Language;
    import com.cine.views.widgets.CircularImageView;
    import com.cine.views.widgets.Loader;
    import com.google.gson.Gson;
    import com.google.gson.reflect.TypeToken;

    import org.json.JSONObject;

    import java.io.ByteArrayOutputStream;
    import java.util.ArrayList;
    import java.util.List;

    import butterknife.BindString;
    import butterknife.BindView;
    import butterknife.ButterKnife;

    public class MainActivity extends AppCompatActivity implements Category.UserInteraction,FansClub.UserInteraction {
    @BindString(R.string.about)
  public   String about;
        @BindString(R.string.home)
        public   String home;
        @BindString(R.string.language)
        public    String language;
        @BindString(R.string.bonus)
        public    String bonus;
        @BindString(R.string.fans)
        public     String fans;
        @BindString(R.string.events)
        public    String events;
        @BindString(R.string.category)
        public String category;
        @BindView(R.id.toolbar)
        public   Toolbar toolbar;
        @BindView(R.id.tabs)
        public    TabLayout tabLayout;
        @BindView(R.id.viewpager)
        public   ViewPager viewPager;
        boolean listenrEnabler;
        CineApplication app = CineApplication.getInstance();
        private Permission permission;
        ICallBack<String> statusListener;
        private int spinnerSelectionCheck = 0;

        public static Intent getStartIntent(Context context) {
            return new Intent(context, MainActivity.class);
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            permission =  new Permission(this);
            /*mfeed*/

            /*wall post -mcwall*/
//          callWallPostApi();
            /*wall post sub - mscwall*/
//           callWallPostSubCategoryApi();
            /*wall post*/
//           apiCallWallPost();
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons();
            setSupportActionBar(toolbar);
           // getSupportActionBar().setDisplayShowHomeEnabled(true);
           // getSupportActionBar().setIcon(R.drawable.logo_small);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_action_bar);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            //createSupportedToolbar(false);
            setTitle("");
//            alertsApiCall();
        }

        public void createSupportedToolbar(boolean value) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.custom_action_bar_view);
            if (toolbar != null) {
                if (value) {
                    toolbar.setNavigationIcon(R.drawable.logo_small);
                }
                setSupportActionBar(toolbar);
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_search:
                    ToastUtil.showErrorUpdate(this, "Clicked Search");
                    startActivity(new Intent(this,SearchUsersActivity.class));
                    break;
                case R.id.action_edit_profile:
                    startActivity(new Intent(this,EditProfile.class));
                    break;
                case R.id.action_logout:

                    break;
                case R.id.action_my_profile:
                    startActivity(new Intent(this,MyWallActivity.class));
                    break;
                case R.id.action_reset_password:
                    invokeDialog(R.layout.reset_password, "Update Password");
                    break;
               case R.id.action_language:
                    //startActivity(new Intent(this,LanguageActivity.class));
                 //  invokeLanguageDialog(R.layout.dialogue_language, "Choose language");
                   startActivity(new Intent(this,LanguageDialogActivity.class));
                    break;
            }
            return super.onOptionsItemSelected(item);
        }

        private void invokeLanguageDialog(int resId, String title) {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(resId, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setTitle(title);
            dialogBuilder.setIcon(R.mipmap.ic_launcher);
            final AppCompatSpinner languageSpinner = (AppCompatSpinner) dialogView.findViewById(R.id.languageDialogSpinner);

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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item) {

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

                }

                @Override
                public void onFailure(String response) {
                    updateErrorUI(response);
                    dismissLoader();
                }
            });
            spinnerSelectionCheck= 0;
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
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

                        WebServiceWrapper.getInstance().callService(MainActivity.this, WebService.FEEDS_URL, params, new ICallBack<String>() {
                            @Override
                            public void onSuccess(String response) {
                                dismissLoader();
                                try {

                                    JSONObject json = new JSONObject(response);
                                    if (json.getString("cg_state_change_msg").equals("Language Changed Successfully")) {
                                        updateErrorUI("Language Changed Successfully");
                                        alertDialog.dismiss();
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


        private void invokeDialog(int resId, String title){ // change password dialogue
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(resId, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setTitle(title);
            dialogBuilder.setIcon(R.mipmap.ic_launcher);
            final EditText currentPassword = (EditText) dialogView.findViewById(R.id.currentPasswordView);
            final EditText newPassword = (EditText) dialogView.findViewById(R.id.newPasswordView);
            final EditText verifyNewPassword = (EditText) dialogView.findViewById(R.id.verifyNewPasswordView);
            final AppCompatTextView errorTextView = (AppCompatTextView) dialogView.findViewById(R.id.errorText);

            dialogBuilder.setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });
            dialogBuilder.setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    currentPassword.getText().toString();
                    newPassword.getText().toString();
                    verifyNewPassword.getText().toString();
                    if(!TextUtils.isEmpty(currentPassword.getText().toString()) && !TextUtils.isEmpty(newPassword.getText().toString())){
if(newPassword.getText().toString().equals(verifyNewPassword.getText().toString())) {
    if(app.getUserInfo()!=null) {
        Loader.showProgressBar(MainActivity.this);
        Params query = new Params();
        query.addParam("REQUEST_METHOD", "POST");
        query.addParam("cg_api_req_name", "updateusepawd");
        query.addParam("user_name", app.getUserInfo().getCg_info().getCgusername());
        query.addParam("user_oldpass", currentPassword.getText().toString());
        query.addParam("user_newpass", newPassword.getText().toString());
        query.addParam("user_newconfirmpass", verifyNewPassword.getText().toString());
        WebServiceWrapper.getInstance().callService(MainActivity.this, WebService.USER_PROFILE_URL, query, new ICallBack<String>() {
            @Override
            public void onSuccess(String response) {
                dismissLoader();
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getString("status").equals("1")) {
                        alertDialog.dismiss();
                    }
                    updateErrorUI(json.getString("cg_msg"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void onFailure(String response) {
                updateErrorUI(response);
                dismissLoader();
            }
        });
    }
    errorTextView.setText("");
}else {
    errorTextView.setText("New password and confirm password should be same");
}


                    }else{
                        errorTextView.setText("Fields should not be empty");

                    }
                }
            });
        }



        @Override
        public void onUserInteraction() {
            super.onUserInteraction();
            listenrEnabler = true;
        }

        /*API CALL FOR WALL POST-MAIN CATEGORY*/
private void callWallPostApi(){

    Loader.showProgressBar(this);
    Params params=new Params();

    params.addParam("cg_api_req_name","getposts");
    params.addParam("cg_username","prabu944");
    params.addParam("cg_mcat","Casting");
    WebServiceWrapper.getInstance().callService(this, WebService.WALLPOST_URL, params, new ICallBack<String>() {
        @Override
        public void onSuccess(String response) {
            LocalStorage.feedModel = new Gson().fromJson(response,FeedModel.class);
            dismissLoader();
        }

        @Override
        public void onFailure(String response) {

        }
    });
}
        /*API CALL FOR WALL POST-SUB CATEGORY*/
        private void callWallPostSubCategoryApi(){

            Loader.showProgressBar(this);
            Params params=new Params();

            params.addParam("cg_api_req_name","getposts");
            params.addParam("cg_username","prabu944");
            params.addParam("cg_scat","Hero");
            WebServiceWrapper.getInstance().callService(this, WebService.WALLPOSTSUBCAT, params, new ICallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    LocalStorage.feedModel = new Gson().fromJson(response,FeedModel.class);
                    dismissLoader();
                }

                @Override
                public void onFailure(String response) {

                }
            });
        }

        /*API CALL - WALL POST*/
        private void apiCallWallPost() {
            Loader.showProgressBar(this);
            Params params=new Params();

            params.addParam("cg_api_req_name","getposts");
            params.addParam("cg_username","prabu944");
            WebServiceWrapper.getInstance().callService(this, WebService.WALLPOST, params, new ICallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    LocalStorage.feedModel = new Gson().fromJson(response,FeedModel.class);
                    dismissLoader();
                }

                @Override
                public void onFailure(String response) {

                }
            });
        }

        /**
         * Adding custom view to tab
         */
        private void setupTabIcons() {

            TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabOne.setText(home);
            tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home, 0, 0);
            tabLayout.getTabAt(0).setCustomView(tabOne);

            TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabTwo.setText(category);
            tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.category, 0, 0);
            tabLayout.getTabAt(1).setCustomView(tabTwo);

            TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabThree.setText(bonus);
            tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bonus, 0, 0);
            tabLayout.getTabAt(2).setCustomView(tabThree);

            TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabfour.setText(fans);
            tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.fans_club, 0, 0);
            tabLayout.getTabAt(3).setCustomView(tabfour);

            /*TextView tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabFive.setText(language);
            tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.language, 0, 0);
            tabLayout.getTabAt(4).setCustomView(tabFive);*/

            TextView tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabFive.setText(events);
            tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.events, 0, 0);
            tabLayout.getTabAt(4).setCustomView(tabFive);


        }

        /**
         * Adding fragments to ViewPager
         * @param viewPager
         */
        private void setupViewPager(ViewPager viewPager) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFrag(new Home(), home);
            adapter.addFrag(new Category(), category);
            adapter.addFrag(new Bonus(), bonus);

            adapter.addFrag(new FansClub(),fans);
            adapter.addFrag(new Events(),events);

            viewPager.setAdapter(adapter);

        }



        private void dismissLoader() {
            Loader.dismissProgressBar();
        }

        private void updateErrorUI(String errorMsg) {
            ToastUtil.showErrorUpdate(this, errorMsg);
        }

        @Override
        public boolean isUserActive() {
            return listenrEnabler;
        }



        class ViewPagerAdapter extends FragmentPagerAdapter {
            private final List<Fragment> mFragmentList = new ArrayList<>();
            private final List<String> mFragmentTitleList = new ArrayList<>();

            public ViewPagerAdapter(FragmentManager manager) {
                super(manager);
            }

            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            public void addFrag(Fragment fragment, String title) {
                mFragmentList.add(fragment);
                mFragmentTitleList.add(title);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentTitleList.get(position);
            }


        }
        @Override
        public void setUserAction(){
            listenrEnabler = false;

        }
        public void selectImage(ICallBack<String> listener ) {
            statusListener = listener;

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
                    this);



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
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
         if(bitmap !=null){
             showImagePopUp(bitmap);
             //statusListener.onSuccess(getStringImage(bitmap));
         }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private void showImagePopUp(final Bitmap selectedImage) {
            final Dialog d = new Dialog(this);
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.image_update_popup);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(d.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            d.show();
            d.getWindow().setAttributes(lp);
            final ImageView imageView = (ImageView) d.findViewById(R.id.supImageView);
            final EditText editTextCommets = (EditText) d.findViewById(R.id.imageUploadComments);
            final Button btnPostImage = (Button) d.findViewById(R.id.postImage);
            imageView.setImageBitmap(selectedImage);
            btnPostImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstants.statusUpdateComments = editTextCommets.getText().toString();
                    statusListener.onSuccess(getStringImage(selectedImage));
                }
            });
        }

        public String getStringImage(Bitmap bmp){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }
        public  void showVideoPopup(final ICallBack<String> listener){
            statusListener = listener;
            final Dialog d = new Dialog(this);
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.video_url_popup);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(d.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            d.show();
            d.getWindow().setAttributes(lp);
            final Button continueNext = (Button) d.findViewById(R.id.continuevide);
            continueNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                }
            });
            Button fetch = (Button) d.findViewById(R.id.fetch);
            final EditText editUrl = (EditText) d.findViewById(R.id.editTextVide);
            final EditText editComments = (EditText) d.findViewById(R.id.editTextVideoComments);
            fetch.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
if(!editUrl.getText().toString().isEmpty()){
    if(verifyVideoUrl(editUrl.getText().toString())){
        continueNext.setClickable(true);
        continueNext.setAlpha(1.0f);
        AppConstants.statusUpdateComments = editComments.getText().toString();
        statusListener.onSuccess(editUrl.getText().toString());

    }else{
        updateErrorUI("Video Url is invalid");
    }
}else {
    updateErrorUI("Video Url is empty");
}
                }
            });
        }
private boolean verifyVideoUrl(String url){
    String verifyUrl = url.toLowerCase();
if(verifyUrl.contains("youtube")||verifyUrl.contains("vimeo")||verifyUrl.contains("metacafe")||verifyUrl.contains("dailymotion")||verifyUrl.contains("flickr")){
    return true;
}
return  false;
}
    }