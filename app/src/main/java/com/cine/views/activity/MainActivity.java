    package com.cine.views.activity;

    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.AppCompatTextView;
    import android.support.v7.widget.Toolbar;
    import android.text.TextUtils;
    import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
    import android.view.View;
    import android.widget.EditText;
    import android.widget.TextView;

import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.model.FeedModel;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.LocalStorage;
import com.cine.utils.ToastUtil;
    import com.cine.views.fragments.Bonus;
    import com.cine.views.fragments.Category;
import com.cine.views.fragments.Events;
import com.cine.views.fragments.FansClub;
import com.cine.views.fragments.Home;
import com.cine.views.fragments.Language;
import com.cine.views.widgets.Loader;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

    public class MainActivity extends AppCompatActivity implements Category.UserInteraction{
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

        public static Intent getStartIntent(Context context) {
            return new Intent(context, MainActivity.class);
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
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
               /* case R.id.action_ads:
                    startActivity(new Intent(this,AdsActivity.class));
                    break;*/
            }
            return super.onOptionsItemSelected(item);
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
                        errorTextView.setText("");


                        alertDialog.dismiss();
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
            tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_favourite, 0, 0);
            tabLayout.getTabAt(2).setCustomView(tabThree);

            TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabfour.setText(fans);
            tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.fans_club, 0, 0);
            tabLayout.getTabAt(3).setCustomView(tabfour);

            TextView tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabFive.setText(language);
            tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.language, 0, 0);
            tabLayout.getTabAt(4).setCustomView(tabFive);

            TextView tabSix = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabSix.setText(events);
            tabSix.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.events, 0, 0);
            tabLayout.getTabAt(5).setCustomView(tabSix);


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
            adapter.addFrag(new FansClub(), fans);
            adapter.addFrag(new Language(),language);
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
    }