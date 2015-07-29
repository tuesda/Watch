package com.tuesda.watch.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tuesda.watch.R;
import com.tuesda.watch.dribleSdk.DriRegInfo;
import com.tuesda.watch.dribleSdk.data.DribleUser;
import com.tuesda.watch.httpnetwork.NetworkHandler;
import com.tuesda.watch.log.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhanglei on 15/7/24.
 */
public class UserInfoActivity extends Activity {

    private RelativeLayout mNavBack;
    private static final int RETRY_COUNT = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
        setUpRequest();
    }

    private void initView() {
        mNavBack = (RelativeLayout) findViewById(R.id.nav_back);
        mNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }




    private void setUpRequest() {
        int user_id = getIntent().getIntExtra(DriRegInfo.INTENT_USER_ID, 1);
        String url = DriRegInfo.REQUEST_USER_URL + user_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserInfoActivity.this, "error", Toast.LENGTH_LONG).show();
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences sharedPreferences = getSharedPreferences(DriRegInfo.DRIBLE_MEM, Context.MODE_PRIVATE);
                String access_token = sharedPreferences.getString(DriRegInfo.DRIBLE_TOKEN_FIELD, null);
                if (TextUtils.isEmpty(access_token)) {
                    Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(DriRegInfo.REQUEST_HEAD_AUTH_FIELD, DriRegInfo.REQUEST_HEAD_BEAR + access_token);
                params.putAll(super.getHeaders());
                // Log.i(params.toString());
                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                // Log.i(" status code: " + response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };
        // request.setShouldCache(false);

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 0;
            }

            @Override
            public int getCurrentRetryCount() {
                return RETRY_COUNT;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                Log.e("Retry Error: " + error.toString());
            }
        });

        NetworkHandler.getInstance(this).addToRequestQueue(request);
    }
    private void parseJson(JSONObject data) {
        // Log.i("user info: " + data.toString());
        DribleUser dribleUser = new DribleUser(data);
        Log.i("dribleUser: " + dribleUser);
    }


}
