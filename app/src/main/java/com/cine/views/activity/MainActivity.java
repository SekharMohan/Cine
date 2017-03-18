    package com.cine.views.activity;

    import android.os.Bundle;
    import android.support.design.widget.TabLayout;
    import android.support.v4.app.Fragment;
    import android.support.v4.app.FragmentManager;
    import android.support.v4.app.FragmentPagerAdapter;
    import android.support.v4.view.ViewPager;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.view.LayoutInflater;
    import android.widget.TextView;

    import com.cine.R;
    import com.cine.views.fragments.AboutUs;
    import com.cine.views.fragments.Category;
    import com.cine.views.fragments.Events;
    import com.cine.views.fragments.FansClub;
    import com.cine.views.fragments.Home;
    import com.cine.views.fragments.Language;

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
        @BindView(R.id.toolbar)
        public   Toolbar toolbar;
        @BindView(R.id.tabs)
        public    TabLayout tabLayout;
        @BindView(R.id.viewpager)
        public   ViewPager viewPager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons();
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