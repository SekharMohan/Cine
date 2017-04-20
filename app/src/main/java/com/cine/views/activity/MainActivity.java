    package com.cine.views.activity;

    import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.model.FeedModel;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.LocalStorage;
import com.cine.utils.ToastUtil;
import com.cine.views.fragments.AboutUs;
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

    public class MainActivity extends AppCompatActivity {
    @BindString(R.string.about)
  public   String about;
        @BindString(R.string.home)
        public   String home;
        @BindString(R.string.language)
        public    String language;
        @BindString(R.string.fans)
        public     String fans;
        @BindString(R.string.events)
        public    String events;
        @BindString(R.string.category)
        public String category;
       /* @BindView(R.id.toolbar)
        public   Toolbar toolbar;*/
        @BindView(R.id.tabs)
        public    TabLayout tabLayout;
        @BindView(R.id.viewpager)
        public   ViewPager viewPager;

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
//            callWallPostApi();
            /*wall post sub - mscwall*/
//            callWallPostSubCategoryApi();
            /*wall post*/
//            apiCallWallPost();
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons();
            createSupportedToolbar(false);
            setTitle(getString(R.string.app_name));
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
            }
            return super.onOptionsItemSelected(item);
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
            params.addParam("cg_mcat","Hero");
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
            tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_favourite, 0, 0);
            tabLayout.getTabAt(0).setCustomView(tabOne);

            TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabTwo.setText(category);
            tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_call, 0, 0);
            tabLayout.getTabAt(1).setCustomView(tabTwo);

            TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabThree.setText(fans);
            tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_contacts, 0, 0);
            tabLayout.getTabAt(2).setCustomView(tabThree);

            TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabfour.setText(language);
            tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_favourite, 0, 0);
            tabLayout.getTabAt(3).setCustomView(tabfour);

            TextView tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabFive.setText(events);
            tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_call, 0, 0);
            tabLayout.getTabAt(4).setCustomView(tabFive);

            TextView tabSix = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabSix.setText(about);
            tabSix.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_contacts, 0, 0);
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
            adapter.addFrag(new FansClub(), fans);
            adapter.addFrag(new Language(),language);
            adapter.addFrag(new Events(),events);
            adapter.addFrag(new AboutUs(),about);
            viewPager.setAdapter(adapter);
        }



        private void dismissLoader() {
            Loader.dismissProgressBar();
        }

        private void updateErrorUI(String errorMsg) {
            ToastUtil.showErrorUpdate(this, errorMsg);
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
    }