package com.gx.appstore.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gx.appstore.base.BaseProtocol;

import java.util.List;

public class HotProtocol extends BaseProtocol<List<String>> {

	@Override
	protected String getInterfaceKey() {
		return "hot";
	}

	@Override
	protected List<String> parseJson(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, new TypeToken<List<String>>() {
		}.getType());
	}
}
