package com.gx.appstore.base;

import com.gx.appstore.config.Constants;
import com.gx.appstore.utils.FileUtils;
import com.gx.appstore.utils.IOUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public abstract class BaseProtocol<T> {

    public T loadData(int index) throws Exception {
        // 首先从本地加载数据
        T t = getDataFromLocal(index);
        if (t != null) {
            return t;
        }
        // 不合格则从网络加载
        T parseJson = getDataFromNet(index);
        return parseJson;
    }

    /**
     * 从本地加载数据
     */
    private T getDataFromLocal(int index) {
        try {
            File cacheFile = getCacheFile(index);
            if (cacheFile.exists()) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(cacheFile));
                    // 读取插入时间
                    String timeStr = reader.readLine();
                    long time_ = Long.parseLong(timeStr);
                    // 判断是否过期
                    if (System.currentTimeMillis() - time_ < Constants.PROTOCOLTIMEOUT) {
                        // 未过期
                        String cacheJsonString = reader.readLine();
                        return parseJson(cacheJsonString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.close(reader);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 在指定路径创建文件并返回
     */
    public File getCacheFile(int index) {
        String dir = FileUtils.getDir("json");// sdcard/Android/data/包名/json
        Map<String, String> extraParams = getExtraParams();
        String name = "";
        if (extraParams != null) {
            // 走的详情协议
            for (Map.Entry<String, String> info : extraParams.entrySet()) {
                String packageName = info.getValue();
                name = getInterfaceKey() + "." + packageName;
            }
        } else {
            name = getInterfaceKey() + "." + index;
        }
        File cacheFile = new File(dir, name);
        return cacheFile;
    }

    /**
     * 从网络加载数据
     */
    public T getDataFromNet(int index) throws HttpException, IOException {
        HttpUtils httpUtils = new HttpUtils();
        String url = Constants.URLS.BASEURL + getInterfaceKey();

        RequestParams params = new RequestParams();
        Map<String, String> extraParams = getExtraParams();

        if (extraParams != null) {
            // 子类覆写了该方法
            for (Map.Entry<String, String> info : extraParams.entrySet()) {
                String key = info.getKey();
                String packageName = info.getValue();
                params.addQueryStringParameter(key, "" + packageName);
            }
        } else {
            params.addQueryStringParameter("index", "" + index);

        }

        ResponseStream responseStream = httpUtils.sendSync(HttpMethod.GET, url, params);
        String jsonString = responseStream.readString();

        // 写入缓存
        File cacheFile = getCacheFile(index);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(cacheFile));
            writer.write(System.currentTimeMillis() + "");
            // 换行
            writer.write("\r\n");
            // 开始写入jsonString
            writer.write(jsonString);
        } catch (Exception e) {

        } finally {
            IOUtils.close(writer);
        }

        T parseJson = parseJson(jsonString);
        return parseJson;
    }

    /**
     * 传递额外的参数, 默认情况是null
     */
    protected Map<String, String> getExtraParams() {
        return null;
    }

    /**
     * 返回协议的关键字home/game/subject
     */
    protected abstract String getInterfaceKey();

    /**
     * jsonString->实体bean的过程
     */
    protected abstract T parseJson(String jsonString);
}
