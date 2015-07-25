package com.tuesda.watch.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tuesda.watch.R;
import com.tuesda.watch.dribleSdk.DriRegInfo;
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

    private TextView mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mContent = (TextView) findViewById(R.id.user_info_content);
        JSONObject params = new JSONObject();
        final String accessToken = getSharedPreferences(DriRegInfo.DRIBLE_MEM, Context.MODE_PRIVATE).getString(DriRegInfo.DRIBLE_TOKEN_FIELD, null);

        String url = "https://api.dribbble.com/v1/shots/2?access_token=" + accessToken;
        url = "https://api.dribbble.com/v1/user";


        NetworkHandler.getInstance(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET,  url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            StringBuilder strBuilder = new StringBuilder();
                            Iterator<String> iter = response.keys();
                            while (iter.hasNext()) {
                                String key = iter.next();
                                strBuilder.append(key);
                                strBuilder.append(": ");
                                strBuilder.append(response.get(key));
                                strBuilder.append("\n");
                            }
                            mContent.setText(strBuilder.toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Log.i("" +  response.get("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mContent.setText("Error occurs!!!");
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                Log.i("access token" + accessToken);
                params.put("Authorization", " Bearer " + accessToken);
//                return super.getHeaders();
                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Log.i(response.headers.toString());
                return super.parseNetworkResponse(response);
            }
        });
    }
}
