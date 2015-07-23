package com.tuesda.watch.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tuesda.watch.R;
import com.tuesda.watch.dribleSdk.DriRegInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhanglei on 15/7/23.
 */
public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";

    private WebView mLoginWeb;
    private String mReturnCode;
    private String accessToken;

    private ProgressBar mProgress;

    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mQueue = Volley.newRequestQueue(this);

        mLoginWeb = (WebView) findViewById(R.id.login_web);
        mProgress = (ProgressBar) findViewById(R.id.login_progress);
        mProgress.setVisibility(View.INVISIBLE);


        if (mReturnCode == null) {
            mProgress.setVisibility(View.VISIBLE);
            mLoginWeb.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.startsWith(DriRegInfo.DRIBLE_CALL_BACK)) {
                        if (url.indexOf("code=")!=-1) {
                            // save the code str
                            mReturnCode = getCodeFromUrl(url);
                        }

                        if (!TextUtils.isEmpty(mReturnCode)) {
                            requestForAccessToken();
                        } else {
                            Log.i(TAG, "code returned is empty");
                        }

                        //finish(); // return last acitivty
                        //don't go redirect url
                        return true;
                    }
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    mProgress.setVisibility(View.INVISIBLE);
                }
            });

            mLoginWeb.loadUrl(DriRegInfo.DRIBLE_LOGIN_URL);
            Log.i(TAG, DriRegInfo.DRIBLE_LOGIN_URL);
        } else {
            Toast.makeText(LoginActivity.this, "code have already exists", Toast.LENGTH_LONG).show();
            //finish();
        }



    }

    private String getCodeFromUrl(String url) {
        int startIndex = url.indexOf("code=") + "code=".length();
        int endIndex = url.indexOf("&state");
        String code = url.substring(startIndex, endIndex);
        Log.i(TAG, "code=" + code);
        return code;
    }

    private void requestForAccessToken() {
        final JSONObject requestJson = new JSONObject();
        try {
            requestJson.put("client_id", DriRegInfo.DRIBLE_CLIENT_ID);
            requestJson.put("client_secret", DriRegInfo.DRIBLE_SECRET);
            requestJson.put("code", mReturnCode);
            requestJson.put("state", DriRegInfo.mState);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, DriRegInfo.DRIBLE_TOKEN_URL, requestJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());
                        finish();
                    }
                },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "error occurs: " + error.getCause());
                finish();
            }
        });
        mQueue.add(jsonObjectRequest);

    }
}
