package com.tuesda.watch.dribleSdk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tuesda.watch.activities.LoginActivity;

/**
 * Created by zhanglei on 15/7/28.
 */
public class AuthUtil {
    public static String getAccessToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DriRegInfo.DRIBLE_MEM, Context.MODE_PRIVATE);
        String access_token = sharedPreferences.getString(DriRegInfo.DRIBLE_TOKEN_FIELD, null);
        if (TextUtils.isEmpty(access_token)) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
        return access_token;
    }

    public static int getMyId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DriRegInfo.ACCOUNT_INFO_MEM, Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt(DriRegInfo.ACCOUNT_USER_ID, -1);
        return id;
    }
}
