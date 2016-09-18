package com.gx.appstore.factory;

import android.support.v4.util.SparseArrayCompat;

import com.gx.appstore.base.BaseFragment;
import com.gx.appstore.fragment.AppFragment;
import com.gx.appstore.fragment.CategoryFragment;
import com.gx.appstore.fragment.GameFragment;
import com.gx.appstore.fragment.HomeFragment;
import com.gx.appstore.fragment.HotFragment;
import com.gx.appstore.fragment.RecommendFragment;
import com.gx.appstore.fragment.SubjectFragment;

public class FragmentFactory {
    public static final int FRAGMENT_HOME = 0;
    public static final int FRAGMENT_APP = 1;
    public static final int FRAGMENT_GAME = 2;
    public static final int FRAGMENT_SUBJECT = 3;
    public static final int FRAGMENT_RECOMMEND = 4;
    public static final int FRAGMENT_CATEGORY = 5;
    public static final int FRAGMENT_HOT = 6;
    private static SparseArrayCompat<BaseFragment> cacheFragmentArray = new SparseArrayCompat<BaseFragment>();

    public static BaseFragment getFragment(int position) {
        BaseFragment fragment = null;
        if (cacheFragmentArray.get(position) != null) {
            fragment = cacheFragmentArray.get(position);
            return fragment;
        }
        switch (position) {
            case FRAGMENT_HOME:
                fragment = new HomeFragment();
                break;

            case FRAGMENT_APP:
                fragment = new AppFragment();
                break;

            case FRAGMENT_GAME:
                fragment = new GameFragment();
                break;

            case FRAGMENT_SUBJECT:
                fragment = new SubjectFragment();
                break;

            case FRAGMENT_RECOMMEND:
                fragment = new RecommendFragment();
                break;

            case FRAGMENT_CATEGORY:
                fragment = new CategoryFragment();
                break;

            case FRAGMENT_HOT:
                fragment = new HotFragment();
                break;
        }

        cacheFragmentArray.put(position, fragment);
        return fragment;
    }

}
