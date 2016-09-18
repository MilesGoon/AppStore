package com.gx.appstore.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gx.appstore.base.BaseProtocol;
import com.gx.appstore.bean.AppInfoBean;

import java.util.List;

public class AppProtocol extends BaseProtocol<List<AppInfoBean>> {

    @Override
    protected String getInterfaceKey() {
        return "app";
    }

    @Override
    protected List<AppInfoBean> parseJson(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, new TypeToken<List<AppInfoBean>>() {
        }.getType());
    }

}
