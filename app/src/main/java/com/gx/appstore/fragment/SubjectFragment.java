package com.gx.appstore.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.gx.appstore.base.BaseFragment;
import com.gx.appstore.base.BaseHolder;
import com.gx.appstore.base.LoadingController;
import com.gx.appstore.base.SuperBaseAdapter;
import com.gx.appstore.bean.SubjectInfoBean;
import com.gx.appstore.factory.ListViewFactory;
import com.gx.appstore.holder.SubjectHolder;
import com.gx.appstore.protocol.SubjectProtocol;

import java.util.List;


public class SubjectFragment extends BaseFragment {

	private List<SubjectInfoBean>	mDatas;
	private SubjectProtocol mProtocol;

	@Override
	protected LoadingController.LoadedResult initData() {
		mProtocol = new SubjectProtocol();
		try {
			mDatas = mProtocol.loadData(0);
			return checkState(mDatas);
		} catch (Exception e) {
			e.printStackTrace();
			return LoadingController.LoadedResult.ERROR;
		}
	}

	@Override
	protected View initSuccessView() {
		ListView listView = ListViewFactory.createListView();
		listView.setAdapter(new SubjectAdapter(listView, mDatas));
		return listView;
	}

	class SubjectAdapter extends SuperBaseAdapter<SubjectInfoBean> {

		public SubjectAdapter(AbsListView absListView, List<SubjectInfoBean> datas) {
			super(absListView, datas);
		}

		@Override
		protected BaseHolder<SubjectInfoBean> getSpecialHolder(int position) {
			return new SubjectHolder();
		}

		@Override
		public List<SubjectInfoBean> onLoadMore() throws Exception {
			return mProtocol.loadData(mDatas.size());
		}

	}
}