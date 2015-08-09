package com.tuesda.watch.dribleSdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tuesda.watch.activities.HomeActivity;
import com.tuesda.watch.activities.LoginActivity;
import com.tuesda.watch.dribleSdk.data.DribleShot;
import com.tuesda.watch.dribleSdk.data.DribleUser;
import com.tuesda.watch.log.Log;

/**
 * Created by zhanglei on 15/7/28.
 */
public class AuthUtil {
    public static String getAccessToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LoginActivity.DRIBLE_MEM, Context.MODE_PRIVATE);
        String access_token = sharedPreferences.getString(LoginActivity.DRIBLE_TOKEN_FIELD, null);
        if (TextUtils.isEmpty(access_token)) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
        return access_token;
    }

    public static DribleUser getMe(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LoginActivity.ACCOUNT_INFO_MEM, Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt(LoginActivity.ACCOUNT_USER_ID, -1);

        if (id == -1) {
            Intent intent = new Intent(context, LoginActivity.class);
            ((Activity) context).finish();
            ((Activity) context).overridePendingTransition(0, 0);

            context.startActivity(intent);

            return null;
        }

        String name = sharedPreferences.getString(LoginActivity.ACCOUNT_NAME, null);
        String avatar_url = sharedPreferences.getString(LoginActivity.ACCOUNT_USER_AVATAR_URL, null);
        String like_url = sharedPreferences.getString(LoginActivity.ACCOUNT_USER_LIKE_URL, null);
        String buckets_url = sharedPreferences.getString(LoginActivity.ACCOUNT_USER_BUCKETS_URL, null);

        DribleUser me = new DribleUser();
        me.setId(id);
        me.setName(name);
        me.setAvatar_url(avatar_url);
        me.setLikes_url(like_url);
        me.setBuckets_url(buckets_url);
        return me;
    }

    public static void checkIfLogin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LoginActivity.DRIBLE_MEM, Context.MODE_PRIVATE);
        String access = sharedPreferences.getString(LoginActivity.DRIBLE_TOKEN_FIELD, null);
        if (!TextUtils.isEmpty(access)) {
            goHome(context);
        } else {
            goLogin(context);
        }
    }

    public static void goHome(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    public static  void goLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static boolean hasUserInfo(Context context) {
        SharedPreferences shared = context.getSharedPreferences(LoginActivity.ACCOUNT_INFO_MEM, Context.MODE_PRIVATE);
        int id = shared.getInt(LoginActivity.ACCOUNT_USER_ID, -1);
        return id != -1;

    }

    private static void clearAuthInfo(Context context) {
        SharedPreferences shared = context.getSharedPreferences(LoginActivity.DRIBLE_MEM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.clear();
        editor.commit();

        SharedPreferences sharedAccount = context.getSharedPreferences(LoginActivity.ACCOUNT_INFO_MEM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorAcc = sharedAccount.edit();
        editorAcc.clear();
        editorAcc.commit();
    }

}
