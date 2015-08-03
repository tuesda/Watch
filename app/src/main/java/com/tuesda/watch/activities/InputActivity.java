package com.tuesda.watch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tuesda.watch.R;
import com.tuesda.watch.dribleSdk.AuthUtil;
import com.tuesda.watch.dribleSdk.DriRegInfo;
import com.tuesda.watch.httpnetwork.NetworkHandler;
import com.tuesda.watch.log.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanglei on 15/8/2.
 */
public class InputActivity extends Activity {

    private static final int RETRY_COUNT = 5;

    public static final String INPUT_NAME = "com.tuesda.watch.input.extra";
    public static final String SHOT_ID = "com.tuesda.watch.shot.id";
    public static final String INPUT_VALUE = "com.tuesda.watch.input.value";

    private String mName;
    private int mShotId;

    private RelativeLayout mNavCancel;
    private TextView mNavName;
    private RelativeLayout mNavPublish;
    private EditText mInputText;

    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        mName = getIntent().getStringExtra(INPUT_NAME);
        mShotId = getIntent().getIntExtra(SHOT_ID, 1);
        if (TextUtils.isEmpty(mName)) {
            mName = "Input";
        }
        initView();
        fillData();


    }

    private void initView() {
        mNavCancel = (RelativeLayout) findViewById(R.id.nav_cancel);
        mNavName = (TextView) findViewById(R.id.nav_name);
        mNavPublish = (RelativeLayout) findViewById(R.id.nav_publish);
        mInputText = (EditText) findViewById(R.id.input_edit);
        mProgress = (ProgressBar) findViewById(R.id.input_progress);
        mProgress.setVisibility(View.INVISIBLE);

        if (mInputText.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        mNavCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mNavPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mInputText.getText().toString();
                if (TextUtils.isEmpty(input)) {
                    Toast.makeText(InputActivity.this, "comment is empty", Toast.LENGTH_SHORT).show();
                } else {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    requestPublishCom(input);
                }

            }
        });
    }

    private void fillData() {
        mNavName.setText(mName);
    }


    private void requestPublishCom(String input) {
        final String accessToken = AuthUtil.getAccessToken(this);

        final JSONObject requestParams = new JSONObject();
        try {
            requestParams.put("body", input);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = DriRegInfo.REQUEST_ONE_SHOT_URL + mShotId + "/comments";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(InputActivity.this, "comment success", Toast.LENGTH_SHORT).show();
                        mProgress.setVisibility(View.INVISIBLE);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == 403) {
                    Toast.makeText(InputActivity.this, "only Player can comment", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InputActivity.this, "errors: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                }
                mProgress.setVisibility(View.INVISIBLE);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put(DriRegInfo.REQUEST_HEAD_AUTH_FIELD, DriRegInfo.REQUEST_HEAD_BEAR + accessToken);
                params.putAll(super.getHeaders());
                return params;
            }


        };

        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        NetworkHandler.getInstance(this).addToRequestQueue(request);
        mProgress.setVisibility(View.VISIBLE);
    }


}
