package com.gx.appstore.protocol;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.gx.appstore.base.BaseProtocol;
import com.gx.appstore.bean.AppInfoBean;

import java.util.ArrayList;
import java.util.List;

public class GameProtocol extends BaseProtocol<List<AppInfoBean>> {

    @Override
    protected String getInterfaceKey() {
        return "game";
    }

    @Override
    protected List<AppInfoBean> parseJson(String jsonString) {
        List<AppInfoBean> appInfoBeans = new ArrayList<AppInfoBean>();

        // 获得 解析者
        JsonParser parser = new JsonParser();
        // string-->jsonElement
        JsonElement rootJsonElent = parser.parse(jsonString);
        // JsonElement-->JsonArray
        JsonArray rootJsonArray = rootJsonElent.getAsJsonArray();
        // 遍历jsonArrary
        for (JsonElement itemJsonElement : rootJsonArray) {

            JsonObject itemJsonObject = itemJsonElement.getAsJsonObject();

            JsonElement nameJsonElement = itemJsonObject.get("name");
            // JsonElement-->JsonPrimitive
            JsonPrimitive namePrimitive = nameJsonElement.getAsJsonPrimitive();
            // JsonPrimitive-->String
            String name = namePrimitive.getAsString();

            JsonElement urlJsonElement = itemJsonObject.get("iconUrl");
            // JsonElement-->String
            String iconUrl = urlJsonElement.getAsString();

            long id = itemJsonObject.get("id").getAsLong();
            String packageName = itemJsonObject.get("packageName").getAsString();
            float stars = itemJsonObject.get("stars").getAsFloat();
            long size = itemJsonObject.get("size").getAsLong();
            String downloadUrl = itemJsonObject.get("downloadUrl").getAsString();
            String des = itemJsonObject.get("des").getAsString();

            // 创建AppInfoBean
            AppInfoBean info = new AppInfoBean();
            info.des = des;
            info.downloadUrl = downloadUrl;
            info.iconUrl = iconUrl;
            info.id = id;
            info.name = name;
            info.packageName = packageName;
            info.size = size;
            info.stars = stars;

            // 添加到集合
            appInfoBeans.add(info);
        }

        return appInfoBeans;
    }
}
