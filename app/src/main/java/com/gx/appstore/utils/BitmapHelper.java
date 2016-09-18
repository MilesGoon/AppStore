package com.gx.appstore.utils;

import android.view.View;

import com.lidroid.xutils.BitmapUtils;

public class BitmapHelper {
    static BitmapUtils bitmapUtils;

    static {
        bitmapUtils = new BitmapUtils(UIUtils.getContext());
    }

    public static <T extends View> void display(T container, String uri) {
        bitmapUtils.display(container, uri);
    }
}
