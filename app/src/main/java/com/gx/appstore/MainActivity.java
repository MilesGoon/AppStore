package com.gx.appstore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.gx.appstore.base.BaseFragment;
import com.gx.appstore.factory.FragmentFactory;
import com.gx.appstore.holder.MenuHolder;
import com.gx.appstore.lib.PagerSlidingTabStrip;
import com.gx.appstore.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_leftmenu)
    FrameLayout mMainLeftmenu;
    @Bind(R.id.main_tabs)
    PagerSlidingTabStrip mMainTabs;
    @Bind(R.id.main_viewpager)
    ViewPager mMainViewpager;
    @Bind(R.id.main_drawerlayout)
    DrawerLayout mMainDrawerlayout;
    @Bind(R.id.main_toolbar)
    Toolbar mToolbar;
    private ActionBarDrawerToggle mToggle;
    private String[] mMainTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolBar();
        initToogle();
        initViewAndDate();
        initListener();
    }

    private void initToolBar() {
        mToolbar.setLogo(R.drawable.ic_launcher);
        mToolbar.setTitle("AppStore");
        setSupportActionBar(mToolbar);
    }

    private void initToogle() {
        mToggle = new ActionBarDrawerToggle(MainActivity.this, mMainDrawerlayout, mToolbar, R.string.open, R.string.close);
        mToggle.syncState();
        mMainDrawerlayout.addDrawerListener(mToggle);
    }

    private void initViewAndDate() {
        mMainTitles = UIUtils.getStringArr(R.array.main_titles);
        HomeFragmentPagerAdapter adapter1 = new HomeFragmentPagerAdapter(getSupportFragmentManager());
        mMainViewpager.setAdapter(adapter1);
        mMainTabs.setViewPager(mMainViewpager);

        MenuHolder menuHolder = new MenuHolder();
        mMainLeftmenu.addView(menuHolder.mHolderView);
    }

    private void initListener() {
        final ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                BaseFragment fragment = FragmentFactory.getFragment(position);
                fragment.getLoadingController().LoadDataTrigger();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        mMainViewpager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                listener.onPageSelected(0);
                mMainViewpager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mMainTabs.setOnPageChangeListener(listener);
    }

    class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

        public HomeFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentFactory.getFragment(position);
        }

        @Override
        public int getCount() {
            if (mMainTitles != null) {
                return mMainTitles.length;
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mMainTitles[position];
        }

    }
}