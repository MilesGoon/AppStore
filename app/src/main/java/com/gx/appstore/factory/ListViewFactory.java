package com.gx.appstore.factory;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ListView;

import com.gx.appstore.utils.UIUtils;


public class ListViewFactory {
	public static ListView createListView() {
		ListView listView = new ListView(UIUtils.getContext());
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setFastScrollEnabled(true);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		return listView;
	}
}
