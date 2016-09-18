package com.gx.appstore.config;


import com.gx.appstore.utils.LogUtils;

public class Constants {

    public static final int DEBUGLEVEL = LogUtils.LEVEL_ALL;
    public static final int PAGERSIZE = 20;
    public static final long PROTOCOLTIMEOUT = 5 * 60 * 1000;

    public static final class URLS {
        public static final String BASEURL = "http://10.0.3.2:8080/GooglePlayServer/";
        public static final String IMAGEBASEURL = BASEURL + "image?name=";
    }

    public static final class PAY {
        public static final String PAYTYPE_ZHIFUBAO = "1";
        public static final String PAYTYPE_YL = "2";
        public static final String PAYTYPE_WX = "3";
    }
}
