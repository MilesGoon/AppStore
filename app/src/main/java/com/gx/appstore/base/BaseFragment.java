package com.gx.appstore.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gx.appstore.utils.UIUtils;

import java.util.List;
import java.util.Map;

public abstract class BaseFragment extends Fragment {
    // 控制视图展示及数据加载时机
    private LoadingController mLoadingController;

    public LoadingController getLoadingController() {
        return mLoadingController;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 第一次
        if (mLoadingController == null) {
            mLoadingController = new LoadingController(UIUtils.getContext()) {

                @Override
                protected LoadedResult initData() {
                    return BaseFragment.this.initData();
                }

                @Override
                protected View initSuccessView() {
                    return BaseFragment.this.initSuccessView();
                }
            };
        } else {
            ViewGroup vg = ((ViewGroup) mLoadingController.getParent());
            if (vg != null) {
                vg.removeView(mLoadingController);
            }
        }
        return mLoadingController;
    }

    /**
     * 它是和LoadingPager中的initData同名方法
     * triggerLoadData方法被调用的时候调用
     */
    protected abstract LoadingController.LoadedResult initData();

    /**
     * 它是和LoadingPager中的initSuccessView同名方法
     * 数据加载成功的时候调用
     */
    protected abstract View initSuccessView();

    /**
     * 检测加载网络之后返回数据对应的状态  非空即SUCCESS
     */
    public LoadingController.LoadedResult checkState(Object obj) {
        if (obj == null) {
            return LoadingController.LoadedResult.EMPTY;
        }
        // list
        if (obj instanceof List) {
            if (((List) obj).size() == 0) {
                return LoadingController.LoadedResult.EMPTY;
            }
        }
        // map
        if (obj instanceof Map) {
            if (((Map) obj).size() == 0) {
                return LoadingController.LoadedResult.EMPTY;
            }
        }

        return LoadingController.LoadedResult.SUCCESS;
    }
}

