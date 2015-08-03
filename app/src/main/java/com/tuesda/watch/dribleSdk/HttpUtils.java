package com.tuesda.watch.dribleSdk;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by zhanglei on 15/8/2.
 */
public class HttpUtils {
    public static HashMap<String, String> genNextUrl(String link) {
        HashMap<String, String> result = new HashMap<String, String>();
        if (TextUtils.isEmpty(link)) {
            return null;
        }
        String [] links = link.split(",");
        for (int i=0; i< links.length; i++) {
            String str = links[i];
            String url = str.substring(str.indexOf("<") + 1, str.lastIndexOf(">"));
            String flag = str.substring(str.indexOf("rel=\"") + 5, str.lastIndexOf("\""));
            result.put(flag, url);
        }
        return result;
    }
}
