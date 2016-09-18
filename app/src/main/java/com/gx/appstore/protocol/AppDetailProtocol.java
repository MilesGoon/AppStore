package com.gx.appstore.protocol;

import com.google.gson.Gson;
import com.gx.appstore.base.BaseProtocol;
import com.gx.appstore.bean.AppInfoBean;

import java.util.HashMap;
import java.util.Map;

public class AppDetailProtocol extends BaseProtocol<AppInfoBean> {

    private String mPackageName;

    public AppDetailProtocol(String packageName) {
        super();
        mPackageName = packageName;
    }

    @Override
    protected String getInterfaceKey() {
        return "detail";
    }

    @Override
    protected AppInfoBean parseJson(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, AppInfoBean.class);
    }

    //

    /**
     * 覆写getExtraParmas方法, 传递额外的参数
     */
    @Override
    protected Map<String, String> getExtraParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("packageName", mPackageName);
        return params;
    }
}
