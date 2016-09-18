package com.gx.appstore.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gx.appstore.base.BaseProtocol;
import com.gx.appstore.bean.SubjectInfoBean;

import java.util.List;

public class SubjectProtocol extends BaseProtocol<List<SubjectInfoBean>> {

    @Override
    protected String getInterfaceKey() {
        return "subject";
    }

    @Override
    protected List<SubjectInfoBean> parseJson(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, new TypeToken<List<SubjectInfoBean>>() {
        }.getType());
    }

}
