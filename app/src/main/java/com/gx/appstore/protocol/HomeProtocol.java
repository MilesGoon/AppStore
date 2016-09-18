package com.gx.appstore.protocol;

import com.google.gson.Gson;
import com.gx.appstore.base.BaseProtocol;
import com.gx.appstore.bean.HomeBean;

public class HomeProtocol extends BaseProtocol<HomeBean> {

	@Override
	protected String getInterfaceKey() {
		return "home";
	}

	@Override
	protected HomeBean parseJson(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, HomeBean.class);
	}
}
