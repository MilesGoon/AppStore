package com.gx.appstore.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.gx.appstore.base.BaseFragment;
import com.gx.appstore.base.BaseHolder;
import com.gx.appstore.base.LoadingController;
import com.gx.appstore.base.SuperBaseAdapter;
import com.gx.appstore.bean.CategoryBean;
import com.gx.appstore.factory.ListViewFactory;
import com.gx.appstore.holder.CategoryInfoHolder;
import com.gx.appstore.holder.CategoryTitleHolder;
import com.gx.appstore.protocol.CategoryProtocol;
import com.gx.appstore.utils.LogUtils;

import java.util.List;

public class CategoryFragment extends BaseFragment {

    private List<CategoryBean> mDatas;

    @Override
    protected LoadingController.LoadedResult initData() {
        CategoryProtocol protocol = new CategoryProtocol();
        try {
            mDatas = protocol.loadData(0);
            LogUtils.printList(mDatas);
            return checkState(mDatas);
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingController.LoadedResult.ERROR;
        }
    }

    @Override
    protected View initSuccessView() {
        ListView listView = ListViewFactory.createListView();
        listView.setAdapter(new CategoryAdapter(listView, mDatas));
        return listView;
    }

    class CategoryAdapter extends SuperBaseAdapter<CategoryBean> {

        public CategoryAdapter(AbsListView absListView, List<CategoryBean> datas) {
            super(absListView, datas);
        }

        @Override
        protected BaseHolder<CategoryBean> getSpecialHolder(int position) {
            CategoryBean categoryBean = mDatas.get(position);
            if (categoryBean.isTitle) {
                return new CategoryTitleHolder();
            } else {
                return new CategoryInfoHolder();
            }
        }

        // 因为现在实际有3种ViewType,但是我们的SuperBaseAdapter中getViewTypeCount还是返回的2种 必定会出现混乱
        // 还需要覆写SuperBaseAdapter里面的getViewTypeCount
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;// 2+1(loadmore)=3-->正好就是我们实际有的viewType的count值
        }

        @Override
        protected int getNormalItemType(int position) {
            // 1、2
            CategoryBean categoryBean = mDatas.get(position);
            if (categoryBean.isTitle) {
                return 2;
            } else {
                return 1;
            }
        }

        @Override
        protected boolean hasLoadMore() {
            return false;
        }
    }

}
